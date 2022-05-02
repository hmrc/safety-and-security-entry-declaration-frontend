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

package pages.predec

import base.SpecBase
import controllers.predec.routes
import models.TraderIdentity.{GBEORI, NameAddress}
import models.{Address, Country, GbEori}
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class CarrierIdentityPageSpec extends SpecBase with PageBehaviours {

  "CarrierIdentityPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Carrier EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(CarrierIdentityPage, GBEORI).success.value

        CarrierIdentityPage
          .navigate(waypoints, answers)
          .mustEqual(routes.CarrierEORIController.onPageLoad(waypoints, answers.lrn))
      }

      "to Carrier Name when answered `name & address`" in {
        val answers = emptyUserAnswers.set(CarrierIdentityPage, NameAddress).success.value

        CarrierIdentityPage
          .navigate(waypoints, answers)
          .mustEqual(routes.CarrierNameController.onPageLoad(waypoints, answers.lrn))
      }
    }
    
    "must navigate when the current waypoint is Check Predec" - {

      val waypoints = Waypoints(List(CheckPredecPage.waypoint))

      "when the answer is GB EORI" - {

        "and Carrier EORI is already answered" - {

          "to Check Carrier with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(CarrierEORIPage, GbEori("123456789000")).success.value
                .set(CarrierIdentityPage, GBEORI).success.value

            CarrierIdentityPage.navigate(waypoints, answers)
              .mustEqual(routes.CheckPredecController.onPageLoad(EmptyWaypoints, answers.lrn))
          }
        }

        "and Carrier EORI has not been answered" - {

          "to Carrier EORI" in {

            val answers = emptyUserAnswers.set(CarrierIdentityPage, GBEORI).success.value

            CarrierIdentityPage.navigate(waypoints, answers)
              .mustEqual(routes.CarrierEORIController.onPageLoad(waypoints, answers.lrn))
          }
        }
      }

      "when the answer is Name and Address" - {

        "and Carrier Name is already answered" - {

          "to Check Carrier with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(CarrierNamePage, "Name").success.value
                .set(CarrierIdentityPage, NameAddress).success.value

            CarrierIdentityPage.navigate(waypoints, answers)
              .mustEqual(routes.CheckPredecController.onPageLoad(EmptyWaypoints, answers.lrn))
          }
        }

        "and Carrier Name has not been answered" - {

          "to Carrier Name" in {

            val answers = emptyUserAnswers.set(CarrierIdentityPage, NameAddress).success.value

            CarrierIdentityPage.navigate(waypoints, answers)
              .mustEqual(routes.CarrierNameController.onPageLoad(waypoints, answers.lrn))
          }
        }
      }
    }

    "must remove Carrier EORI when the answer is Name and Address" in {

      val answers =
        emptyUserAnswers
          .set(CarrierIdentityPage, NameAddress).success.value
          .set(CarrierEORIPage, GbEori("123456789000")).success.value

      val result = CarrierIdentityPage.cleanup(Some(NameAddress), answers).success.value

      result mustEqual answers.remove(CarrierEORIPage).success.value
    }

    "must remove Carrier Name and Carrier Address when the answer is EORI" in {

      val answers =
        emptyUserAnswers
          .set(CarrierIdentityPage, GBEORI).success.value
          .set(CarrierNamePage, "name").success.value
          .set(CarrierAddressPage, Address("street", "town", "post code", Country("GB", "United Kingdom"))).success.value

      val result = CarrierIdentityPage.cleanup(Some(GBEORI), answers).success.value

      result.mustEqual(
        answers
          .remove(CarrierNamePage).success.value
          .remove(CarrierAddressPage).success.value)
    }
  }
}
