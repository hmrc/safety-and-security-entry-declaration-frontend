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

import models.completion.{CustomsOffice, Itinerary}
import models.completion.downstream.{GoodsItem, Header, Metadata, Submission}

import scala.xml.NodeSeq
import serialisation.xml.XmlImplicits._

trait SubmissionFormats
  extends CustomsOfficeFormats
  with GoodsItemFormats
  with HeaderFormats
  with ItineraryFormats
  with MetadataFormats {

  implicit val SubmissionFmt = new Format[Submission] {

    override def encode(sub: Submission): NodeSeq = {

      val goodsItems: NodeSeq = sub.goodsItems map {
        gi => <GOOITEGDS>{gi.toXml}</GOOITEGDS>
      }

      val seals: NodeSeq = sub.seals map {
        s => <SEAID529><SeaIdSEAID530>{s}</SeaIdSEAID530></SEAID529>
      }

      val carrier = sub.carrier.map {
        c => <TRACARENT601>{carrierFormat.encode(c)}</TRACARENT601>
      }.toSeq

      val consignor = sub.consignor.map {
        c => <TRACONCO1>{rootConsignorFormat.encode(c)}</TRACONCO1>
      }.toSeq

      <ie:CC315A xmlns:ie="http://ics.dgtaxud.ec/CC315A">
        {sub.metadata.toXml}
        {sub.header.toXml}
        {consignor}
        {goodsItems}
        {sub.itinerary.toXml}
        <PERLODSUMDEC>{lodgingPersonFormat.encode(sub.declarer)}</PERLODSUMDEC>
        {seals}
        <CUSOFFFENT730>{sub.customsOffice.toXml}</CUSOFFFENT730>
        {carrier}
      </ie:CC315A>
    }

    override def decode(data: NodeSeq): Submission = Submission(
      metadata = data.parseXml[Metadata],
      header = (data \ "HEAHEA").parseXml[Header],
      goodsItems = (data \ "GOOITEGDS").map { _.parseXml[GoodsItem] }.toList,
      itinerary = (data \ "ITI").parseXml[Itinerary],
      declarer = lodgingPersonFormat.decode(data \ "PERLODSUMDEC"),
      seals = (data \ "SEAID529" \ "SeaIdSEAID530").map { _.text }.toList,
      customsOffice = (data \ "CUSOFFFENT730").parseXml[CustomsOffice],
      carrier = (data \ "TRACARENT601").headOption.map(carrierFormat.decode),
      consignor = (data \ "TRACONCO1").headOption.map(rootConsignorFormat.decode),
    )
  }
}
