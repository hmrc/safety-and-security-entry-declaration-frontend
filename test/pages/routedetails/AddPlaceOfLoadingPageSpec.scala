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
import models.{CheckMode, Index, NormalMode, PlaceOfLoading}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class AddPlaceOfLoadingPageSpec extends SpecBase with PageBehaviours {

  "AddPlaceOfLoadingPage" - {

    "must navigate in Normal Mode" - {

      "to Place of Loading with an index equal tot he number of places of loading we have details for when the answer is yes" in {

        val answers =
          emptyUserAnswers
            .set(PlaceOfLoadingPage(index), arbitrary[PlaceOfLoading].sample.value).success.value

        AddPlaceOfLoadingPage.navigate(NormalMode, answers, addAnother = true)
          .mustEqual(routedetailsRoutes.PlaceOfLoadingController.onPageLoad(NormalMode, answers.lrn, Index(1)))
      }

      "to Goods Pass Through Other Countries when the answer is no" in {

        AddPlaceOfLoadingPage.navigate(NormalMode, emptyUserAnswers, addAnother = false)
          .mustEqual(routedetailsRoutes.GoodsPassThroughOtherCountriesController.onPageLoad(NormalMode, emptyUserAnswers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddPlaceOfLoadingPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
