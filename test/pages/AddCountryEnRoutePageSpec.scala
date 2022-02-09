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
import models.{CheckMode, Country, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary

class AddCountryEnRoutePageSpec extends SpecBase {

  "AddCountryEnRoutePage" - {

    "must navigate in Normal Mode" - {

      "to Country En Route with an index equal to the number of countries we have details for when the answer is yes" in {

        val answers = emptyUserAnswers.set(CountryEnRoutePage(index), arbitrary[Country].sample.value).success.value

        AddCountryEnRoutePage.navigate(NormalMode, answers, addAnother = true)
          .mustEqual(routes.CountryEnRouteController.onPageLoad(NormalMode, answers.lrn, Index(1)))
      }

      "to Customs Office of First Entry when the answer is no" in {

        AddCountryEnRoutePage.navigate(NormalMode, emptyUserAnswers, addAnother = false)
          .mustEqual(routes.CustomsOfficeOfFirstEntryController.onPageLoad(NormalMode, emptyUserAnswers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddCountryEnRoutePage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
