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
import models.{CheckMode, NormalMode}
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours

class ConsignorAddressPageSpec extends SpecBase with PageBehaviours {

  "ConsignorAddressPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints
      
      "to Check Consignor" in {

        ConsignorAddressPage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.CheckConsignorController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Add Consignor" - {

      val waypoints = Waypoints(List(AddConsignorPage.waypoint(NormalMode)))

      "to Check consignor" in {

        ConsignorAddressPage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.CheckConsignorController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Check Consignor" - {

      val waypoints = Waypoints(List(CheckConsignorPage(index).waypoint))

      "to Check Consignor with the current waypoint removed" in {
        ConsignorAddressPage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.CheckConsignorController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn, index))
      }
    }
  }
}
