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

package pages.routedetails

import base.SpecBase
import controllers.routedetails.{routes => routedetailsRoutes}
import controllers.routes
import models.{CheckMode, Country, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.routedetails

class RemoveCountryEnRoutePageSpec extends SpecBase with PageBehaviours {

  "RemoveCountryEnRoutePage" - {

    "must navigate in Normal Mode" - {

      "to Add Country En Route when there is at least one country in user answers" in {

        val answers = emptyUserAnswers
          .set(CountryEnRoutePage(Index(0)), arbitrary[Country].sample.value)
          .success
          .value

        RemoveCountryEnRoutePage(index)
          .navigate(NormalMode, answers)
          .mustEqual(routedetailsRoutes.AddCountryEnRouteController.onPageLoad(NormalMode, answers.lrn))
      }

      "to Goods Pass Through Other Countries when there are no countries in user answers" in {

        routedetails.RemoveCountryEnRoutePage(index)
          .navigate(NormalMode, emptyUserAnswers)
          .mustEqual(
            routedetailsRoutes.GoodsPassThroughOtherCountriesController
              .onPageLoad(NormalMode, emptyUserAnswers.lrn)
          )
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        routedetails.RemoveCountryEnRoutePage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
