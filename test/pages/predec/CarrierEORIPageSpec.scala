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
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours

class CarrierEORIPageSpec extends SpecBase with PageBehaviours {

  "CarrierEORIPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Provide Gross Weight" in {

        CarrierAddressPage.navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.ProvideGrossWeightController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }

      "must navigate when the current waypoint is Check Predec" - {

        val waypoints = Waypoints(List(CheckPredecPage.waypoint))

        "to Check Carrier with the current waypoint removed" in {
          CarrierAddressPage.navigate(waypoints, emptyUserAnswers)
            .mustEqual(routes.CheckPredecController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn))
        }
      }
    }
  }
}
