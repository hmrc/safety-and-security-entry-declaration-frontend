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
import controllers.predec.{routes => predecRoutes}
import controllers.routes
import models.{CheckMode, LodgingPersonType, NormalMode}
import pages.behaviours.PageBehaviours

class LodgingPersonTypePageSpec extends SpecBase with PageBehaviours {

  "LodgingPersonTypePage" - {

    "must navigate in Normal Mode" - {
//
//      "to Carrier EORI when the answer is Representative" in {
//
//        val answers = emptyUserAnswers.set(LodgingPersonTypePage, LodgingPersonType.Representative).success.value
//
//        LodgingPersonTypePage
//          .navigate(NormalMode, answers)
//          .mustEqual(predecRoutes.CarrierEORIController.onPageLoad(NormalMode, emptyUserAnswers.lrn))
//      }
//
//      "to Gross Weight when the answer is Carrier" in {
//
//        val answers = emptyUserAnswers.set(LodgingPersonTypePage, LodgingPersonType.Carrier).success.value
//
//        LodgingPersonTypePage
//          .navigate(NormalMode, answers)
//          .mustEqual(predecRoutes.ProvideGrossWeightController.onPageLoad(NormalMode, emptyUserAnswers.lrn))
//      }
//    }
//
//    "must navigate in Check Mode" - {
//
//      "to Check Your Answers" in {
//
//        LodgingPersonTypePage
//          .navigate(CheckMode, emptyUserAnswers)
//          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
//      }
    }
  }
}
