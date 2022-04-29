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
import models.{Address, Country, NormalMode}
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class CarrierNamePageSpec extends SpecBase with PageBehaviours {

  "CarrierNamePage" - {

//    "must navigate when there are no waypoints" - {
//
//      val waypoints = EmptyWaypoints
//
//      "to carrier address" in {
//
//        CarrierNamePage(index)
//          .navigate(waypoints, emptyUserAnswers)
//          .mustEqual(routes.CarrierAddressController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
//      }
//    }
//
//    "must navigate when the current waypoint is AddCarrier" - {
//
//      val waypoints = Waypoints(List(AddCarrierPage.waypoint(NormalMode)))
//
//      "to carrier address" in {
//
//        CarrierNamePage(index)
//          .navigate(waypoints, emptyUserAnswers)
//          .mustEqual(
//            routes.CarrierAddressController.onPageLoad(waypoints, emptyUserAnswers.lrn, index)
//          )
//      }
//    }
//
//    "must navigate when the current waypoint is Check Carrier" - {
//
//      val waypoints = Waypoints(List(CheckCarrierPage(index).waypoint))
//
//      "and Carrier Address has been answered" - {
//
//        "to Check Carrier with the current waypoint removed" in {
//
//          val answers =
//            emptyUserAnswers
//              .set(CarrierAddressPage(index), Address("street", "city", "AA11 1AA", Country("GB", "United Kingdom")))
//              .success.value
//
//          CarrierNamePage(index).navigate(waypoints, answers)
//            .mustEqual(routes.CheckCarrierController.onPageLoad(EmptyWaypoints, answers.lrn, index))
//        }
//      }
//
//      "and Carrier Address has not been answered" - {
//
//        "to Carrier Address" in {
//
//          CarrierNamePage(index).navigate(waypoints, emptyUserAnswers)
//            .mustEqual(routes.CarrierAddressController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
//        }
//      }
//    }
  }
}
