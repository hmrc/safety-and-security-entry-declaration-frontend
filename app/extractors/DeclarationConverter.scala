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

import java.time.{Clock, Instant}
import java.util.UUID

import models.{GbEori, LocalReferenceNumber}
import models.TransportIdentity._
import models.completion.Party
import models.completion.answers.{Declaration => DeclarationAnswers, Transport => TransportAnswers}
import models.completion.downstream.{Declaration => XmlDeclaration, _}

/**
 * Convert a complete declaration from a collection of modelled answers into the downstream models
 *
 * This bridges the gap between collecting answers into models and converting them into a form
 * readily serialisable into a downstream XML payload
 */
class DeclarationConverter(clock: Clock) {
  private def convertTransportDetails(answers: TransportAnswers): TransportDetails = {
    val (transportIdentity, conveyanceRef) = answers.identity match {
      case AirIdentity(flightNumber) =>
        (None, Some(flightNumber))
      case MaritimeIdentity(imo, conRef) =>
        (Some(imo), Some(conRef))
      case RailIdentity(wagonNum) =>
        (Some(wagonNum), None)
      case RoadIdentity(vehicleReg, trailerReg, ferryCompany) =>
        (Some(s"$vehicleReg $trailerReg"), ferryCompany)
      case RoroAccompaniedIdentity(vehicleReg, trailerReg, ferryCompany) =>
        (Some(s"$vehicleReg $trailerReg"), ferryCompany)
      case RoroUnaccompaniedIdentity(_, imo, ferryCompany) =>
        (Some(imo), ferryCompany)
    }

    TransportDetails(answers.mode, transportIdentity, answers.nationality, conveyanceRef)
  }

  def convert(
    declarer: GbEori,
    lrn: LocalReferenceNumber,
    answers: DeclarationAnswers
  ): XmlDeclaration = {
    val now = Instant.now(clock)
    val messageType = MessageType.Submission // TODO: Support amendments as well

    // TODO: We need to provide a branch code where available as well; for the moment we use
    // the default value, 0000000000
    val messageSender = MessageSender(s"GB${declarer.value}", "0000000000")

    // This is a full count of packages + pieces, with bulk items considered 1 piece
    val packageCount: Int = {
      answers.items
        .flatMap { _.packages }
        .map { p => Math.max(1, (p.numPackages ++ p.numPieces).sum) }
        .sum
    }

    // An overall consignor is set if and only if the consignor is the same for all goods items
    val overallConsignor: Option[Party] = {
      val allConsignors = answers.items.map { _.consignor }.toSet

      if (allConsignors.size == 1) {
        Some(allConsignors.head)
      } else {
        None
      }
    }

    val timePlace = SubmissionTimePlace(answers.predec.location, now)

    val metadata = Metadata(
      messageId = UUID.randomUUID.toString.filterNot { _ == '-' }.take(14),
      messageSender = messageSender,
      messageType = messageType,
      datetime = now
    )

    val header = Header(
      ref = lrn,
      transportDetails = convertTransportDetails(answers.transportDetails),
      itemCount = answers.items.length,
      packageCount = packageCount,
      grossMass = answers.predec.totalMass,
      timePlace = timePlace
    )

    val items = answers.items.zipWithIndex map { case (item, i) =>
      GoodsItem(
        i + 1,
        item.id,
        item.grossMass,
        item.paymentMethod,
        item.dangerousGoodsCode,
        item.placeOfLoading,
        item.placeOfUnloading,
        item.documents,
        if (overallConsignor.isDefined) None else Some(item.consignor),
        item.consignee,
        item.notifiedParty,
        item.containers,
        item.packages,
        if (item.notifiedParty.isDefined) List(SpecialMention.HasNegotiableBillOfLading) else Nil
      )
    }

    XmlDeclaration(
      metadata = metadata,
      header = header,
      goodsItems = items,
      itinerary = answers.routeDetails.itinerary,
      declarer = Party.ByEori(declarer),
      seals = answers.transportDetails.seals,
      customsOffice = answers.routeDetails.customsOffice,
      carrier = answers.predec.carrier,
      consignor = overallConsignor
    )
  }
}
