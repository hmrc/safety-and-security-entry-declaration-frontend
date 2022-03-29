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
import controllers.consignees.routes
import models.{ConsigneeIdentity, GbEori, Index}
import pages.Breadcrumbs
import pages.behaviours.PageBehaviours
import queries.consignees.ConsigneeKeyQuery

class AddConsigneePageSpec extends SpecBase with PageBehaviours {

  "AddConsigneePage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = Breadcrumbs.empty

      "to Consignee Identity for the next index when the answer is yes" in {

        val answers =
          emptyUserAnswers
            .set(ConsigneeEORIPage(Index(0)), GbEori("123456789000")).success.value
            .set(ConsigneeKeyQuery(Index(0)), 1).success.value
            .set(AddConsigneePage, true).success.value

        AddConsigneePage.navigate(breadcrumbs, answers)
          .mustEqual(routes.ConsigneeIdentityController.onPageLoad(breadcrumbs, answers.lrn, Index(1)))
      }

      "to Add Any Notified Parties when the answer is no" in {

        val answers = emptyUserAnswers.set(AddConsigneePage, false).success.value

        AddConsigneePage.navigate(breadcrumbs, answers)
          .mustEqual(routes.AddAnyNotifiedPartiesController.onPageLoad(breadcrumbs, answers.lrn))
      }
    }

    "must navigate when the current breadcrumb is Check Consignees and Notified Parties" - {

      val breadcrumbs = Breadcrumbs(List(CheckConsigneesAndNotifiedPartiesPage))

      "when the answer is yes" - {

        "to ConsigneeIdentity for the next index with AddConsignee added to the breadcrumbs" in {

          val answers =
            emptyUserAnswers
              .set(AddConsigneePage, true).success.value
              .set(ConsigneeIdentityPage(Index(0)), ConsigneeIdentity.GBEORI).success.value
              .set(ConsigneeEORIPage(Index(0)), GbEori("123456789000")).success.value
              .set(ConsigneeKeyQuery(Index(0)), 1).success.value

          AddConsigneePage.navigate(breadcrumbs, answers)
            .mustEqual(routes.ConsigneeIdentityController.onPageLoad(breadcrumbs.push(AddConsigneePage), answers.lrn, Index(1)))
        }
      }

      "when the answer is no" - {

        "to Check Consignees and Notified Parties with the current breadcrumb removed" in {

          val answers = emptyUserAnswers.set(AddConsigneePage, false).success.value

          AddConsigneePage.navigate(breadcrumbs, answers)
            .mustEqual(routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(breadcrumbs.pop, answers.lrn))
        }
      }
    }
  }
}