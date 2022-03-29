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
import models.ConsigneeIdentity.{GBEORI, NameAddress}
import pages.Breadcrumbs
import pages.behaviours.PageBehaviours

class ConsigneeIdentityPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeIdentityPage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = Breadcrumbs.empty

      "to Consignee EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), GBEORI).success.value

        ConsigneeIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(consigneesRoutes.ConsigneeEORIController.onPageLoad(breadcrumbs, answers.lrn, index))
      }

      "to Consignee Name when answered `name & address`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), NameAddress).success.value

        ConsigneeIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(consigneesRoutes.ConsigneeNameController.onPageLoad(breadcrumbs, answers.lrn, index))
      }
    }

    "must navigate when the current breadcrumb is AddConsignee" - {

      val breadcrumbs = Breadcrumbs(List(AddConsigneePage))

      "to Consignee EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), GBEORI).success.value

        ConsigneeIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(consigneesRoutes.ConsigneeEORIController.onPageLoad(breadcrumbs, answers.lrn, index))
      }

      "to Consignee Name when answered `name & address`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), NameAddress).success.value

        ConsigneeIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(consigneesRoutes.ConsigneeNameController.onPageLoad(breadcrumbs, answers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneeIdentityPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
