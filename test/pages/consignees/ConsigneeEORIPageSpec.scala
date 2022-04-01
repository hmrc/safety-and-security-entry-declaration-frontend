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
import models.NormalMode
import pages.{Breadcrumbs, EmptyBreadcrumbs}
import pages.behaviours.PageBehaviours

class ConsigneeEORIPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeEORIPage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = EmptyBreadcrumbs

      "to Check Consignee Details" in {

        ConsigneeEORIPage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            routes.CheckConsigneeController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current breadcrumb is AddConsignee" - {

      val breadcrumbs = Breadcrumbs(List(AddConsigneePage.breadcrumb(NormalMode)))

      "to Check Consignee Details" in {

        ConsigneeEORIPage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            routes.CheckConsigneeController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current breadcrumb is CheckConsignee" - {

      val breadcrumbs = Breadcrumbs(List(CheckConsigneePage(index).breadcrumb))

      "to Check Consignee with the current breadcrumb removed" in {

        ConsigneeEORIPage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(routes.CheckConsigneeController.onPageLoad(breadcrumbs.pop, emptyUserAnswers.lrn, index))
      }
    }
  }
}
