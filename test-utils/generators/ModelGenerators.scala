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

import java.time.temporal.ChronoUnit
import java.time.{Instant, LocalDate, LocalTime, ZoneOffset}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import models._
import models.TransportIdentity._
import models.completion.answers.{Parties, Predec, RouteDetails, Transport}
import models.completion.{CustomsOffice => CustomsOfficePayload, _}
import models.completion.downstream._
import pages.CheckAnswersPage
import pages.consignees.{CheckConsigneePage, CheckConsigneesAndNotifiedPartiesPage, CheckNotifiedPartyPage}


trait ModelGenerators extends StringGenerators {

  // Three decimal places, between 0.001 and 99999999.999
  val grossMassGen: Gen[BigDecimal] = Gen.choose(1L, 99999999999L).map { l => BigDecimal(l, 3) }

  implicit lazy val arbitraryCheckAnswersPage: Arbitrary[CheckAnswersPage] =
    Arbitrary {
      Gen.oneOf(
        CheckConsigneePage(Index(0)),
        CheckNotifiedPartyPage(Index(0)),
        CheckConsigneesAndNotifiedPartiesPage
      )
    }

  implicit lazy val arbitraryTraderWithEori: Arbitrary[TraderWithEori] =
    Arbitrary {
      for {
        key <- arbitrary[Int]
        eori <- arbitrary[GbEori]
      } yield TraderWithEori(key, eori.value)
    }

  implicit lazy val arbitraryTraderWithoutEori: Arbitrary[TraderWithoutEori] =
    Arbitrary {
      for {
        key <- arbitrary[Int]
        name <- stringsWithMaxLength(35)
        address <- arbitrary[Address]
      } yield TraderWithoutEori(key, name, address)
    }

  implicit lazy val arbitraryTrader: Arbitrary[Trader] = {
    Arbitrary {
      Gen.oneOf[Trader](arbitrary[TraderWithEori], arbitrary[TraderWithoutEori])
    }
  }

  // The default arbitrary[Instant] provides unrealistic dates prone to overflow issues; pick a
  // value between 2000-01-01 00:00:00 and 2030-01-01 00:00:00 instead. We also truncate to minute
  // precision since we mostly encode datetimes at that precision, and we pick a date after 2000
  // because we sometimes encode year as two digits
  lazy val arbitraryRecentInstant: Arbitrary[Instant] = Arbitrary {
    Gen.choose(1577836800, 1893456000) map { seconds =>
      Instant.EPOCH.plusSeconds(seconds).truncatedTo(ChronoUnit.MINUTES)
    }
  }

  implicit lazy val arbitraryRoroUnaccompaniedIdentity: Arbitrary[RoroUnaccompaniedIdentity] =
    Arbitrary {
      for {
        trailer <- stringsWithMaxLength(12)
        imo <- Gen.listOfN(8, Gen.numChar)
        ferryCompany <- Gen.option(stringsWithMaxLength(12))
      } yield RoroUnaccompaniedIdentity(trailer.mkString, imo.mkString, ferryCompany)
    }

  implicit lazy val arbitraryRoroAccompaniedIdentity: Arbitrary[RoroAccompaniedIdentity] =
    Arbitrary {
      for {
        vehicleReg <- stringsWithMaxLength(12)
        trailer <- stringsWithMaxLength(12)
        ferry <- Gen.option(stringsWithMaxLength(12))
      } yield RoroAccompaniedIdentity(vehicleReg.mkString, trailer.mkString, ferry)
    }

  implicit lazy val arbitraryRoadIdentity: Arbitrary[RoadIdentity] =
    Arbitrary {
      for {
        vehicleReg <- stringsWithMaxLength(12)
        trailer <- stringsWithMaxLength(12)
        ferry <- Gen.option(stringsWithMaxLength(12))
      } yield RoadIdentity(vehicleReg.mkString, trailer.mkString, ferry)
    }

  implicit lazy val arbitraryRailIdentity: Arbitrary[RailIdentity] =
    Arbitrary {
      stringsWithMaxLength(27).map(RailIdentity.apply)
    }

  implicit lazy val arbitraryMaritimeIdentity: Arbitrary[MaritimeIdentity] =
    Arbitrary {
      for {
        imo <- Gen.listOfN(8, Gen.numChar)
        conveyanceRef <- stringsWithMaxLength(35)
      } yield MaritimeIdentity(imo.mkString, conveyanceRef)
    }

