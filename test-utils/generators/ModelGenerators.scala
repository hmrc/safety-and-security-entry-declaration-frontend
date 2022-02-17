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
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

import java.time.{Instant, LocalDate, LocalTime, ZoneOffset}

trait ModelGenerators {
  val allDangerousGoods = Seq(
    DangerousGood("4", "AMMONIUM PICRATE dry or wetted with less than 10% water, by mass"),
    DangerousGood("5", "CARTRIDGES FOR WEAPONS with bursting charge"),
    DangerousGood("6", "CARTRIDGES FOR WEAPONS with bursting charge"),
    DangerousGood("7", "CARTRIDGES FOR WEAPONS with bursting charge"))

  implicit lazy val arbitraryCustomsOffice: Arbitrary[CustomsOffice]=
    Arbitrary {
      Gen.oneOf(CustomsOffice.allCustomsOffices)
    }

  implicit lazy val arbitraryConsignorsIdentity: Arbitrary[ConsignorsIdentity] =
    Arbitrary {
      Gen.oneOf(ConsignorsIdentity.values)
    }

  implicit lazy val arbitraryDocument: Arbitrary[Document] =
    Arbitrary {
      for {
        documentType <- arbitrary[DocumentType]
        reference    <- arbitrary[String]
      } yield Document(documentType, reference)
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

  implicit lazy val arbitraryArrivalDateAndTime: Arbitrary[ArrivalDateAndTime] =
    Arbitrary {
      for {
        hour      <- Gen.choose(0, 23)
        minute    <- Gen.choose(0, 59)
        earlyDate = LocalDate.now.atStartOfDay().atZone(ZoneOffset.UTC).toInstant.toEpochMilli
        lateDate  = LocalDate.now.plusYears(100).atStartOfDay().atZone(ZoneOffset.UTC).toInstant.toEpochMilli
        date      <- Gen.choose(earlyDate, lateDate).map(m => Instant.ofEpochMilli(m).atOffset(ZoneOffset.UTC).toLocalDate)
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
        chars  <- Gen.listOfN(length, Gen.alphaNumChar)
      } yield LocalReferenceNumber(chars.mkString)
    }

  implicit lazy val arbitraryTransportMode: Arbitrary[TransportMode] =
    Arbitrary {
      Gen.oneOf(TransportMode.values.toSeq)
    }

  implicit lazy val arbitraryGrossWeight: Arbitrary[GrossWeight] =
    Arbitrary {
      Gen.oneOf(GrossWeight.values.toSeq)
    }

  implicit lazy val arbitraryLodgingPersonType: Arbitrary[LodgingPersonType] =
    Arbitrary {
      Gen.oneOf(LodgingPersonType.values.toSeq)
    }
}
