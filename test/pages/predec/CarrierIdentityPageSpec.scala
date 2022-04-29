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
import models.{Address, Country, GbEori, NormalMode}
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class CarrierIdentityPageSpec extends SpecBase with PageBehaviours {

  "CarrierIdentityPage" - {

    "must navigate when there are no waypoints" - {

//      val waypoints = EmptyWaypoints
//
//      "to Carrier EORI when answered `gb eori`" in {
//        val answers = emptyUserAnswers.set(CarrierIdentityPage(index), GBEORI).success.value
//
//        CarrierIdentityPage(index)
//          .navigate(waypoints, answers)
//          .mustEqual(routes.CarrierEORIController.onPageLoad(waypoints, answers.lrn, index))
//      }
//
//      "to Carrier Name when answered `name & address`" in {
//        val answers = emptyUserAnswers.set(CarrierIdentityPage(index), NameAddress).success.value
//
//        CarrierIdentityPage(index)
//          .navigate(waypoints, answers)
//          .mustEqual(routes.CarrierNameController.onPageLoad(waypoints, answers.lrn, index))
//      }
//    }
//
//    "must navigate when the current waypoint is AddCarrier" - {
//
//      val waypoints = Waypoints(List(AddCarrierPage.waypoint((NormalMode))))
//
//      "to Carrier EORI when answered `gb eori`" in {
//        val answers = emptyUserAnswers.set(CarrierIdentityPage(index), GBEORI).success.value
//
//        CarrierIdentityPage(index)
//          .navigate(waypoints, answers)
//          .mustEqual(routes.CarrierEORIController.onPageLoad(waypoints, answers.lrn, index))
//      }
//
//      "to Carrier Name when answered `name & address`" in {
//        val answers = emptyUserAnswers.set(CarrierIdentityPage(index), NameAddress).success.value
//
//        CarrierIdentityPage(index)
//          .navigate(waypoints, answers)
//          .mustEqual(routes.CarrierNameController.onPageLoad(waypoints, answers.lrn, index))
//      }
//    }
//
//    "must navigate when the current waypoint is Check Carrier" - {
//
//      val waypoints = Waypoints(List(CheckCarrierPage(index).waypoint))
//
//      "when the answer is GB EORI" - {
//
//        "and Carrier EORI is already answered" - {
//
//          "to Check Carrier with the current waypoint removed" in {
//
//            val answers =
//              emptyUserAnswers
//                .set(CarrierEORIPage(index), GbEori("123456789000")).success.value
//                .set(CarrierIdentityPage(index), GBEORI).success.value
//
//            CarrierIdentityPage(index).navigate(waypoints, answers)
//              .mustEqual(routes.CheckCarrierController.onPageLoad(EmptyWaypoints, answers.lrn, index))
//          }
//        }
//
//        "and Carrier EORI has not been answered" - {
//
//          "to Carrier EORI" in {
//
//            val answers = emptyUserAnswers.set(CarrierIdentityPage(index), GBEORI).success.value
//
//            CarrierIdentityPage(index).navigate(waypoints, answers)
//              .mustEqual(routes.CarrierEORIController.onPageLoad(waypoints, answers.lrn, index))
//          }
//        }
//      }
//
//      "when the answer is Name and Address" - {
//
//        "and Carrier Name is already answered" - {
//
//          "to Check Carrier with the current waypoint removed" in {
//
//            val answers =
//              emptyUserAnswers
//                .set(CarrierNamePage(index), "Name").success.value
//                .set(CarrierIdentityPage(index), NameAddress).success.value
//
//            CarrierIdentityPage(index).navigate(waypoints, answers)
//              .mustEqual(routes.CheckCarrierController.onPageLoad(EmptyWaypoints, answers.lrn, index))
//          }
//        }
//
//        "and Carrier Name has not been answered" - {
//
//          "to Carrier Name" in {
//
//            val answers = emptyUserAnswers.set(CarrierIdentityPage(index), NameAddress).success.value
//
//            CarrierIdentityPage(index).navigate(waypoints, answers)
//              .mustEqual(routes.CarrierNameController.onPageLoad(waypoints, answers.lrn, index))
//          }
//        }
//      }
//    }
//
//    "must remove Carrier EORI when the answer is Name and Address" in {
//
//      val answers =
//        emptyUserAnswers
//          .set(CarrierIdentityPage(index), NameAddress).success.value
//          .set(CarrierEORIPage(index), GbEori("123456789000")).success.value
//
//      val result = CarrierIdentityPage(index).cleanup(Some(NameAddress), answers).success.value
//
//      result mustEqual answers.remove(CarrierEORIPage(index)).success.value
//    }
//
//    "must remove Carrier Name and Carrier Address when the answer is EORI" in {
//
//      val answers =
//        emptyUserAnswers
//          .set(CarrierIdentityPage(index), GBEORI).success.value
//          .set(CarrierNamePage(index), "name").success.value
//          .set(CarrierAddressPage(index), Address("street", "town", "post code", Country("GB", "United Kingdom"))).success.value
//
//      val result = CarrierIdentityPage(index).cleanup(Some(GBEORI), answers).success.value
//
//      result.mustEqual(
//        answers
//          .remove(CarrierNamePage(index)).success.value
//          .remove(CarrierAddressPage(index)).success.value)
    }
  }
}
