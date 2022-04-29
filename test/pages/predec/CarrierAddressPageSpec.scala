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
import models.{CheckMode, NormalMode}
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours

class CarrierAddressPageSpec extends SpecBase with PageBehaviours {

  "CarrierAddressPage" - {

//    "must navigate when there are no waypoints" - {
//
//      val waypoints = EmptyWaypoints
//
//      "to Check Carrier" in {
//
//        CarrierAddressPage(index)
//          .navigate(waypoints, emptyUserAnswers)
//          .mustEqual(routes.CheckCarrierController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
//      }
//    }
//
//    "must navigate when the current waypoint is Add Carrier" - {
//
//      val waypoints = Waypoints(List(AddCarrierPage.waypoint(NormalMode)))
//
//      "to Check carrier" in {
//
//        CarrierAddressPage(index)
//          .navigate(waypoints, emptyUserAnswers)
//          .mustEqual(routes.CheckCarrierController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
//      }
//    }
//
//    "must navigate when the current waypoint is Check Carrier" - {
//
//      val waypoints = Waypoints(List(CheckCarrierPage(index).waypoint))
//
//      "to Check Carrier with the current waypoint removed" in {
//        CarrierAddressPage(index)
//          .navigate(waypoints, emptyUserAnswers)
//          .mustEqual(routes.CheckCarrierController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn, index))
//      }
//    }
  }
}