  implicit lazy val arbitraryAirIdentity: Arbitrary[AirIdentity] = Arbitrary {
    for {
      carrierCode <- Gen.listOfN(3, Gen.alphaNumChar)
      flightNumber <- Gen.listOfN(4, Gen.numChar)
      suffix <- Gen.option(Gen.alphaChar)
    } yield {
      AirIdentity((carrierCode ++ flightNumber ++ suffix).mkString)
    }
  }

  implicit lazy val arbitraryTransportIdentity: Arbitrary[TransportIdentity] = Arbitrary {
    Gen.oneOf(
      arbitrary[AirIdentity],
      arbitrary[MaritimeIdentity],
      arbitrary[RailIdentity],
      arbitrary[RoadIdentity],
      arbitrary[RoroAccompaniedIdentity],
      arbitrary[RoroUnaccompaniedIdentity]
    )
  }

  implicit lazy val arbitraryPlaceOfUnloading: Arbitrary[PlaceOfUnloading] =
    Arbitrary {
      for {
        id <- Gen.choose(1, 100)
        country <- arbitrary[Country]
        place <- stringsWithMaxLength(35)
      } yield PlaceOfUnloading(id, country, place)
    }

  implicit lazy val arbitraryPlaceOfLoading: Arbitrary[PlaceOfLoading] =
    Arbitrary {
      for {
        id <- Gen.choose(1, 100)
        country <- arbitrary[Country]
        place <- stringsWithMaxLength(35)
      } yield PlaceOfLoading(id, country, place)
    }

  implicit lazy val arbitraryPaymentMethod: Arbitrary[PaymentMethod] =
    Arbitrary {
      Gen.oneOf(PaymentMethod.values)
    }

  implicit lazy val arbitraryConsigneeIdentity: Arbitrary[TraderIdentity] =
    Arbitrary {
      Gen.oneOf(TraderIdentity.values)
    }

  val allDangerousGoods = Seq(
    DangerousGood("4", "AMMONIUM PICRATE dry or wetted with less than 10% water, by mass"),
    DangerousGood("5", "CARTRIDGES FOR WEAPONS with bursting charge"),
    DangerousGood("6", "CARTRIDGES FOR WEAPONS with bursting charge"),
    DangerousGood("7", "CARTRIDGES FOR WEAPONS with bursting charge")
  )

  implicit lazy val arbitraryCustomsOfficePayload: Arbitrary[CustomsOfficePayload] =
    Arbitrary {
      for {
        office <- arbitrary[CustomsOffice]
        datetime <- arbitrary[Instant](arbitraryRecentInstant)
      } yield CustomsOfficePayload(office.code, datetime)
    }

  implicit lazy val arbitrarysOffice: Arbitrary[CustomsOffice] =
    Arbitrary {
      Gen.oneOf(CustomsOffice.allCustomsOffices)
    }

  implicit lazy val arbitraryDocument: Arbitrary[Document] =
    Arbitrary {
      for {
        documentType <- arbitrary[DocumentType]
        reference <- stringsWithMaxLength(35)
      } yield Document(documentType, reference)
    }

  implicit lazy val arbitraryContainer: Arbitrary[Container] =
    Arbitrary {
      stringsWithMaxLength(17).map(Container.apply)
    }

