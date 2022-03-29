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

class ConsigneeEORIPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeEORIPage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = Breadcrumbs.empty

      "to Check Consignee Details" in {

        ConsigneeEORIPage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            consigneesRoutes.CheckConsigneeController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current breadcrumb is AddConsignee" - {

      val breadcrumbs = Breadcrumbs(List(AddConsigneePage))

      "to Check Consignee Details" in {

        ConsigneeEORIPage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            consigneesRoutes.CheckConsigneeController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneeEORIPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
