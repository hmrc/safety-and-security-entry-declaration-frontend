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
import models.{Address, Country, GbEori, LodgingPersonType}
import models.TraderIdentity.GBEORI
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class LodgingPersonTypePageSpec extends SpecBase with PageBehaviours {

  "LodgingPersonTypePage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Carrier Identity when the answer is Representative" in {

        val answers = emptyUserAnswers.set(LodgingPersonTypePage, LodgingPersonType.Representative).success.value

        LodgingPersonTypePage
          .navigate(waypoints, answers)
          .mustEqual(routes.CarrierIdentityController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }

      "to Gross Weight when the answer is Carrier" in {

        val answers = emptyUserAnswers.set(LodgingPersonTypePage, LodgingPersonType.Carrier).success.value

        LodgingPersonTypePage
          .navigate(waypoints, answers)
          .mustEqual(routes.ProvideGrossWeightController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Predec" - {

      val waypoints = Waypoints(List(CheckPredecPage.waypoint))

      "and the answer is Representative" - {

        "to Check Predec with the current waypoint removed when Carrier Identity has been answered" in {

          val answers =
            emptyUserAnswers
              .set(LodgingPersonTypePage, LodgingPersonType.Representative).success.value
              .set(CarrierIdentityPage, GBEORI).success.value

          LodgingPersonTypePage.navigate(waypoints, answers)
            .mustEqual(routes.CheckPredecController.onPageLoad(EmptyWaypoints, answers.lrn))
        }

        "to Carrier Identity when it has not been answered" in {

          val answers = emptyUserAnswers.set(LodgingPersonTypePage, LodgingPersonType.Representative).success.value

          LodgingPersonTypePage.navigate(waypoints, answers)
            .mustEqual(routes.CarrierIdentityController.onPageLoad(waypoints, answers.lrn))
        }
      }

      "and the answer is Carrier" - {

        "to Check Predec with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(LodgingPersonTypePage, LodgingPersonType.Carrier).success.value

          LodgingPersonTypePage.navigate(waypoints, answers)
            .mustEqual(routes.CheckPredecController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }
    }

    "must not alter the user's answers when the answer is Representative" in {

      val answers =
        emptyUserAnswers
          .set(CarrierIdentityPage, GBEORI).success.value
          .set(CarrierEORIPage, GbEori("123456789000")).success.value
          .set(CarrierNamePage, "name").success.value
          .set(CarrierAddressPage, Address("street", "town", "postcode", Country("GB", "United Kingdom"))).success.value

      val result = answers.set(LodgingPersonTypePage, LodgingPersonType.Representative).success.value

      result.get(CarrierIdentityPage).value mustEqual GBEORI
      result.get(CarrierEORIPage).value mustEqual GbEori("123456789000")
      result.get(CarrierNamePage).value mustEqual "name"
      result.get(CarrierAddressPage).value mustEqual Address("street", "town", "postcode", Country("GB", "United Kingdom"))
    }

    "must remove Carrier identity, EORI, name and address when the answer is Carrier" in {

      val answers =
        emptyUserAnswers
          .set(CarrierIdentityPage, GBEORI).success.value
          .set(CarrierEORIPage, GbEori("123456789000")).success.value
          .set(CarrierNamePage, "name").success.value
          .set(CarrierAddressPage, Address("street", "town", "postcode", Country("GB", "United Kingdom"))).success.value

      val result = answers.set(LodgingPersonTypePage, LodgingPersonType.Carrier).success.value

      result.get(CarrierIdentityPage) must not be defined
      result.get(CarrierEORIPage) must not be defined
      result.get(CarrierNamePage) must not be defined
      result.get(CarrierAddressPage) must not be defined
    }
  }
}
