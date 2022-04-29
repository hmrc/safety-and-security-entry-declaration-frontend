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

import scala.xml.NodeSeq

import models.MovementReferenceNumber
import models.completion.downstream.{CorrelationId, Outcome, RejectionReason}
import serialisation.xml.XmlImplicits._

trait ResponseFormats extends CommonFormats {
  // A successful response to a submit or amend request will contain a correlation ID
  implicit val correlationIdDecoding = new Decoding[CorrelationId] {
    override def decode(data: NodeSeq): CorrelationId = CorrelationId(
      (data \ "ResponseData" \ "CorrelationId").headOption map { _.text } getOrElse {
        throw new XmlDecodingException("Could not extract correlation ID from success response")
      }
    )
  }

  // The list outcomes API will return a list of unacknowledged outcomes by correlation ID
  implicit val correlationIdsDecoding = new Decoding[List[CorrelationId]] {
    override def decode(data: NodeSeq): List[CorrelationId] = {
      (data \ "response" \ "correlationId").toList.map { id => CorrelationId(id.text) }
    }
  }

  private implicit val rejectionReasonDecoding = new Decoding[RejectionReason] {
    override def decode(data: NodeSeq): RejectionReason = {
      val code = (data \ "AmeRejMotCodHEA604").headOption
      val desc = Seq(data \ "AmeRejMotTexHEA605", data \ "DecRejReaHEA252").flatten.headOption

      RejectionReason(code.map { _.text }, desc.map { _.text })
    }
  }

  implicit val acceptedOutcomeDecoding = new Decoding[Outcome.Accepted] {
    override def decode(data: NodeSeq): Outcome.Accepted = Outcome.Accepted(
      correlationId = CorrelationId((data \ "CorIdeMES25").text),
      mrn = (data \ "HEAHEA" \ "DocNumHEA5").text.parseXmlString[MovementReferenceNumber]
    )
  }

  implicit val rejectedOutcomeDecoding = new Decoding[Outcome.Rejected] {
    override def decode(data: NodeSeq): Outcome.Rejected = Outcome.Rejected(
      correlationId = CorrelationId((data \ "CorIdeMES25").text),
      reason = (data \ "HEAHEA").parseXml[RejectionReason]
    )
  }

  implicit val outcomeDecoding = new Decoding[Outcome] {
    private val acceptedTypes = Set("CC328A", "CC304A")

    override def decode(data: NodeSeq): Outcome = {
      val root = data \ "response" \ "_"
      if (acceptedTypes.contains((root \ "MesTypMES20").text)) {
        root.parseXml[Outcome.Accepted]
      } else {
        root.parseXml[Outcome.Rejected]
      }
    }
  }
}

object ResponseFormats extends ResponseFormats
