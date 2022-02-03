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

package navigation

import base.SpecBase
import controllers.routes
import pages._
import models._

class NavigatorSpec extends SpecBase {

  private val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, NormalMode, UserAnswers("id")) mustBe routes.IndexController.onPageLoad
      }

      "must go from Local Reference Number to Lodging Person Type" in {

        navigator.nextPage(LocalReferenceNumberPage, NormalMode, emptyUserAnswers)
          .mustBe(routes.LodgingPersonTypeController.onPageLoad(NormalMode))
      }

      "must go from Lodging Person Type to Gross Weight" in {

        navigator.nextPage(LodgingPersonTypePage, NormalMode, emptyUserAnswers)
          .mustBe(routes.GrossWeightController.onPageLoad(NormalMode))
      }

      "must go from Gross Weight" - {
        "to Transport Mode when the user enters Per item" in {

          val answers = emptyUserAnswers.set(GrossWeightPage, GrossWeight.PerItem).success.value
          navigator.nextPage(GrossWeightPage, NormalMode, answers)
            .mustBe(routes.TransportModeController.onPageLoad(NormalMode))
        }

        "to Total Gross weight when the user enters overall" in {

          val answers = emptyUserAnswers.set(GrossWeightPage, GrossWeight.Overall).success.value
          navigator.nextPage(GrossWeightPage, NormalMode, answers)
            .mustBe(routes.TotalGrossWeightController.onPageLoad(NormalMode))
        }

        "to Journey Recovery when nethier overall or per item is made" in {
          navigator.nextPage(GrossWeightPage, NormalMode, emptyUserAnswers)
            .mustBe(routes.JourneyRecoveryController.onPageLoad())
        }
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }
  }
}
