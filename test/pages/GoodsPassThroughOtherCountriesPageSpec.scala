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
import models.{CheckMode, Index, NormalMode}
import pages.behaviours.PageBehaviours

class GoodsPassThroughOtherCountriesPageSpec extends SpecBase with PageBehaviours {

  "GoodsPassThroughOtherCountriesPage" - {

    beRetrievable[Boolean](GoodsPassThroughOtherCountriesPage)

    beSettable[Boolean](GoodsPassThroughOtherCountriesPage)

    beRemovable[Boolean](GoodsPassThroughOtherCountriesPage)

    "must navigate in Normal Mode" - {

      "to Country En Route when the answer is yes" in {

        val answers = emptyUserAnswers.set(GoodsPassThroughOtherCountriesPage, true).success.value

        GoodsPassThroughOtherCountriesPage.navigate(NormalMode, answers)
          .mustEqual(routes.CountryEnRouteController.onPageLoad(NormalMode, answers.lrn, Index(0)))
      }

      "to Customs Office of First Entry when the answer is no" in {

        val answers = emptyUserAnswers.set(GoodsPassThroughOtherCountriesPage, false).success.value

        GoodsPassThroughOtherCountriesPage.navigate(NormalMode, answers)
          .mustEqual(routes.CustomsOfficeOfFirstEntryController.onPageLoad(NormalMode, answers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        GoodsPassThroughOtherCountriesPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
