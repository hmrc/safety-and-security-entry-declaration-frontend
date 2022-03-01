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

package generators

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import pages.consignors.{ConsignorAddressPage, ConsignorEORIPage, ConsignorNamePage, ConsignorsIdentityPage}
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {
  implicit lazy val arbitraryCarrierPaymentMethodUserAnswersEntry
    : Arbitrary[(CarrierPaymentMethodPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CarrierPaymentMethodPage]
        value <- arbitrary[CarrierPaymentMethod].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddPaymentMethodUserAnswersEntry
    : Arbitrary[(AddPaymentMethodPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[AddPaymentMethodPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryUnloadingCodeUserAnswersEntry
    : Arbitrary[(UnloadingCodePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[UnloadingCodePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNotifiedPartyNameUserAnswersEntry
    : Arbitrary[(NotifiedPartyNamePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NotifiedPartyNamePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNotifiedPartyIdentityUserAnswersEntry
    : Arbitrary[(NotifiedPartyIdentityPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NotifiedPartyIdentityPage]
        value <- arbitrary[NotifiedPartyIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNotifiedPartyEORIUserAnswersEntry
    : Arbitrary[(NotifiedPartyEORIPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NotifiedPartyEORIPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNotifiedPartyAddressUserAnswersEntry
    : Arbitrary[(NotifiedPartyAddressPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NotifiedPartyAddressPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeNameUserAnswersEntry
    : Arbitrary[(ConsigneeNamePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsigneeNamePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeIdentityUserAnswersEntry
    : Arbitrary[(ConsigneeIdentityPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsigneeIdentityPage]
        value <- arbitrary[ConsigneeIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeEORIUserAnswersEntry
    : Arbitrary[(ConsigneeEORIPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsigneeEORIPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeAddressUserAnswersEntry
    : Arbitrary[(ConsigneeAddressPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsigneeAddressPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsignorNameUserAnswersEntry
    : Arbitrary[(ConsignorNamePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsignorNamePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsignorEORIUserAnswersEntry
    : Arbitrary[(ConsignorEORIPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsignorEORIPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsignorAddressUserAnswersEntry
    : Arbitrary[(ConsignorAddressPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsignorAddressPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeKnownUserAnswersEntry
    : Arbitrary[(ConsigneeKnownPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsigneeKnownPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGoodsItemGrossWeightUserAnswersEntry
    : Arbitrary[(GoodsItemGrossWeightPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[GoodsItemGrossWeightPage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsignorsIdentityUserAnswersEntry
    : Arbitrary[(ConsignorsIdentityPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsignorsIdentityPage]
        value <- arbitrary[ConsignorsIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDangerousGoodCodeUserAnswersEntry
    : Arbitrary[(DangerousGoodCodePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[DangerousGoodCodePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryOverallCrnKnownUserAnswersEntry
    : Arbitrary[(OverallCrnKnownPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[OverallCrnKnownPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryOverallCrnUserAnswersEntry: Arbitrary[(OverallCrnPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[OverallCrnPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGoodsItemCrnKnownUserAnswersEntry
    : Arbitrary[(GoodsItemCrnKnownPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[GoodsItemCrnKnownPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGoodsItemCrnUserAnswersEntry: Arbitrary[(GoodsItemCrnPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[GoodsItemCrnPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDangerousGoodUserAnswersEntry
    : Arbitrary[(DangerousGoodPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[DangerousGoodPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDocumentUserAnswersEntry: Arbitrary[(DocumentPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[DocumentPage]
        value <- arbitrary[Document].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddAnyDocumentsUserAnswersEntry
    : Arbitrary[(AddAnyDocumentsPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[AddAnyDocumentsPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNumberOfPiecesUserAnswersEntry
    : Arbitrary[(NumberOfPiecesPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NumberOfPiecesPage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNumberOfPackagesUserAnswersEntry
    : Arbitrary[(NumberOfPackagesPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NumberOfPackagesPage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMarkOrNumberUserAnswersEntry: Arbitrary[(MarkOrNumberPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[MarkOrNumberPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryKindOfPackageUserAnswersEntry
    : Arbitrary[(KindOfPackagePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[KindOfPackagePage]
        value <- arbitrary[KindOfPackage].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddMarkOrNumberUserAnswersEntry
    : Arbitrary[(AddMarkOrNumberPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[AddMarkOrNumberPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDeclarationPlaceUserAnswersEntry
    : Arbitrary[(DeclarationPlacePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[DeclarationPlacePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGoodsDescriptionUserAnswersEntry
    : Arbitrary[(GoodsDescriptionPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[GoodsDescriptionPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCommodityCodeKnownUserAnswersEntry
    : Arbitrary[(CommodityCodeKnownPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CommodityCodeKnownPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCommodityCodeUserAnswersEntry
    : Arbitrary[(CommodityCodePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CommodityCodePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRemoveCountryEnRouteUserAnswersEntry
    : Arbitrary[(RemoveCountryEnRoutePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[RemoveCountryEnRoutePage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGoodsPassThroughOtherCountriesUserAnswersEntry
    : Arbitrary[(GoodsPassThroughOtherCountriesPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[GoodsPassThroughOtherCountriesPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCountryEnRouteUserAnswersEntry
    : Arbitrary[(CountryEnRoutePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CountryEnRoutePage]
        value <- arbitrary[Country].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddCountryEnRouteUserAnswersEntry
    : Arbitrary[(AddCountryEnRoutePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[AddCountryEnRoutePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCustomsOfficeOfFirstEntryUserAnswersEntry
    : Arbitrary[(CustomsOfficeOfFirstEntryPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CustomsOfficeOfFirstEntryPage.type]
        value <- arbitrary[CustomsOffice].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCountryOfOriginUserAnswersEntry
    : Arbitrary[(CountryOfOriginPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CountryOfOriginPage.type]
        value <- arbitrary[Country].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryArrivalDateAndTimeUserAnswersEntry
    : Arbitrary[(ArrivalDateAndTimePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ArrivalDateAndTimePage.type]
        value <- arbitrary[ArrivalDateAndTime].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCarriersEORIUserAnswersEntry
    : Arbitrary[(CarriersEORIPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CarriersEORIPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryIdentifyCarrierUserAnswersEntry
    : Arbitrary[(IdentifyCarrierPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[IdentifyCarrierPage.type]
        value <- arbitrary[IdentifyCarrier].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryTransportModeUserAnswersEntry
    : Arbitrary[(TransportModePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[TransportModePage.type]
        value <- arbitrary[TransportMode].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryTotalGrossWeightUserAnswersEntry
    : Arbitrary[(TotalGrossWeightPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[TotalGrossWeightPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGrossWeightUserAnswersEntry
    : Arbitrary[(GrossWeightPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[GrossWeightPage.type]
        value <- arbitrary[GrossWeight].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryLodgingPersonTypeUserAnswersEntry
    : Arbitrary[(LodgingPersonTypePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[LodgingPersonTypePage.type]
        value <- arbitrary[LodgingPersonType].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryLocalReferenceNumberUserAnswersEntry
    : Arbitrary[(LocalReferenceNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[LocalReferenceNumberPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }
}
