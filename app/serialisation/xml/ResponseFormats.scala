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

import models.completion.downstream.CorrelationId

trait ResponseFormats {
  // A successful response to a submit or amend request will contain a correlation ID
  implicit val correlationIdDecoding = new Decoding[CorrelationId] {
    override def decode(data: NodeSeq): CorrelationId = CorrelationId(
      (data \ "ResponseData" \ "CorrelationId").headOption map { _.text } getOrElse {
        throw new XmlDecodingException("Could not extract correlation ID from success response")
      }
    )
  }
}

object ResponseFormats extends ResponseFormats
