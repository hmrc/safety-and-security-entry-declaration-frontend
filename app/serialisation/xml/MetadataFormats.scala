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

import java.time.{Instant, ZoneId, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.xml.NodeSeq

import models.completion.downstream.{MessageSender, MessageType, Metadata}
import models.completion.downstream.MessageType._
import serialisation.xml.XmlImplicits._

trait MetadataFormats {
  // N.B. we aren't using the StringFormat in TimeFormats because we have to serialise multiple
  // fields for a separate date + time in the message metadata fields
  implicit val metadataInstantFmt = new Format[Instant] {
    private val dtFmt = DateTimeFormatter.ofPattern("yyyyMMddHHmm").withZone(ZoneId.from(ZoneOffset.UTC))
    private val dateFmt = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.from(ZoneOffset.UTC))
    private val timeFmt = DateTimeFormatter.ofPattern("HHmm").withZone(ZoneId.from(ZoneOffset.UTC))

    override def encode(dt: Instant): NodeSeq = {
      <DatOfPreMES9>{dateFmt.format(dt)}</DatOfPreMES9>
      <TimOfPreMES10>{timeFmt.format(dt)}</TimOfPreMES10>
    }
    override def decode(data: NodeSeq): Instant = {
      val input = (data \\ "DatOfPreMES9").text + (data \\ "TimOfPreMES10").text
      Instant.from(dtFmt.parse(input))
    }
  }

  implicit val messageTypeFmt = new StringFormat[MessageType] {
    override def encode(t: MessageType): String = t match {
      case Submission => "CC315A"
      case Amendment => "CC313A"
    }
    override def decode(s: String): MessageType = s match {
      case "CC315A" => Submission
      case "CC313A" => Amendment
      case v => throw new XmlDecodingException(s"Unexpected value $v for message type")
    }
  }

  implicit val messageSenderFmt = new StringFormat[MessageSender] {
    override def encode(sender: MessageSender): String = s"${sender.eori}/${sender.branch}"

    override def decode(s: String): MessageSender = s.split("/").toList match {
      case eori :: branch :: Nil => MessageSender(eori, branch)
      case _ => throw new XmlDecodingException(s"Unexpected value $s for message sender")
    }
  }

  implicit val metadataFmt = new Format[Metadata] {
    override def encode(metadata: Metadata): NodeSeq = Seq(
      <MesSenMES3>{metadata.messageSender.toXmlString}</MesSenMES3>,
      <MesIdeMES19>{metadata.messageId}</MesIdeMES19>,
      <MesTypMES20>{metadata.messageType.toXmlString}</MesTypMES20>,
      metadata.datetime.toXml
    ).flatten

    override def decode(data: NodeSeq): Metadata = Metadata(
      messageId = (data \\ "MesIdeMES19").text,
      messageSender = (data \\ "MesSenMES3").text.parseXmlString[MessageSender],
      messageType = (data \\ "MesTypMES20").text.parseXmlString[MessageType],
      datetime = data.parseXml[Instant]
    )
  }
}

object MetadataFormats extends MetadataFormats
