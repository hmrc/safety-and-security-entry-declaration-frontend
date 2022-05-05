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

import java.time.{Clock, Instant, ZoneId}

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

import base.SpecBase
import models.{CustomsOffice => _, _}
import models.TransportIdentity._
import models.completion._
import models.completion.answers.{Declaration => DeclarationAnswers, GoodsItem => GoodsItemAnswer, _}
import models.completion.downstream.{Declaration => XmlDeclaration, GoodsItem => XmlGoodsItem, _}

class DeclarationConverterSpec extends SpecBase {
  private val now = Instant.now
  private val clock = Clock.fixed(now, ZoneId.systemDefault)
  private val declarer = arbitrary[GbEori].sample.value

  private val totalMass = BigDecimal.exact("100")

  private val itinerary = arbitrary[Itinerary].sample.value
  private val customsOffice = CustomsOffice("CUSOFF", arbitraryInstant)
  private val dangerousGoodsCode = arbitrary[DangerousGoodsCode].sample.value
  private val paymentMethod = arbitrary[PaymentMethod].sample.value

  private val docs = Gen.listOfN(3, arbitrary[Document]).sample.value
  private val seals = Gen.listOfN(3, stringsWithMaxLength(14)).sample.value
  private val containers = Gen.listOfN(3, arbitrary[Container]).sample.value

  private val bulkPackage = Package(KindOfPackage.bulkKindsOfPackage.head, None, None, None)
  private val packedPackage = {
    Package(KindOfPackage.unpackedKindsOfPackage.head, Some(10), None, None)
  }
  private val unpackedPackage = {
    Package(KindOfPackage.standardKindsOfPackages.head, None, Some(100), None)
  }
  private val mixedPackages = List(bulkPackage, packedPackage, unpackedPackage)

  private val consignor1 = arbitrary[Party.ByEori].sample.value
  private val consignor2 = arbitrary[Party.ByEori].suchThat { _ != consignor1 }.sample.value
  private val consignee = arbitrary[Party.ByEori].sample.value
  private val notifiedParty = arbitrary[Party.ByEori].sample.value

  private val loadingPlace = LoadingPlace(arbitrary[Country].sample.value, "loading")
  private val unloadingPlace = LoadingPlace(arbitrary[Country].sample.value, "unloading")

  private val airIdentity = AirIdentity("AAA0000A")
  private val maritimeIdentity = MaritimeIdentity("imo", "conveyance-ref")
  private val railIdentity = RailIdentity("wagon-number")
  private val roadIdentity = RoadIdentity("LORRYREG", "TRAILERREG", Some("ferry company"))
  private val roroAccIdentity = {
    RoroAccompaniedIdentity("LORRYREG", "TRAILERREG", Some("ferry company"))
  }
  private val roroUnaccIdentity = {
    RoroUnaccompaniedIdentity("TRAILERREG", "imo", Some("ferry company"))
  }

  private val item1 = GoodsItemAnswer(
    GoodsItemIdentity.ByCommodityCode("000000"),
    consignor1,
    Some(consignee),
    None,
    loadingPlace,
    unloadingPlace,
    containers,
    mixedPackages,
    None,
    docs,
    Some(dangerousGoodsCode),
    paymentMethod
  )

  private val item1Payload = XmlGoodsItem(
    1,
    item1.id,
    None,
    paymentMethod,
    Some(dangerousGoodsCode),
    loadingPlace,
    unloadingPlace,
    docs,
    None,
    Some(consignee),
    None,
    containers,
    mixedPackages,
    Nil
  )

  private val answers = DeclarationAnswers(
    Predec(lrn, "location", carrier = None, totalMass = Some(totalMass)),
    Transport(railIdentity, TransportMode.Rail, None, docs, seals),
    RouteDetails(Map.empty, Map.empty, itinerary, customsOffice),
    List(item1, item1)
  )

  private val payload = XmlDeclaration(
    Metadata(
      "id",
      MessageSender(s"GB${declarer.value}", "0000000000"),
      MessageType.Submission,
      now
    ),
    Header(
      lrn,
      TransportDetails(TransportMode.Rail, Some("wagon-number"), None, None),
      2,
      222,
      Some(totalMass),
      SubmissionTimePlace("location", now)
    ),
    List(item1Payload, item1Payload.copy(itemNumber = 2)),
    itinerary,
    Party.ByEori(declarer),
    seals,
    customsOffice,
    None,
    Some(consignor1)
  )

  private val converter = new DeclarationConverter(clock)

  /**
   * Assert that the actual XML declaration is equal to expected, fixing the UUID
   *
   * Since we can't predict the UUID we fix the expected value to the actual one and compare
   * everything else as normal
   */
  private def mustBeEqual(actual: XmlDeclaration, expected: XmlDeclaration): Unit = {
    actual must be(expected.copy(expected.metadata.copy(messageId = actual.metadata.messageId)))
  }

