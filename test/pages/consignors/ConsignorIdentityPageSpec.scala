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

package pages.consignors

import base.SpecBase
import controllers.consignors.routes
import models.ConsignorIdentity.{GBEORI, NameAddress}
import models.{Address, ConsignorIdentity, Country, GbEori, NormalMode}
import pages.{Waypoints, EmptyWaypoints}
import pages.behaviours.PageBehaviours

class ConsignorIdentityPageSpec extends SpecBase with PageBehaviours {

  "ConsignorIdentityPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Consignor EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(ConsignorIdentityPage(index), GBEORI).success.value

        ConsignorIdentityPage(index)
          .navigate(waypoints, answers)
          .mustEqual(routes.ConsignorEORIController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Consignor Name when answered `name & address`" in {
        val answers = emptyUserAnswers.set(ConsignorIdentityPage(index), NameAddress).success.value

        ConsignorIdentityPage(index)
          .navigate(waypoints, answers)
          .mustEqual(routes.ConsignorNameController.onPageLoad(waypoints, answers.lrn, index))
      }
    }

    "must navigate when the current waypoint is AddConsignor" - {

      val waypoints = Waypoints(List(AddConsignorPage.waypoint((NormalMode))))

      "to Consignor EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(ConsignorIdentityPage(index), GBEORI).success.value

        ConsignorIdentityPage(index)
          .navigate(waypoints, answers)
          .mustEqual(routes.ConsignorEORIController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Consignor Name when answered `name & address`" in {
        val answers = emptyUserAnswers.set(ConsignorIdentityPage(index), NameAddress).success.value

        ConsignorIdentityPage(index)
          .navigate(waypoints, answers)
          .mustEqual(routes.ConsignorNameController.onPageLoad(waypoints, answers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Check Consignor" - {

      val waypoints = Waypoints(List(CheckConsignorPage(index).waypoint))

      "when the answer is GB EORI" - {

        "and Consignor EORI is already answered" - {

          "to Check Consignor with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(ConsignorEORIPage(index), GbEori("123456789000")).success.value
                .set(ConsignorIdentityPage(index), ConsignorIdentity.GBEORI).success.value

            ConsignorIdentityPage(index).navigate(waypoints, answers)
              .mustEqual(routes.CheckConsignorController.onPageLoad(EmptyWaypoints, answers.lrn, index))
          }
        }

        "and Consignor EORI has not been answered" - {

          "to Consignor EORI" in {

            val answers = emptyUserAnswers.set(ConsignorIdentityPage(index), ConsignorIdentity.GBEORI).success.value

            ConsignorIdentityPage(index).navigate(waypoints, answers)
              .mustEqual(routes.ConsignorEORIController.onPageLoad(waypoints, answers.lrn, index))
          }
        }
      }

      "when the answer is Name and Address" - {

        "and Consignor Name is already answered" - {

          "to Check Consignor with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(ConsignorNamePage(index), "Name").success.value
                .set(ConsignorIdentityPage(index), ConsignorIdentity.NameAddress).success.value

            ConsignorIdentityPage(index).navigate(waypoints, answers)
              .mustEqual(routes.CheckConsignorController.onPageLoad(EmptyWaypoints, answers.lrn, index))
          }
        }

        "and Consignor Name has not been answered" - {

          "to Consignor Name" in {

            val answers = emptyUserAnswers.set(ConsignorIdentityPage(index), ConsignorIdentity.NameAddress).success.value

            ConsignorIdentityPage(index).navigate(waypoints, answers)
              .mustEqual(routes.ConsignorNameController.onPageLoad(waypoints, answers.lrn, index))
          }
        }
      }
    }

    "must remove Consignor EORI when the answer is Name and Address" in {

      val answers =
        emptyUserAnswers
          .set(ConsignorIdentityPage(index), ConsignorIdentity.NameAddress).success.value
          .set(ConsignorEORIPage(index), GbEori("123456789000")).success.value

      val result = ConsignorIdentityPage(index).cleanup(Some(ConsignorIdentity.NameAddress), answers).success.value

      result mustEqual answers.remove(ConsignorEORIPage(index)).success.value
    }

    "must remove Consignor Name and Consignor Address when the answer is EORI" in {

      val answers =
        emptyUserAnswers
          .set(ConsignorIdentityPage(index), ConsignorIdentity.GBEORI).success.value
          .set(ConsignorNamePage(index), "name").success.value
          .set(ConsignorAddressPage(index), Address("street", "town", "post code", Country("GB", "United Kingdom"))).success.value

      val result = ConsignorIdentityPage(index).cleanup(Some(ConsignorIdentity.GBEORI), answers).success.value

      result.mustEqual(
        answers
          .remove(ConsignorNamePage(index)).success.value
          .remove(ConsignorAddressPage(index)).success.value)
    }
  }
}
