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
import models.{CheckMode, Index, NormalMode}
import pages.behaviours.PageBehaviours

class AddAnyNotifiedPartiesPageSpec extends SpecBase with PageBehaviours {

  "AddAnyNotifiedPartiesPage" - {

    beRetrievable[Boolean](AddAnyNotifiedPartiesPage)

    beSettable[Boolean](AddAnyNotifiedPartiesPage)

    beRemovable[Boolean](AddAnyNotifiedPartiesPage)

    "must navigate in Normal Mode" - {

      "to Notified Party Identity when the answer is yes" in {

        val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, true).success.value

        AddAnyNotifiedPartiesPage.navigate(NormalMode, answers)
          .mustEqual(consigneesRoutes.NotifiedPartyIdentityController.onPageLoad(NormalMode, answers.lrn, Index(0)))
      }

      "to Check Consignees and Notified Parties when the answer is no" in {

        val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, false).success.value

        AddAnyNotifiedPartiesPage.navigate(NormalMode, answers)
          .mustEqual(consigneesRoutes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(answers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddAnyNotifiedPartiesPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
