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

package pages.transport

import base.SpecBase
import controllers.routes
import controllers.transport.{routes => transportRoutes}
import models.{CheckMode, NormalMode, TransportMode}
import pages.behaviours.PageBehaviours

class TransportModePageSpec extends SpecBase with PageBehaviours {

  "TransportModePage" - {

//    "must navigate in Normal Mode" - {
//
//      "when the user chooses Roro accompanied" - {
//
//        "to nationality of transport" in {
//
//          val answers = emptyUserAnswers.set(TransportModePage, TransportMode.RoroAccompanied).success.value
//
//          TransportModePage.navigate(NormalMode, answers)
//            .mustEqual(transportRoutes.NationalityOfTransportController.onPageLoad(NormalMode, answers.lrn))
//        }
//      }
//
//      "when the user chooses Roro unaccompanied" - {
//
//        "to nationality of transport" in {
//
//          val answers = emptyUserAnswers.set(TransportModePage, TransportMode.RoroUnaccompanied).success.value
//
//          TransportModePage.navigate(NormalMode, answers)
//            .mustEqual(transportRoutes.NationalityOfTransportController.onPageLoad(NormalMode, answers.lrn))
//        }
//      }
//
//      "when the user chooses Road" - {
//
//        "to nationality of transport" in {
//
//          val answers = emptyUserAnswers.set(TransportModePage, TransportMode.Road).success.value
//
//          TransportModePage.navigate(NormalMode, answers)
//            .mustEqual(transportRoutes.NationalityOfTransportController.onPageLoad(NormalMode, answers.lrn))
//        }
//      }
//
//      "when the user chooses Air" - {
//
//        "to air identity" in {
//
//          val answers = emptyUserAnswers.set(TransportModePage, TransportMode.Air).success.value
//
//          TransportModePage.navigate(NormalMode, answers)
//            .mustEqual(transportRoutes.AirIdentityController.onPageLoad(NormalMode, answers.lrn))
//        }
//      }
//
//      "when the user chooses Rail" - {
//
//        "to rail identity" in {
//
//          val answers = emptyUserAnswers.set(TransportModePage, TransportMode.Rail).success.value
//
//          TransportModePage.navigate(NormalMode, answers)
//            .mustEqual(transportRoutes.RailIdentityController.onPageLoad(NormalMode, answers.lrn))
//        }
//      }
//
//      "when the user chooses Maritime" - {
//
//        "to maritime identity" in {
//
//          val answers = emptyUserAnswers.set(TransportModePage, TransportMode.Maritime).success.value
//
//          TransportModePage.navigate(NormalMode, answers)
//            .mustEqual(transportRoutes.MaritimeIdentityController.onPageLoad(NormalMode, answers.lrn))
//        }
//      }
//    }
//
//    "must navigate in Check Mode" - {
//
//      "to Check Your Answers" in {
//
//        TransportModePage
//          .navigate(CheckMode, emptyUserAnswers)
//          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
//      }
//    }
  }
}
