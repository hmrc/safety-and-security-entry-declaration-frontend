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

package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, GrossWeight, NormalMode}
import pages.behaviours.PageBehaviours

class GrossWeightPageSpec extends SpecBase with PageBehaviours {

  "GrossWeightPage" - {

    beRetrievable[GrossWeight](GrossWeightPage)

    beSettable[GrossWeight](GrossWeightPage)

    beRemovable[GrossWeight](GrossWeightPage)

    "must navigate in Normal Mode" - {

      "to Transport Mode when the answer is Per Item" in {

        val answers = emptyUserAnswers.set(GrossWeightPage, GrossWeight.PerItem).success.value

        GrossWeightPage
          .navigate(NormalMode, answers)
          .mustEqual(routes.TransportModeController.onPageLoad(NormalMode, answers.lrn))
      }

      "to Total Gross Weight when the answer is Overall" in {

        val answers = emptyUserAnswers.set(GrossWeightPage, GrossWeight.Overall).success.value

        GrossWeightPage
          .navigate(NormalMode, answers)
          .mustEqual(routes.TotalGrossWeightController.onPageLoad(NormalMode, answers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        GrossWeightPage
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
