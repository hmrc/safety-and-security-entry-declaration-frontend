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
import models.completion.downstream._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

import java.time.{Instant, LocalDate, LocalTime, ZoneOffset}

trait ModelGenerators {
  // The default arbitrary[Instant] provides unrealistic dates prone to overflow issues; pick a
  // value between epoch and 2030-01-01 00:00:00 instead
  lazy val arbitraryRecentInstant: Arbitrary[Instant] = Arbitrary {
    Gen.choose(0, 1893456000) map { seconds => Instant.EPOCH.plusSeconds(seconds) }
  }

  implicit lazy val arbitraryPaymentMethod: Arbitrary[PaymentMethod] =
    Arbitrary {
      Gen.oneOf(PaymentMethod.values.toSeq)
    }

  implicit lazy val arbitraryNotifiedPartyIdentity: Arbitrary[NotifiedPartyIdentity] =
    Arbitrary {
      Gen.oneOf(NotifiedPartyIdentity.values.toSeq)
    }

  implicit lazy val arbitraryConsigneeIdentity: Arbitrary[ConsigneeIdentity] =
    Arbitrary {
      Gen.oneOf(ConsigneeIdentity.values.toSeq)
    }
  val allDangerousGoods = Seq(
    DangerousGood("4", "AMMONIUM PICRATE dry or wetted with less than 10% water, by mass"),
    DangerousGood("5", "CARTRIDGES FOR WEAPONS with bursting charge"),
    DangerousGood("6", "CARTRIDGES FOR WEAPONS with bursting charge"),
    DangerousGood("7", "CARTRIDGES FOR WEAPONS with bursting charge")
  )

  implicit lazy val arbitraryCustomsOffice: Arbitrary[CustomsOffice] =
    Arbitrary {
      Gen.oneOf(CustomsOffice.allCustomsOffices)
    }

  implicit lazy val arbitraryConsignorIdentity: Arbitrary[ConsignorIdentity] =
    Arbitrary {
      Gen.oneOf(ConsignorIdentity.values)
    }

  implicit lazy val arbitraryDocument: Arbitrary[Document] =
    Arbitrary {
      for {
        documentType <- arbitrary[DocumentType]
        reference <- arbitrary[String]
      } yield Document(documentType, reference)
    }

  implicit lazy val arbitraryGoodItem: Arbitrary[GoodItem] =
    Arbitrary {
      for {
        commodityCode <- arbitrary[String]
      } yield GoodItem(commodityCode)
    }

  implicit lazy val arbitraryDocumentType: Arbitrary[DocumentType] =
    Arbitrary {
      Gen.oneOf(DocumentType.allDocumentTypes)
    }

  implicit lazy val arbitraryKindOfPackage: Arbitrary[KindOfPackage] =
    Arbitrary {
      Gen.oneOf(KindOfPackage.allKindsOfPackage)
    }

  implicit lazy val arbitraryCountry: Arbitrary[Country] =
    Arbitrary {
      Gen.oneOf(Country.internationalCountries)
    }

  implicit lazy val arbitraryDangerousGood: Arbitrary[DangerousGood] =
    Arbitrary {
      Gen.oneOf(allDangerousGoods)
    }

  implicit lazy val arbitraryAddress: Arbitrary[Address] =
    Arbitrary {
      for {
        streetAndNumber <- arbitrary[String]
        city <- arbitrary[String]
        postCode <- arbitrary[String]
        country <- arbitrary[Country]
      } yield Address(streetAndNumber, city, postCode, country)
    }

  implicit lazy val arbitraryArrivalDateAndTime: Arbitrary[ArrivalDateAndTime] =
    Arbitrary {
      for {
        hour <- Gen.choose(0, 23)
        minute <- Gen.choose(0, 59)
        earlyDate = LocalDate.now.atStartOfDay().atZone(ZoneOffset.UTC).toInstant.toEpochMilli
        lateDate =
          LocalDate.now.plusYears(100).atStartOfDay().atZone(ZoneOffset.UTC).toInstant.toEpochMilli
        date <-
          Gen
            .choose(earlyDate, lateDate)
            .map(m => Instant.ofEpochMilli(m).atOffset(ZoneOffset.UTC).toLocalDate)
      } yield ArrivalDateAndTime(date, LocalTime.of(hour, minute))
    }

  implicit lazy val arbitraryIdentifyCarrier: Arbitrary[IdentifyCarrier] =
    Arbitrary {
      Gen.oneOf(IdentifyCarrier.values.toSeq)
    }

  implicit lazy val arbitraryLocalReferenceNumber: Arbitrary[LocalReferenceNumber] =
    Arbitrary {
      for {
        length <- Gen.choose(1, 22)
        chars <- Gen.listOfN(length, Gen.alphaNumChar)
      } yield LocalReferenceNumber(chars.mkString)
    }

  implicit lazy val arbitraryTransportMode: Arbitrary[TransportMode] =
    Arbitrary {
      Gen.oneOf(TransportMode.values.toSeq)
    }

  implicit lazy val arbitraryGrossWeight: Arbitrary[ProvideGrossWeight] =
    Arbitrary {
      Gen.oneOf(ProvideGrossWeight.values.toSeq)
    }

  implicit lazy val arbitraryLodgingPersonType: Arbitrary[LodgingPersonType] =
    Arbitrary {
      Gen.oneOf(LodgingPersonType.values.toSeq)
    }

  implicit lazy val arbitraryGbEori: Arbitrary[GbEori] =
    Arbitrary {
      Gen.listOfN(12, Gen.numChar).map { content: List[Char] => new GbEori(content.mkString) }
    }

  implicit lazy val arbitraryTransportDetails: Arbitrary[TransportDetails] = Arbitrary {
    for {
      mode <- arbitrary[TransportMode]
      identifier <- Gen.alphaNumStr
      nationality <- Gen.option(arbitrary[Country])
      paymentMethod <- Gen.option(arbitrary[PaymentMethod])
    } yield TransportDetails(mode, identifier, nationality, paymentMethod)
  }

  implicit lazy val arbitraryPayloadHeader: Arbitrary[Header] = Arbitrary {
    for {
      lrn <- arbitrary[LocalReferenceNumber]
      transportDetails <- arbitrary[TransportDetails]
      itemCount <- Gen.choose(1, 3)
      packageCount <- Gen.choose(1, 3)
      grossMass <- Gen.option(Gen.choose(BigDecimal(0.001), BigDecimal(99999999.999)))
      declarationPlace <- Gen.alphaNumStr
      conveyanceReferenceNumber <- Gen.alphaNumStr
      datetime <- arbitrary[Instant](arbitraryRecentInstant)
    } yield {
      Header(
        lrn,
        transportDetails,
        itemCount,
        packageCount,
        grossMass,
        declarationPlace,
        conveyanceReferenceNumber,
        datetime
      )
    }
  }
}
