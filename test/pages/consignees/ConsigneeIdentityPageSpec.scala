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
import models.ConsigneeIdentity.{GBEORI, NameAddress}
import models.{Address, ConsigneeIdentity, Country, GbEori}
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
          .mustEqual(routes.ConsigneeEORIController.onPageLoad(breadcrumbs, answers.lrn, index))
      }

      "to Consignee Name when answered `name & address`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), NameAddress).success.value

        ConsigneeIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(routes.ConsigneeNameController.onPageLoad(breadcrumbs, answers.lrn, index))
      }
    }

    "must navigate when the current breadcrumb is AddConsignee" - {

      val breadcrumbs = Breadcrumbs(List(AddConsigneePage))

      "to Consignee EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), GBEORI).success.value

        ConsigneeIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(routes.ConsigneeEORIController.onPageLoad(breadcrumbs, answers.lrn, index))
      }

      "to Consignee Name when answered `name & address`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), NameAddress).success.value

        ConsigneeIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(routes.ConsigneeNameController.onPageLoad(breadcrumbs, answers.lrn, index))
      }
    }

    "must navigate when the current breadcrumb is Check Consignee" - {

      val breadcrumbs = Breadcrumbs(List(CheckConsigneePage(index)))

      "when the answer is GB EORI" - {

        "and Consignee EORI is already answered" - {

          "to Check Consignee with the current breadcrumb removed" in {

            val answers =
              emptyUserAnswers
                .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value
                .set(ConsigneeIdentityPage(index), ConsigneeIdentity.GBEORI).success.value

            ConsigneeIdentityPage(index).navigate(breadcrumbs, answers)
              .mustEqual(routes.CheckConsigneeController.onPageLoad(breadcrumbs.pop, answers.lrn, index))
          }
        }

        "and Consignee EORI has not been answered" - {

          "to Consignee EORI" in {

            val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), ConsigneeIdentity.GBEORI).success.value

            ConsigneeIdentityPage(index).navigate(breadcrumbs, answers)
              .mustEqual(routes.ConsigneeEORIController.onPageLoad(breadcrumbs, answers.lrn, index))
          }
        }
      }

      "when the answer is Name and Address" - {

        "and Consignee Name is already answered" - {

          "to Check Consignee with the current breadcrumb removed" in {

            val answers =
              emptyUserAnswers
                .set(ConsigneeNamePage(index), "Name").success.value
                .set(ConsigneeIdentityPage(index), ConsigneeIdentity.NameAddress).success.value

            ConsigneeIdentityPage(index).navigate(breadcrumbs, answers)
              .mustEqual(routes.CheckConsigneeController.onPageLoad(breadcrumbs.pop, answers.lrn, index))
          }
        }

        "and Consignee Name has not been answered" - {

          "to Consignee Name" in {

            val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), ConsigneeIdentity.NameAddress).success.value

            ConsigneeIdentityPage(index).navigate(breadcrumbs, answers)
              .mustEqual(routes.ConsigneeNameController.onPageLoad(breadcrumbs, answers.lrn, index))
          }
        }
      }
    }

    "must remove Consignee EORI when the answer is Name and Address" in {

      val answers =
        emptyUserAnswers
          .set(ConsigneeIdentityPage(index), ConsigneeIdentity.NameAddress).success.value
          .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value

      val result = ConsigneeIdentityPage(index).cleanup(Some(ConsigneeIdentity.NameAddress), answers).success.value

      result mustEqual answers.remove(ConsigneeEORIPage(index)).success.value
    }

    "must remove Consignee Name and Consignee Address when the answer is EORI" in {

      val answers =
        emptyUserAnswers
          .set(ConsigneeIdentityPage(index), ConsigneeIdentity.GBEORI).success.value
          .set(ConsigneeNamePage(index), "name").success.value
          .set(ConsigneeAddressPage(index), Address("street", "town", "post code", Country("GB", "United Kingdom"))).success.value

      val result = ConsigneeIdentityPage(index).cleanup(Some(ConsigneeIdentity.GBEORI), answers).success.value

      result.mustEqual(
        answers
          .remove(ConsigneeNamePage(index)).success.value
          .remove(ConsigneeAddressPage(index)).success.value)
    }
  }
}
