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
import models.{CheckMode, Index}
import pages.Breadcrumbs
import pages.behaviours.PageBehaviours

class AddAnyNotifiedPartiesPageSpec extends SpecBase with PageBehaviours {

  "AddAnyNotifiedPartiesPage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = Breadcrumbs.empty

      "to Notified Party Identity when the answer is yes" in {

        val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, true).success.value

        AddAnyNotifiedPartiesPage.navigate(breadcrumbs, answers)
          .mustEqual(consigneesRoutes.NotifiedPartyIdentityController.onPageLoad(breadcrumbs, answers.lrn, Index(0)))
      }

      "to Check Consignees and Notified Parties when the answer is no" in {

        val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, false).success.value

        AddAnyNotifiedPartiesPage.navigate(breadcrumbs, answers)
          .mustEqual(consigneesRoutes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(breadcrumbs, answers.lrn))
      }
    }

    "must navigate when the current breadcrumb is a CheckAnswersPage" - {

      "to Check Your Answers" in {

        AddAnyNotifiedPartiesPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
