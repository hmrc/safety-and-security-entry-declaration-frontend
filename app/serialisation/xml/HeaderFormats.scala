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

package serialisation.xml

import java.time.Instant

import scala.xml.NodeSeq
import models.{LocalReferenceNumber, MovementReferenceNumber}
import models.completion.downstream._
import serialisation.xml.XmlImplicits._

trait HeaderFormats extends TransportFormats with TimeFormats {
  private implicit val datetimeFmt = TimeFormats.HeaderFormat

  implicit val headerFmt = new Format[Header] {
    override def encode(header: Header): NodeSeq = {
      val grossMass: NodeSeq = {
        header.grossMass.map { v => <TotGroMasHEA307>{v.toXmlString}</TotGroMasHEA307> }.toSeq
      }

      // Unfortunately we can't simply write using the TransportDetails format because we have to
      // position the individual fields in the right places across the header
      val transportNationality: NodeSeq = header.transportDetails.nationality.map { n =>
        <NatOfMeaOfTraCroHEA87>{n.toXmlString}</NatOfMeaOfTraCroHEA87>
      }.toSeq

      val refNum: NodeSeq = header.ref match {
        case lrn: LocalReferenceNumber =>
          <RefNumHEA4>{lrn.toXmlString}</RefNumHEA4>
        case mrn: MovementReferenceNumber =>
          <DocNumHEA5>{mrn.toXmlString}</DocNumHEA5>
      }

      val (place: NodeSeq, datetime: NodeSeq) = header.timePlace match {
        case SubmissionTimePlace(p, dt) =>
          (
            <DecPlaHEA394>{p}</DecPlaHEA394>,
            <DecDatTimHEA114>{dt.toXmlString}</DecDatTimHEA114>
          )
        case AmendmentTimePlace(p, dt) =>
          (
            <AmdPlaHEA598>{p}</AmdPlaHEA598>,
            <DatTimAmeHEA113>{dt.toXmlString}</DatTimAmeHEA113>
          )
      }

      <HEAHEA>
        {refNum}
        <TraModAtBorHEA76>{header.transportDetails.mode.toXmlString}</TraModAtBorHEA76>
        <IdeOfMeaOfTraCroHEA85>{header.transportDetails.identity}</IdeOfMeaOfTraCroHEA85>
        {transportNationality}
        <TotNumOfIteHEA305>{header.itemCount}</TotNumOfIteHEA305>
        <TotNumOfPacHEA306>{header.packageCount}</TotNumOfPacHEA306>
        {grossMass}
        {place}
        <ConRefNumHEA>{header.conveyanceReferenceNumber}</ConRefNumHEA>
        {datetime}
      </HEAHEA>
    }

    override def decode(data: NodeSeq): Header = Header(
      ref = {
        val lrn: Option[LocalReferenceNumber] = {
          (data \ "RefNumHEA4")
            .headOption
            .map { _.text.parseXmlString[LocalReferenceNumber] }
        }
        lazy val mrn: MovementReferenceNumber = {
          (data \ "DocNumHEA5").text.parseXmlString[MovementReferenceNumber]
        }

        lrn.getOrElse(mrn)
      },
      transportDetails = data.parseXml[TransportDetails],
      itemCount = (data \ "TotNumOfIteHEA305").text.toInt,
      packageCount = (data \ "TotNumOfPacHEA306").text.toInt,
      grossMass = (data \ "TotGroMasHEA307").headOption map { _.text.parseXmlString[BigDecimal] },
      conveyanceReferenceNumber = (data \ "ConRefNumHEA").text,
      timePlace = {
        ((data \ "DecPlaHEA394").headOption, (data \ "DecDatTimHEA114").headOption) match {
          case (Some(place), Some(dt)) =>
            SubmissionTimePlace(place.text, dt.text.parseXmlString[Instant])
          case _ =>
            AmendmentTimePlace(
              (data \ "AmdPlaHEA598").text,
              (data \ "DatTimAmeHEA113").text.parseXmlString[Instant]
            )
        }
      }
    )
  }
}

object HeaderFormats extends HeaderFormats
