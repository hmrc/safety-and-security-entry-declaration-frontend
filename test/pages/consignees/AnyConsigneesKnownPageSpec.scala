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
import models.CheckMode
import pages.Breadcrumbs
import pages.behaviours.PageBehaviours

class AnyConsigneesKnownPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeKnownPage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = Breadcrumbs.empty

      "to Consignee Identity when answer is yes" in {
        val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, true).success.value

        AnyConsigneesKnownPage
          .navigate(breadcrumbs, answers)
          .mustEqual(consigneesRoutes.ConsigneeIdentityController.onPageLoad(breadcrumbs, answers.lrn, index))
      }

      "to Notified Party Identity when answer is no" in {
        val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, false).success.value

        AnyConsigneesKnownPage
          .navigate(breadcrumbs, answers)
          .mustEqual(
            consigneesRoutes.NotifiedPartyIdentityController.onPageLoad(breadcrumbs, answers.lrn, index)
          )
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AnyConsigneesKnownPage
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
