/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package extractors

import cats.implicits._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

import base.SpecBase
import extractors.ValidationError.MissingField
import models.completion.downstream.{DangerousGoodsCode, GoodsItemIdentity, Package}
import models.completion.answers.{GoodsItem, Parties, RouteDetails}
import models._
import pages.goods._
import pages.predec.ProvideGrossWeightPage
import queries.goods.{AllContainersQuery, AllDocumentsQuery}

class GoodsItemExtractorSpec extends SpecBase {
  private val routeDetails = arbitrary[RouteDetails].sample.value
  private val parties = arbitrary[Parties].sample.value

  private val itemNumber = Index(0)
  private val commodityCode = arbitrary[GoodsItemIdentity.ByCommodityCode].sample.value
  private val desc = arbitrary[GoodsItemIdentity.WithDescription].sample.value

  private val consignorIndex = Gen.choose(1, parties.consignors.size).sample.value - 1
  private val consigneeIndex = Gen.choose(1, parties.consignees.size).sample.value - 1
  private val notifiedPartyIndex = Gen.choose(1, parties.notifiedParties.size).sample.value - 1

  private val consignor = parties.consignors.get(consignorIndex).get
  private val consignee = parties.consignees.get(consigneeIndex).get
  private val notifiedParty = parties.notifiedParties.get(notifiedPartyIndex).get

  private val placesOfLoading = routeDetails.placesOfLoading
  private val placeOfLoadingIndex = Gen.choose(1, placesOfLoading.size).sample.value - 1
  private val selectedPlaceOfLoading = placesOfLoading.get(placeOfLoadingIndex).get

  private val placesOfUnloading = routeDetails.placesOfUnloading
  private val placeOfUnloadingIndex = Gen.choose(1, placesOfUnloading.size).sample.value - 1
  private val selectedPlaceOfUnloading = placesOfUnloading.get(placeOfUnloadingIndex).get

  private val grossMass = 1000
  private val paymentMethod = arbitrary[PaymentMethod].sample.value
  private val dangerousGoods = arbitrary[DangerousGood].sample.value
  private val standardPackage = Gen.oneOf(KindOfPackage.standardKindsOfPackages).sample.value
  private val unpackedPackage = Gen.oneOf(KindOfPackage.unpackedKindsOfPackage).sample.value
  private val bulkPackage = Gen.oneOf(KindOfPackage.bulkKindsOfPackage).sample.value
  private val numPackages = Gen.choose(1, 10).sample.value
  private val numPieces = Gen.choose(1, 10).sample.value
  private val mark = stringsWithMaxLength(10).sample.value

  private val documents = {
    Gen.choose(1, 10)
      .flatMap { len => Gen.listOfN(len, arbitrary[Document]) }
      .sample.value
  }

  private val containers = {
    Gen.choose(1, 10)
      .flatMap { len => Gen.listOfN(len, arbitrary[Container]) }
      .sample.value
  }

  private val packages = List(
    Package(
      standardPackage,
      Some(numPackages),
      None,
      Some(mark)
    )
  )

  private val expectedResult = GoodsItem(
    commodityCode,
    consignor,
    Some(consignee),
    None,
    selectedPlaceOfLoading,
    selectedPlaceOfUnloading,
    containers,
    packages,
    Some(grossMass),
    documents,
    Some(DangerousGoodsCode(dangerousGoods.code)),
    paymentMethod,
  )

  private val validAnswers = {
    emptyUserAnswers
      .set(ProvideGrossWeightPage, ProvideGrossWeight.PerItem).success.value
      .set(CommodityCodeKnownPage(itemNumber), true).success.value
      .set(CommodityCodePage(itemNumber), commodityCode.code).success.value
      .set(ConsignorPage(itemNumber), consignorIndex).success.value
      .set(ConsigneeKnownPage(itemNumber), true).success.value
      .set(ConsigneePage(itemNumber), consigneeIndex).success.value
      .set(LoadingPlacePage(itemNumber), placeOfLoadingIndex).success.value
      .set(UnloadingPlacePage(itemNumber), placeOfUnloadingIndex).success.value
      .set(AnyShippingContainersPage(itemNumber), true).success.value
      .set(AllContainersQuery(itemNumber), containers).success.value
      .set(GoodsItemGrossWeightPage(itemNumber), BigDecimal.exact(grossMass)).success.value
      .set(KindOfPackagePage(itemNumber, Index(0)), standardPackage).success.value
      .set(NumberOfPackagesPage(itemNumber, Index(0)), numPackages).success.value
      .set(MarkOrNumberPage(itemNumber, Index(0)), mark).success.value
      .set(AddAnyDocumentsPage(itemNumber), true).success.value
      .set(AllDocumentsQuery(itemNumber), documents).success.value
      .set(DangerousGoodPage(itemNumber), true).success.value
      .set(DangerousGoodCodePage(itemNumber), dangerousGoods).success.value
      .set(PaymentMethodPage(itemNumber), paymentMethod).success.value
  }

