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

import base.SpecBase

import models.MovementReferenceNumber
import models.completion.downstream.{CorrelationId, Outcome, RejectionReason}

class ResponseFormatsSpec
  extends SpecBase
  with ResponseFormats
  with XmlImplicits
  with XmlPayloadFixtures {

  "The correlation ID decoding" - {
    "should successfully read correlation ID from a success response" in {
      submissionResponse.parseXml[CorrelationId] must be(submissionCorrId)
    }
  }

  "The list of outcomes (correlation IDs) decoding" - {
    "should successfully read a list of correlation IDs from an outcomes response" in {
      val actual = listOutcomesResponse.parseXml[List[CorrelationId]]

      actual must contain theSameElementsAs(outcomeCorrIds)
    }
  }

  "The outcome decoding" - {
    "should successfully read a new submission accepted (CC328A) outcome" in {
      val expected = Outcome.Accepted(
        CorrelationId("0JRF7UncK0t004"),
        MovementReferenceNumber("10GB08I01234567891")
      )
      val actual = acceptedSubmissionOutcome.parseXml[Outcome]

      actual must be(expected)
    }

    "should successfully read an amendment accepted (CC304A) outcome" in {
      val expected = Outcome.Accepted(
        CorrelationId("1LRF2NnNK0t124"),
        MovementReferenceNumber("10GB08I00000000000")
      )
      val actual = acceptedAmendmentOutcome.parseXml[Outcome]

      actual must be(expected)
    }

    "should successfully read a new submission rejected (CC316A) outcome" in {
      val expected = Outcome.Rejected(
        CorrelationId("7J1F7gngK0200k"),
        RejectionReason(code = None, desc = Some("Rejection explanation"))
      )
      val actual = rejectedSubmissionOutcome.parseXml[Outcome]

      actual must be(expected)
    }

    "should successfully read an amendment rejected (CC305A) outcome" in {
      val expected = Outcome.Rejected(
        CorrelationId("2L227hhaK0211k"),
        RejectionReason(code = Some("1"), desc = Some("Trader not allowed to amend"))
      )
      val actual = rejectedAmendmentOutcome.parseXml[Outcome]

      actual must be(expected)
    }
  }
}
