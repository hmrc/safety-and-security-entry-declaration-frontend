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
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class AnyOverallDocumentsPageSpec extends SpecBase with PageBehaviours {

  "AnyOverallDocumentsPage" - {

    beRetrievable[Boolean](AnyOverallDocumentsPage)

    beSettable[Boolean](AnyOverallDocumentsPage)

    beRemovable[Boolean](AnyOverallDocumentsPage)

    "must navigate in Normal Mode" - {

      "to OverallDocument if answer is yes" in {
        val userAnswers = emptyUserAnswers.set(AnyOverallDocumentsPage, true).success.value

        AnyOverallDocumentsPage.navigate(NormalMode, userAnswers)
          .mustEqual(
            transportRoutes.OverallDocumentController.onPageLoad(
              NormalMode,
              userAnswers.lrn,
              index
            )
          )
      }

      "to AddSeal if answer is no" in {
        val userAnswers = emptyUserAnswers.set(AnyOverallDocumentsPage, false).success.value

        AnyOverallDocumentsPage.navigate(NormalMode, userAnswers)
          .mustEqual(transportRoutes.AddAnySealsController.onPageLoad(NormalMode, userAnswers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AnyOverallDocumentsPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