  "The goods item extractor" - {
    "should correctly extract valid goods item" in {
      val actual = new GoodsItemExtractor(placesOfLoading, placesOfUnloading, parties, itemNumber)(validAnswers).extract().value
      actual must be(expectedResult)
    }

    "commodity code" - {
      "should correctly extract code if code known" in {
        val answers = {
          validAnswers
            .set(CommodityCodeKnownPage(itemNumber), true).success.value
            .set(CommodityCodePage(itemNumber), commodityCode.code).success.value
        }
        val expected = expectedResult.copy(id = commodityCode)
        val actual = new GoodsItemExtractor(placesOfLoading, placesOfUnloading, parties, itemNumber)(answers).extract().value

        actual must be(expected)
      }
      "should fail if code known but not provided" in {
        val answers = {
          validAnswers
            .set(CommodityCodeKnownPage(itemNumber), true).success.value
            .remove(CommodityCodePage(itemNumber)).success.value
        }
        val expected = List(MissingField(CommodityCodePage(itemNumber)))
        val actual = new GoodsItemExtractor(placesOfLoading, placesOfUnloading, parties, itemNumber)(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }
      "should correctly extract description if code not known" in {
        val answers = {
          validAnswers
            .set(CommodityCodeKnownPage(itemNumber), false).success.value
            .set(GoodsDescriptionPage(itemNumber), desc.desc).success.value
        }
        val expected = expectedResult.copy(id = desc)
        val actual = new GoodsItemExtractor(placesOfLoading, placesOfUnloading, parties, itemNumber)(answers).extract().value

        actual must be(expected)
      }
      "should fail if code not known and no description provided" in {
        val answers = {
          validAnswers
            .set(CommodityCodeKnownPage(itemNumber), false).success.value
            .remove(GoodsDescriptionPage(itemNumber)).success.value
        }
        val expected = List(MissingField(GoodsDescriptionPage(itemNumber)))
        val actual = new GoodsItemExtractor(placesOfLoading, placesOfUnloading, parties, itemNumber)(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }
    }

    "consignor" - {
      "should fail if not provided" in {
        val answers = {
          validAnswers
            .remove(ConsignorPage(itemNumber)).success.value
        }
        val expected = List(MissingField(ConsignorPage(itemNumber)))
        val actual = new GoodsItemExtractor(placesOfLoading, placesOfUnloading, parties, itemNumber)(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }
    }

    "consignee" - {
      "should extract correctly if 0 notified parties" in {
        val answers = {
          validAnswers
            .remove(ConsigneeKnownPage(itemNumber)).success.value
            .set(ConsigneePage(itemNumber), consigneeIndex).success.value
        }
        val suppliedParties = Parties(parties.consignors, parties.consignees, Map.empty)
        val actual = new GoodsItemExtractor(placesOfLoading, placesOfUnloading, suppliedParties, itemNumber)(answers).extract().value

        actual must be(expectedResult)
      }
      "should extract correctly if consignee selected in consignee known page" in {
        val answers = {
          validAnswers
            .set(ConsigneeKnownPage(itemNumber), true).success.value
            .set(ConsigneePage(itemNumber), consigneeIndex).success.value
        }
        val actual = new GoodsItemExtractor(placesOfLoading, placesOfUnloading, parties, itemNumber)(answers).extract().value

        actual must be(expectedResult)
      }
      "should fail if 0 notified parties and not provided" in {
      }
      "should fail if consignee selected in consignee known page and not provided" in {
      }
      "should select the correct consignee from the parties section" in {
      }
    }

    "notified party" - {
      "should extract correctly if 0 consignees" in {
      }
      "should extract correctly if notified party selected in consignee known page" in {
      }
      "should fail if 0 consignees and not provided" in {
      }
      "should fail if notified party selected in consignee known page and not provided" in {
      }
      "should select the correct notified party from the parties section" in {
      }
    }

    "loading place" - {
      "should extract automaticall if the user has only one loading place" in {
      }
      "should fail if not provided" in {
      }
    }

    "unloading place" - {
      "should extract automaticall if the user has only one loading place" in {
      }
      "should fail if not provided" in {
      }
    }

    "parties and places section" - {
      "should extract correctly if the user has pre-provided only 1 consigner, consignee, notified party, loading place and unloading place" in {
      }
    }

    "container number" - {
      "should fail if goods are in a container but container number not provided" in {
      }
    }

    "item weight" - {
      "should extract correctly if overall weight not provided in predec" - {
      }
      "should fail if overall weight not provided in predec and item weight not provided" - {
      }
    }

    "packaging" - {
      "should fail if no packaging type provided" in {
      }

      "packed" - {
        "should correctly extract number of pieces and item mark if packed type selected" in {
        }
        "should fail if packed type selected and number of pieces not provided" in {
        }
      }

      "unpacked" - {
        "should correctly extract number of packages if unpacked type selected" in {
        }
        "Should fail if unpacked type selected and number of packages not provided" in {
        }
        "should correctly extract item mark if answered yes to add item mark page" in {
        }
        "should fail if answered yes to add item mark page but no item mark provided" in {
        }
      }

      "bulk" - {
        "should correctly extract item mark if answered yes to add item mark page" in {
        }
        "should fail if answered yes to add item mark page but no item mark provided" in {
        }
      }
    }

    "documents" - {
      "should correctly extract documents if answered is yes to add any documents page" in {
      }
      "should fail if answered yes to add any documents page but documents not provided" in {
      }
    }

    "dangerous code" - {
      "should correctly extract documents if answered yes to is dangerous good page" in {
      }
      "should fail if answered yes to dangerous good page but dangerous good code not provided" in {
      }
    }

    "payment method" - {
      "should fail if payment method not provided" in {
      }
    }
  }
}
