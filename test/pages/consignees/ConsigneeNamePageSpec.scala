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
import models.{Address, Country, NormalMode}
import pages.Breadcrumbs
import pages.behaviours.PageBehaviours

class ConsigneeNamePageSpec extends SpecBase with PageBehaviours {

  "ConsigneeNamePage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = Breadcrumbs.empty

      "to consignee address" in {

        ConsigneeNamePage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            routes.ConsigneeAddressController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current breadcrumb is AddConsignee" - {

      val breadcrumbs = Breadcrumbs(List(AddConsigneePage.breadcrumb(NormalMode)))

      "to consignee address" in {

        ConsigneeNamePage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            routes.ConsigneeAddressController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current breadcrumb is Check Consignee" - {

      val breadcrumbs = Breadcrumbs(List(CheckConsigneePage(index).breadcrumb))

      "and Consignee Address has been answered" - {

        "to Check Consignee with the current breadcrumb removed" in {

          val answers =
            emptyUserAnswers
              .set(ConsigneeAddressPage(index), Address("street", "city", "AA11 1AA", Country("GB", "United Kingdom")))
              .success.value

          ConsigneeNamePage(index).navigate(breadcrumbs, answers)
            .mustEqual(routes.CheckConsigneeController.onPageLoad(breadcrumbs.pop, answers.lrn, index))
        }
      }

      "and Consignee Address has not been answered" - {

        "to Consignee Address" in {

          ConsigneeNamePage(index).navigate(breadcrumbs, emptyUserAnswers)
            .mustEqual(routes.ConsigneeAddressController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index))
        }
      }
    }
  }
}
