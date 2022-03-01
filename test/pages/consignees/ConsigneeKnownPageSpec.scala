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

package pages.consignees

import base.SpecBase
import controllers.consignees.{routes => consigneesRoutes}
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class ConsigneeKnownPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeKnownPage" - {

    beRetrievable[Boolean](ConsigneeKnownPage)

    beSettable[Boolean](ConsigneeKnownPage)

    beRemovable[Boolean](ConsigneeKnownPage)

    "must navigate in Normal Mode" - {

      "to `How do you want to identify the consignee` when answer is yes" in {
        val answers = emptyUserAnswers.set(ConsigneeKnownPage, true).success.value

        ConsigneeKnownPage
          .navigate(NormalMode, answers)
          .mustEqual(consigneesRoutes.ConsigneeIdentityController.onPageLoad(NormalMode, answers.lrn, index))
      }

      "to `How do you want to identify the notified party` when answer is no" in {
        val answers = emptyUserAnswers.set(ConsigneeKnownPage, false).success.value

        ConsigneeKnownPage
          .navigate(NormalMode, answers)
          .mustEqual(
            consigneesRoutes.NotifiedPartyIdentityController.onPageLoad(NormalMode, answers.lrn, index)
          )
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneeKnownPage
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
