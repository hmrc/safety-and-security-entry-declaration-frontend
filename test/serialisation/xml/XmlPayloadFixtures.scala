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

import scala.io.Source
import scala.xml.{NodeSeq, XML}

import models.completion.downstream.CorrelationId

/**
 * Provide XML payload samples from test resources
 */
trait XmlPayloadFixtures {
  protected def readPayload(s: String): NodeSeq = {
    XML.loadString(Source.fromResource(s"payload/$s").mkString)
  }

  lazy val submissionResponse = readPayload("response/submission-1.xml")
  lazy val submissionCorrId = CorrelationId("87491122139921")

  lazy val listOutcomesResponse = readPayload("response/list-outcomes-1.xml")
  lazy val outcomeCorrIds = List("1234567890", "0987654321").map(CorrelationId.apply)

  lazy val acceptedSubmissionOutcome = readPayload("response/accepted-submission-1.xml")
  lazy val acceptedAmendmentOutcome = readPayload("response/accepted-amendment-1.xml")
  lazy val rejectedSubmissionOutcome = readPayload("response/rejected-submission-1.xml")
  lazy val rejectedAmendmentOutcome = readPayload("response/rejected-amendment-1.xml")

}

object XmlPayloadFixtures extends XmlPayloadFixtures
