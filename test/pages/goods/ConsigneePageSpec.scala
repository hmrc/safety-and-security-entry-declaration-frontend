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

package pages.goods

import base.SpecBase
import controllers.goods.{routes => goodsRoutes}
import controllers.routes
import models.{CheckMode, Index, NormalMode, PlaceOfLoading}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.routedetails.PlaceOfLoadingPage

class ConsigneeSpec extends SpecBase with PageBehaviours {

  "ConsigneePage" - {

    beRetrievable[Int](ConsigneePage(index))

    beSettable[Int](ConsigneePage(index))

    beRemovable[Int](ConsigneePage(index))

    val placeOfLoading1 = arbitrary[PlaceOfLoading].sample.value
    val placeOfLoading2 = arbitrary[PlaceOfLoading].sample.value

    "must navigate in Normal Mode" - {

      "to place of loading when there is more than one place of loading" in {

        val answers =
          emptyUserAnswers
            .set(PlaceOfLoadingPage(Index(0)), placeOfLoading1).success.value
            .set(PlaceOfLoadingPage(Index(1)), placeOfLoading2).success.value

        ConsigneePage(index).navigate(NormalMode, answers)
          .mustEqual(goodsRoutes.LoadingPlaceController.onPageLoad(NormalMode, answers.lrn, index))
      }

      "to wherever loading place navigates to when there is one place of loading" in {

        val answers =
          emptyUserAnswers
            .set(PlaceOfLoadingPage(Index(0)), placeOfLoading1).success.value

        ConsigneePage(index).navigate(NormalMode, answers)
          .mustEqual(LoadingPlacePage(Index(0)).navigate(NormalMode, answers))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneePage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
