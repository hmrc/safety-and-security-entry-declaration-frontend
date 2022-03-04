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

import models.LocalReferenceNumber
import models.completion.downstream.{Header, TransportDetails}
import serialisation.xml.XmlImplicits._

trait HeaderFormats extends TransportFormats with TimeFormats {
  private implicit val datetimeFmt = TimeFormats.HeaderFormat

  implicit val headerFmt = new Format[Header] {
    override def encode(header: Header): NodeSeq = {
      val grossMass: NodeSeq = {
        header.grossMass.map { v => <TotGroMasHEA307>{v.toXmlString}</TotGroMasHEA307> }.toSeq
      }

      <HEAHEA>
        <RefNumHEA4>{header.lrn.toXmlString}</RefNumHEA4>
        {header.transportDetails.toXml}
        <TotNumOfIteHEA305>{header.itemCount}</TotNumOfIteHEA305>
        <TotNumOfPacHEA306>{header.packageCount}</TotNumOfPacHEA306>
        {grossMass}
        <DecPlaHEA394>{header.declarationPlace}</DecPlaHEA394>
        <ConRefNumHEA>{header.conveyanceReferenceNumber}</ConRefNumHEA>
        <DecDatTimHEA114>{header.datetime.toXmlString}</DecDatTimHEA114>
      </HEAHEA>
    }

    override def decode(data: NodeSeq): Header = Header(
      lrn = (data \ "RefNumHEA4").text.parseXmlString[LocalReferenceNumber],
      transportDetails = data.parseXml[TransportDetails],
      itemCount = (data \ "TotNumOfIteHEA305").text.toInt,
      packageCount = (data \ "TotNumOfPacHEA306").text.toInt,
      grossMass = (data \ "TotGroMasHEA307").headOption map { _.text.parseXmlString[BigDecimal] },
      declarationPlace = (data \ "DecPlaHEA394").text,
      conveyanceReferenceNumber = (data \ "ConRefNumHEA").text,
      datetime = (data \ "DecDatTimHEA114").text.parseXmlString[Instant]
    )
  }
}

object HeaderFormats extends HeaderFormats
