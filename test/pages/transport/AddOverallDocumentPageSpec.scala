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
import models.{CheckMode, Document, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class AddOverallDocumentPageSpec extends SpecBase with PageBehaviours {

  "AddOverallDocumentPage" - {

    "must navigate in Normal Mode" - {

      "to OverallDocument with (index + 1) if yes is selected" in {
        // Add a document and check redirect multiple times to check it works for each index
        (0 to 2).foldLeft(emptyUserAnswers) {
          case (prevAnswers, idx) =>
            val currIndex = Index(idx)
            val answers = prevAnswers.set(
              OverallDocumentPage(currIndex),
              arbitrary[Document].sample.value
            ).success.value

            AddOverallDocumentPage.navigate(NormalMode, answers, addAnother = true)
              .mustEqual(
                transportRoutes.OverallDocumentController.onPageLoad(
                  NormalMode,
                  answers.lrn,
                  currIndex + 1
                )
              )

            answers
        }
      }

      "to AddAnySeals if no is selected" in {
        val answers = emptyUserAnswers.set(
          OverallDocumentPage(index),
          arbitrary[Document].sample.value
        ).success.value

        AddOverallDocumentPage.navigate(NormalMode, answers, addAnother = false)
          .mustEqual(
            transportRoutes.AddAnySealsController.onPageLoad(NormalMode, answers.lrn)
          )
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddOverallDocumentPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
