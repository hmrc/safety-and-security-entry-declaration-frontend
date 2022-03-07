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
import models.{CheckMode, NormalMode, PlaceOfLoading}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class RemovePlaceOfLoadingPageSpec extends SpecBase with PageBehaviours {

  "RemovePlaceOfLoadingPage" - {

    "must navigate in Normal Mode" - {

      "to Place of Loading with index 0 when there are no places of loading left" in {

        RemovePlaceOfLoadingPage(index).navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routedetailsRoutes.PlaceOfLoadingController.onPageLoad(NormalMode, emptyUserAnswers.lrn, index))
      }

      "to Add Place of Loading with index 0 when there is at least one place of loading left" in {

        val placeOfLoading = arbitrary[PlaceOfLoading].sample.value
        val answers = emptyUserAnswers.set(PlaceOfLoadingPage(index), placeOfLoading).success.value

        RemovePlaceOfLoadingPage(index).navigate(NormalMode, answers)
          .mustEqual(routedetailsRoutes.AddPlaceOfLoadingController.onPageLoad(NormalMode, emptyUserAnswers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        RemovePlaceOfLoadingPage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
