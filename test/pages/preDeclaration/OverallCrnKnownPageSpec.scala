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

package pages.preDeclaration

import base.SpecBase
import controllers.preDeclaration.{routes => preDecRoutes}
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class OverallCrnKnownPageSpec extends SpecBase with PageBehaviours {

  "OverallCrnKnownPage" - {

    beRetrievable[Boolean](OverallCrnKnownPage)

    beSettable[Boolean](OverallCrnKnownPage)

    beRemovable[Boolean](OverallCrnKnownPage)

    "must navigate in Normal Mode" - {

      "to Overall CRN if the answer is yes" in {

        val answers = emptyUserAnswers.set(OverallCrnKnownPage, true).success.value

        OverallCrnKnownPage
          .navigate(NormalMode, answers)
          .mustEqual(preDecRoutes.OverallCrnController.onPageLoad(NormalMode, answers.lrn))
      }

      "to Gross Weight if the answer is no" in {

        val answers = emptyUserAnswers.set(OverallCrnKnownPage, false).success.value

        OverallCrnKnownPage
          .navigate(NormalMode, answers)
          .mustEqual(preDecRoutes.ProvideGrossWeightController.onPageLoad(NormalMode, answers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        OverallCrnKnownPage
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
