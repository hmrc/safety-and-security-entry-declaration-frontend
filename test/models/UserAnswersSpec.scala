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

package models

import java.time.Instant

import base.SpecBase

import models.completion.DeclarationEvent
import models.completion.downstream.{CorrelationId, MessageType, Outcome}

class UserAnswersSpec extends SpecBase {
  private val corrId = CorrelationId("123456")
  private val mrn = MovementReferenceNumber("654321")
  private val messageType = MessageType.Submission
  private val outcome = Outcome.Accepted(corrId, mrn)
  private val incompleteEvent = DeclarationEvent(messageType, Instant.now, outcome = None)
  private val completeEvent = incompleteEvent.copy(outcome = Some(outcome))

  "UserAnswers" - {
    "withDeclarationEvent" - {
      "should add a new declaration event by correlation ID" in {
        val actual = emptyUserAnswers.withDeclarationEvent(corrId, incompleteEvent)
        val expected = emptyUserAnswers.copy(declarationEvents = Map(corrId -> incompleteEvent))

        actual must be(expected)
      }

      "should update declaration event if correlation ID is present" in {
        val answers = emptyUserAnswers.copy(declarationEvents = Map(corrId -> incompleteEvent))

        val actual = answers.withDeclarationEvent(corrId, completeEvent)
        val expected = answers.copy(declarationEvents = Map(corrId -> completeEvent))

        actual must be(expected)
      }
    }
  }
}