  implicit lazy val arbitraryGoodItem: Arbitrary[GoodsItemName] =
    Arbitrary {
      Gen.listOfN(8, Gen.numChar) map { num => GoodsItemName(num.mkString) }
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

  implicit lazy val arbitraryItinerary: Arbitrary[Itinerary] =
    Arbitrary {
      for {
        len <- Gen.choose(1, 5)
        countries <- Gen.listOfN(len, arbitrary[Country])
      } yield Itinerary(countries)
    }

  implicit lazy val arbitraryDangerousGood: Arbitrary[DangerousGood] =
    Arbitrary {
      Gen.oneOf(allDangerousGoods)
    }

  implicit lazy val arbitraryAddress: Arbitrary[Address] =
    Arbitrary {
      for {
        streetAndNumber <- stringsWithMaxLength(35)
        city <- stringsWithMaxLength(35)
        postCode <- stringsWithMaxLength(9)
        country <- arbitrary[Country]
      } yield Address(streetAndNumber, city, postCode, country)
    }

  implicit lazy val arbitraryPredec: Arbitrary[Predec] =
    Arbitrary {
      for {
        lrn <- arbitrary[LocalReferenceNumber]
        location <- stringsWithMaxLength(9)
        carrier <- Gen.option(arbitrary[Party])
        totalMass <- Gen.option(grossMassGen)
      } yield {
        Predec(lrn, location, carrier, totalMass)
      }
    }

  implicit lazy val arbitraryTransportAnswers: Arbitrary[Transport] = Arbitrary {
    for {
      id <- arbitrary[TransportIdentity]
      mode <- arbitrary[TransportMode]
      nationality <- Gen.option(arbitrary[Country])
      docsLen <- Gen.choose(1, 3)
      docs <- Gen.listOfN(docsLen, arbitrary[Document])
      sealsLen <- Gen.choose(1, 3)
      seals <- Gen.listOfN(sealsLen, stringsWithMaxLength(20))
    } yield Transport(id, mode, nationality, docs, seals)
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

  // MRNs fit the regex [0-9]{2}[A-Z]{2}[A-Z0-9]{13}[0-9]
  implicit lazy val arbitraryMovementReferenceNumber: Arbitrary[MovementReferenceNumber] = {
    Arbitrary {
      for {
        twoDigits <- Gen.listOfN(2, Gen.numChar)
        thirteenAlphaNum <- Gen.listOfN(13, Gen.alphaNumChar)
        oneDigit <- Gen.numChar
      } yield MovementReferenceNumber(
        s"${twoDigits.mkString}GB${thirteenAlphaNum.mkString.toUpperCase}$oneDigit"
      )
    }
  }

  implicit lazy val arbitraryLocalReferenceNumber: Arbitrary[LocalReferenceNumber] =
    Arbitrary {
      stringsWithMaxLength(22).map(LocalReferenceNumber.apply)
    }

  implicit lazy val arbitraryTransportMode: Arbitrary[TransportMode] =
    Arbitrary {
      Gen.oneOf(TransportMode.values)
    }

  implicit lazy val arbitraryGrossWeight: Arbitrary[ProvideGrossWeight] =
    Arbitrary {
      Gen.oneOf(ProvideGrossWeight.values)
    }

  implicit lazy val arbitraryLodgingPersonType: Arbitrary[LodgingPersonType] =
    Arbitrary {
      Gen.oneOf(LodgingPersonType.values)
    }

  implicit lazy val arbitraryGbEori: Arbitrary[GbEori] =
    Arbitrary {
      Gen.listOfN(12, Gen.numChar).map { content: List[Char] => new GbEori(content.mkString) }
    }

  implicit lazy val arbitraryTransportDetails: Arbitrary[TransportDetails] = Arbitrary {
    for {
      mode <- arbitrary[TransportMode]
      identifier <- stringsWithMaxLength(27)
      nationality <- Gen.option(arbitrary[Country])
    } yield TransportDetails(mode, identifier, nationality)
  }

  implicit lazy val arbitrarySubmissionTimePlace: Arbitrary[SubmissionTimePlace] = Arbitrary {
    for {
      datetime <- arbitrary[Instant](arbitraryRecentInstant)
      declarationPlace <- stringsWithMaxLength(35)
    } yield {
      SubmissionTimePlace(
        declarationPlace,
        datetime
      )
    }
  }

  implicit lazy val arbitraryAmendmentTimePlace: Arbitrary[AmendmentTimePlace] = Arbitrary {
    for {
      datetime <- arbitrary[Instant](arbitraryRecentInstant)
      declarationPlace <- stringsWithMaxLength(35)
    } yield {
      AmendmentTimePlace(
        declarationPlace,
        datetime
      )
    }
  }

  implicit lazy val arbitraryPayloadHeader: Arbitrary[Header] = Arbitrary {
    for {
      lrn <- arbitrary[LocalReferenceNumber]
      transportDetails <- arbitrary[TransportDetails]
      itemCount <- Gen.choose(1, 3)
      packageCount <- Gen.choose(1, 3)
      grossMass <- Gen.option(grossMassGen)
      conveyanceReferenceNumber <- stringsWithMaxLength(35)
      timePlace <- arbitrary[SubmissionTimePlace]
    } yield {
      Header(
        lrn,
        transportDetails,
        itemCount,
        packageCount,
        grossMass,
        conveyanceReferenceNumber,
        timePlace
      )
    }
  }

  implicit lazy val arbitraryGoodsIdByCommCode: Arbitrary[GoodsItemIdentity.ByCommodityCode] = Arbitrary {
    for {
      len <- Gen.choose(4, 8)
      code <- Gen.listOfN(len, Gen.numChar)
    } yield GoodsItemIdentity.ByCommodityCode(code.mkString)
  }

  implicit lazy val arbitraryGoodsIdDesc: Arbitrary[GoodsItemIdentity.WithDescription] = Arbitrary {
    stringsWithMaxLength(200).map(GoodsItemIdentity.WithDescription)
  }

  implicit lazy val arbitraryGoodsId: Arbitrary[GoodsItemIdentity] = Arbitrary {
    Gen.oneOf(
      arbitrary[GoodsItemIdentity.ByCommodityCode],
      arbitrary[GoodsItemIdentity.WithDescription]
    )
  }

  implicit lazy val arbitraryMessageSender: Arbitrary[MessageSender] = Arbitrary {
    for {
      eori <- arbitrary[GbEori]
      branch <- Gen.listOfN(10, Gen.numChar)
    } yield MessageSender("GB" + eori.value, branch.mkString)
  }

  implicit lazy val arbitraryMessageType: Arbitrary[MessageType] = Arbitrary {
    Gen.oneOf(MessageType.Submission, MessageType.Amendment)
  }

  // Represents a newly submitted declaration, with submission-specific values
  protected val submissionGen: Gen[Declaration] = {
    for {
      metadata <- arbitrary[Metadata]
      header <- arbitrary[Header]
      lrn <- arbitrary[LocalReferenceNumber]
      timePlace <- arbitrary[SubmissionTimePlace]
      goodsItemsLen <- Gen.choose(1, 5)
      goodsItems <- Gen.listOfN(goodsItemsLen, arbitrary[GoodsItem])
      itinerary <- arbitrary[Itinerary]
      declarer <- arbitrary[Party.ByEori]
      sealsLen <- Gen.choose(1, 5)
      seals <- Gen.listOfN(sealsLen, stringsWithMaxLength(20))
      customsOffice <- arbitrary[CustomsOfficePayload]
      carrier <- Gen.option(arbitrary[Party])
      consignor <- Gen.option(arbitrary[Party])
    } yield {
      Declaration(
        metadata.copy(messageType = MessageType.Submission),
        header.copy(ref = lrn, timePlace = timePlace),
        goodsItems.zipWithIndex.map { case (item, i) => item.copy(itemNumber = i + 1) },
        itinerary,
        declarer,
        seals,
        customsOffice,
        carrier,
        consignor
      )
    }
  }

  // Represents an amendment to a declaration, with amendment-specific values
  protected val amendmentGen: Gen[Declaration] = {
    for {
      dec <- submissionGen
      mrn <- arbitrary[MovementReferenceNumber]
      timePlace <- arbitrary[AmendmentTimePlace]
    } yield {
      dec.copy(
        metadata = dec.metadata.copy(messageType = MessageType.Amendment),
        header = dec.header.copy(ref = mrn, timePlace = timePlace)
      )
    }
  }

  implicit lazy val arbitraryDeclaration = Arbitrary(Gen.oneOf(submissionGen, amendmentGen))

  implicit lazy val arbitraryMetadata: Arbitrary[Metadata] = Arbitrary {
    for {
      messageId <- stringsWithExactLength(14)
      messageSender <- arbitrary[MessageSender]
      messageType <- arbitrary[MessageType]
      dt <- arbitrary[Instant](arbitraryRecentInstant)
    } yield Metadata(messageId.mkString, messageSender, messageType, dt)
  }

  implicit lazy val arbitraryDangerousGoodsCode: Arbitrary[DangerousGoodsCode] = Arbitrary {
    arbitrary[DangerousGood].map { good => DangerousGoodsCode(good.code) }
  }

  implicit lazy val arbitraryLoadingPlace: Arbitrary[LoadingPlace] = Arbitrary {
    for {
      country <- arbitrary[Country]
      desc <- stringsWithMaxLength(32)
    } yield LoadingPlace(country, desc)
  }

  implicit lazy val arbitraryPartyByEori: Arbitrary[Party.ByEori] = Arbitrary {
    arbitrary[GbEori].map(Party.ByEori)
  }

  implicit lazy val arbitraryPartyByAddress: Arbitrary[Party.ByAddress] = Arbitrary {
    for {
      name <- stringsWithMaxLength(35)
      addr <- arbitrary[Address]
    } yield Party.ByAddress(name, addr)
  }

  implicit lazy val arbitraryParty: Arbitrary[Party] = Arbitrary {
    Gen.oneOf(arbitrary[Party.ByEori], arbitrary[Party.ByAddress])
  }

  implicit lazy val arbitrarySpecialMention = Arbitrary {
    Gen.listOfN(5, Gen.numChar) map { v => SpecialMention(v.mkString) }
  }

  implicit lazy val arbitraryPackage = Arbitrary {
    for {
      kindPackage <- arbitrary[KindOfPackage]
      numPackages <- Gen.option(Gen.choose(0, 99999))
      numPieces <- Gen.option(Gen.choose(0, 99999))
      mark <- Gen.option(Gen.listOfN(2, Gen.alphaChar) map { _.mkString })
    } yield Package(kindPackage, numPackages, numPieces, mark)
  }

  implicit lazy val arbitraryBulkPackageItem: Arbitrary[BulkPackageItem] = Arbitrary{
    for {
      kindPackage <- arbitrary[KindOfPackage]
      mark <- Gen.option(Gen.listOfN(2, Gen.alphaChar) map { _.mkString })
    } yield BulkPackageItem(kindPackage, mark)
  }

  implicit lazy val arbitraryUnpackedPackageItem: Arbitrary[UnpackedPackageItem] = Arbitrary {
    for {
      kindPackage <- arbitrary[KindOfPackage]
      numPieces <- Gen.choose(0, 99999)
      mark <- Gen.option(Gen.listOfN(2, Gen.alphaChar) map { _.mkString })
    } yield UnpackedPackageItem(kindPackage, numPieces, mark)
  }

  implicit lazy val arbitraryStandardPackageItem: Arbitrary[StandardPackageItem] = Arbitrary {
    for {
      kindPackage <- arbitrary[KindOfPackage]
      numPieces <- Gen.choose(0, 99999)
      mark <- Gen.listOfN(2, Gen.alphaChar) map { _.mkString }
    } yield StandardPackageItem(kindPackage, numPieces, mark)
  }

  implicit lazy val arbitraryPackageItem: Arbitrary[PackageItem] = Arbitrary {
    Gen.oneOf(arbitrary[BulkPackageItem], arbitrary[UnpackedPackageItem], arbitrary[StandardPackageItem])
  }

  implicit lazy val arbitraryRouteDetails: Arbitrary[RouteDetails] = Arbitrary {
    val genPlaces: Gen[Map[Int,LoadingPlace]] = for {
      length <- Gen.choose(1, 3)
      places <- Gen.listOfN(length, arbitrary[LoadingPlace])
      placesByIndex <- places.zipWithIndex.map { case (a, b) => (b, a) }.toMap
    } yield placesByIndex

    for {
      loadingPlaces <- genPlaces
      unloadingPlaces <- genPlaces
      originCountry <- arbitrary[Country]
      extraCountries <- Gen.choose(1, 3).flatMap { len => Gen.listOfN(len, arbitrary[Country]) }
      customsOffice <- arbitrary[CustomsOfficePayload]
    } yield {
      val itinerary = Itinerary(originCountry +: extraCountries)
      RouteDetails(loadingPlaces, unloadingPlaces, itinerary, customsOffice)
    }
  }

  implicit lazy val arbitraryParties: Arbitrary[Parties] = Arbitrary {
    val genParties: Gen[Map[Int, Party]] =
      for {
        count <- Gen.choose(1, 3)
        partyList <- Gen.listOfN(count, arbitrary[Party])
      } yield partyList.zipWithIndex.map { case (p, i) => i -> p }.toMap

    for {
      consignors <- genParties
      consignees <- genParties
      notifiedParties <- genParties
    } yield Parties(consignors, consignees, notifiedParties)
  }

  implicit lazy val arbitraryGoodsItem: Arbitrary[GoodsItem] = Arbitrary {
    for {
      itemNumber <- Gen.choose(1, 10)
      itemId <- arbitrary[GoodsItemIdentity]
      grossMass <- Gen.option(grossMassGen)
      paymentMethod <- arbitrary[PaymentMethod]
      dangerousGoodsCode <- Gen.option(arbitrary[DangerousGoodsCode])
      placeOfLoading <- arbitrary[LoadingPlace]
      placeOfUnloading <- arbitrary[LoadingPlace]
      documentsLen <- Gen.choose(0, 3)
      documents <- Gen.listOfN(documentsLen, arbitrary[Document])
      consignor <- Gen.option(arbitrary[Party])
      consignee <- Gen.option(arbitrary[Party])
      notifiedParty <- Gen.option(arbitrary[Party])
      containersLen <- Gen.choose(0, 3)
      containers <- Gen.listOfN(containersLen, arbitrary[Container])
      packagesLen <- Gen.choose(0, 3)
      packages <- Gen.listOfN(packagesLen, arbitrary[Package])
      specialMentionsLen <- Gen.choose(0, 1)
      specialMentions <- Gen.listOfN(specialMentionsLen, arbitrary[SpecialMention])
    } yield GoodsItem(
      itemNumber,
      itemId,
      grossMass,
      paymentMethod,
      dangerousGoodsCode,
      placeOfLoading,
      placeOfUnloading,
      documents,
      consignor,
      consignee,
      notifiedParty,
      containers,
      packages,
      specialMentions
    )
  }
}
