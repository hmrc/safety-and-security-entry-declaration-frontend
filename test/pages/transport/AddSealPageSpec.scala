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

class AddSealPageSpec extends SpecBase with PageBehaviours {

  "AddSealPage" - {

    beRetrievable[Boolean](AddSealPage)

    beSettable[Boolean](AddSealPage)

    beRemovable[Boolean](AddSealPage)

    "must navigate in Normal Mode" - {

      "to SealController when answer is yes" in {
        val answers = emptyUserAnswers.set(AddSealPage, true).success.value

        AddSealPage
          .navigate(NormalMode, answers)
          .mustEqual(transportRoutes.SealController.onPageLoad(NormalMode, answers.lrn))
      }

      "to Check Your Answers when answer is no" in {
        val answers = emptyUserAnswers.set(AddSealPage, false).success.value

        AddSealPage
          .navigate(NormalMode, answers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn)
          )
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {
        AddSealPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
