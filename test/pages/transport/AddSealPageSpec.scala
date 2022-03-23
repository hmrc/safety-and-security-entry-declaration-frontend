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

package pages.transport

import base.SpecBase
import controllers.transport.{routes => transportRoutes}
import controllers.routes
import models.{CheckMode, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class AddSealPageSpec extends SpecBase with PageBehaviours {

  "AddSealPage" - {

    beRetrievable[Boolean](AddSealPage)

    beSettable[Boolean](AddSealPage)

    beRemovable[Boolean](AddSealPage)

    "must navigate in Normal Mode" - {

      "to JourneyRecoveryController when add another answer is yes but DeriveNumberOfSeals not Set" in {
        val answers = emptyUserAnswers.set(AddSealPage, true).success.value

        AddSealPage
          .navigate(NormalMode, answers, true)
          .mustEqual(routes.JourneyRecoveryController.onPageLoad())
      }

      "to Check Your Answers when add another answer is no" in {
        val answers = emptyUserAnswers.set(AddSealPage, false).success.value

        AddSealPage
          .navigate(NormalMode, answers, addAnother = false)
          .mustEqual(transportRoutes.CheckTransportController.onPageLoad(emptyUserAnswers.lrn))
      }

      "to Seal with (index + 1) if yes is selected" in {
        // Add a document and check redirect multiple times to check it works for each index
        (0 to 2).foldLeft(emptyUserAnswers) {
          case (prevAnswers, idx) =>
            val currIndex = Index(idx)
            val answers = prevAnswers.set(
              SealPage(currIndex),
              arbitrary[String].sample.value
            ).success.value

            AddSealPage.navigate(NormalMode, answers, addAnother = true)
              .mustEqual(
                transportRoutes.SealController.onPageLoad(
                  NormalMode,
                  answers.lrn,
                  currIndex + 1
                )
              )
            answers
        }
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {
        AddSealPage.navigate(CheckMode, emptyUserAnswers, false)
          .mustEqual(transportRoutes.CheckTransportController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
