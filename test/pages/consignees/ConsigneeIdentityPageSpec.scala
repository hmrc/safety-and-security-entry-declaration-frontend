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
import models.TraderIdentity.{GBEORI, NameAddress}
import models.{Address, TraderIdentity, Country, GbEori, NormalMode}
import pages.{Waypoints, EmptyWaypoints}
import pages.behaviours.PageBehaviours

class ConsigneeIdentityPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeIdentityPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Consignee EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), GBEORI).success.value

        ConsigneeIdentityPage(index)
          .navigate(waypoints, answers)
          .mustEqual(routes.ConsigneeEORIController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Consignee Name when answered `name & address`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), NameAddress).success.value

        ConsigneeIdentityPage(index)
          .navigate(waypoints, answers)
          .mustEqual(routes.ConsigneeNameController.onPageLoad(waypoints, answers.lrn, index))
      }
    }

    "must navigate when the current waypoint is AddConsignee" - {

      val waypoints = Waypoints(List(AddConsigneePage.waypoint((NormalMode))))

      "to Consignee EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), GBEORI).success.value

        ConsigneeIdentityPage(index)
          .navigate(waypoints, answers)
          .mustEqual(routes.ConsigneeEORIController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Consignee Name when answered `name & address`" in {
        val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), NameAddress).success.value

        ConsigneeIdentityPage(index)
          .navigate(waypoints, answers)
          .mustEqual(routes.ConsigneeNameController.onPageLoad(waypoints, answers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Check Consignee" - {

      val waypoints = Waypoints(List(CheckConsigneePage(index).waypoint))

      "when the answer is GB EORI" - {

        "and Consignee EORI is already answered" - {

          "to Check Consignee with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value
                .set(ConsigneeIdentityPage(index), TraderIdentity.GBEORI).success.value

            ConsigneeIdentityPage(index).navigate(waypoints, answers)
              .mustEqual(routes.CheckConsigneeController.onPageLoad(EmptyWaypoints, answers.lrn, index))
          }
        }

        "and Consignee EORI has not been answered" - {

          "to Consignee EORI" in {

            val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), TraderIdentity.GBEORI).success.value

            ConsigneeIdentityPage(index).navigate(waypoints, answers)
              .mustEqual(routes.ConsigneeEORIController.onPageLoad(waypoints, answers.lrn, index))
          }
        }
      }

      "when the answer is Name and Address" - {

        "and Consignee Name is already answered" - {

          "to Check Consignee with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(ConsigneeNamePage(index), "Name").success.value
                .set(ConsigneeIdentityPage(index), TraderIdentity.NameAddress).success.value

            ConsigneeIdentityPage(index).navigate(waypoints, answers)
              .mustEqual(routes.CheckConsigneeController.onPageLoad(EmptyWaypoints, answers.lrn, index))
          }
        }

        "and Consignee Name has not been answered" - {

          "to Consignee Name" in {

            val answers = emptyUserAnswers.set(ConsigneeIdentityPage(index), TraderIdentity.NameAddress).success.value

            ConsigneeIdentityPage(index).navigate(waypoints, answers)
              .mustEqual(routes.ConsigneeNameController.onPageLoad(waypoints, answers.lrn, index))
          }
        }
      }
    }

    "must remove Consignee EORI when the answer is Name and Address" in {

      val answers =
        emptyUserAnswers
          .set(ConsigneeIdentityPage(index), TraderIdentity.NameAddress).success.value
          .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value

      val result = ConsigneeIdentityPage(index).cleanup(Some(TraderIdentity.NameAddress), answers).success.value

      result mustEqual answers.remove(ConsigneeEORIPage(index)).success.value
    }

    "must remove Consignee Name and Consignee Address when the answer is EORI" in {

      val answers =
        emptyUserAnswers
          .set(ConsigneeIdentityPage(index), TraderIdentity.GBEORI).success.value
          .set(ConsigneeNamePage(index), "name").success.value
          .set(ConsigneeAddressPage(index), Address("street", "town", "post code", Country("GB", "United Kingdom"))).success.value

      val result = ConsigneeIdentityPage(index).cleanup(Some(TraderIdentity.GBEORI), answers).success.value

      result.mustEqual(
        answers
          .remove(ConsigneeNamePage(index)).success.value
          .remove(ConsigneeAddressPage(index)).success.value)
    }
  }
}