  "The declaration converter" - {
    "should convert declaration answers into an XML payload" in {
      val actual = converter.convert(declarer, lrn, answers)

      mustBeEqual(actual, payload)
    }

    "the transport details section" - {
      "should represent an air identity correctly" in {
        val ans = answers.copy(
          transportDetails = answers.transportDetails.copy(
            mode = TransportMode.Air,
            identity = airIdentity
          )
        )
        val expected = payload.copy(
          header = payload.header.copy(
            transportDetails = payload.header.transportDetails.copy(
              mode = TransportMode.Air,
              identity = None,
              conveyanceReferenceNumber = Some("AAA0000A")
            )
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }

      "should represent a maritime identity correctly" in {
        val ans = answers.copy(
          transportDetails = answers.transportDetails.copy(
            mode = TransportMode.Maritime,
            identity = maritimeIdentity
          )
        )
        val expected = payload.copy(
          header = payload.header.copy(
            transportDetails = payload.header.transportDetails.copy(
              mode = TransportMode.Maritime,
              identity = Some("imo"),
              conveyanceReferenceNumber = Some("conveyance-ref")
            )
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }

      "should represent a rail identity correctly" in {
        val ans = answers.copy(
          transportDetails = answers.transportDetails.copy(
            mode = TransportMode.Rail,
            identity = railIdentity
          )
        )
        val expected = payload.copy(
          header = payload.header.copy(
            transportDetails = payload.header.transportDetails.copy(
              mode = TransportMode.Rail,
              identity = Some("wagon-number"),
              conveyanceReferenceNumber = None
            )
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }

      "should represent a road identity correctly" in {
        val ans = answers.copy(
          transportDetails = answers.transportDetails.copy(
            mode = TransportMode.Road,
            identity = roadIdentity
          )
        )
        val expected = payload.copy(
          header = payload.header.copy(
            transportDetails = payload.header.transportDetails.copy(
              mode = TransportMode.Road,
              identity = Some("LORRYREG TRAILERREG"),
              conveyanceReferenceNumber = Some("ferry company")
            )
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }

      "should represent a roro accompanied identity correctly" in {
        val ans = answers.copy(
          transportDetails = answers.transportDetails.copy(
            mode = TransportMode.RoroAccompanied,
            identity = roroAccIdentity
          )
        )
        val expected = payload.copy(
          header = payload.header.copy(
            transportDetails = payload.header.transportDetails.copy(
              mode = TransportMode.RoroAccompanied,
              identity = Some("LORRYREG TRAILERREG"),
              conveyanceReferenceNumber = Some("ferry company")
            )
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }

      "should represent a roro unaccompanied identity correctly" in {
        val ans = answers.copy(
          transportDetails = answers.transportDetails.copy(
            mode = TransportMode.RoroUnaccompanied,
            identity = roroUnaccIdentity
          )
        )
        val expected = payload.copy(
          header = payload.header.copy(
            transportDetails = payload.header.transportDetails.copy(
              mode = TransportMode.RoroUnaccompanied,
              identity = Some("imo"),
              conveyanceReferenceNumber = Some("ferry company")
            )
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }
    }

    "the package count" - {
      "should count packages correctly for a mixture of package types" in {
        val ans = answers.copy(items = List(item1, item1))
        val expected = payload.copy(
          header = payload.header.copy(itemCount = 2, packageCount = 222),
          goodsItems = List(item1Payload, item1Payload.copy(itemNumber = 2))
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }
    }

    "the top-level consignor" - {
      "should be present if the consignor is the same for all items" in {
        val ans = answers.copy(
          items = List.fill(2)(item1.copy(consignor = consignor1))
        )
        val expected = payload.copy(
          consignor = Some(consignor1),
          goodsItems = List(
            item1Payload.copy(consignor = None),
            item1Payload.copy(itemNumber = 2, consignor = None)
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }

      "should be absent if multiple consignors apply" in {
        val ans = answers.copy(
          items = List(item1.copy(consignor = consignor1), item1.copy(consignor = consignor2))
        )
        val expected = payload.copy(
          consignor = None,
          goodsItems = List(
            item1Payload.copy(consignor = Some(consignor1)),
            item1Payload.copy(itemNumber = 2, consignor = Some(consignor2))
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }
    }

    "an item's special mentions" - {
      "should provide code 10600 (negotiable bill of lading) if notified party is used" in {
        val ans = answers.copy(
          items = List(item1.copy(consignee = None, notifiedParty = Some(notifiedParty)))
        )
        val expected = payload.copy(
          header = payload.header.copy(itemCount = 1, packageCount = 111),
          goodsItems = List(
            item1Payload.copy(
              consignee = None,
              notifiedParty = Some(notifiedParty),
              specialMentions = List(SpecialMention.HasNegotiableBillOfLading)
            )
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }

      "should be absent if there is no notified party" in {
        val ans = answers.copy(
          items = List(item1.copy(consignee = Some(consignee), notifiedParty = None))
        )
        val expected = payload.copy(
          header = payload.header.copy(itemCount = 1, packageCount = 111),
          goodsItems = List(
            item1Payload.copy(
              consignee = Some(consignee),
              notifiedParty = None,
              specialMentions = Nil
            )
          )
        )

        val actual = converter.convert(declarer, lrn, ans)

        mustBeEqual(actual, expected)
      }
    }
  }
}
