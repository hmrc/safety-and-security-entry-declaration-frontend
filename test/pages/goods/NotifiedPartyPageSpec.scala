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
import models.{Index, PlaceOfLoading}
import org.scalacheck.Arbitrary.arbitrary
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours
import pages.routedetails.PlaceOfLoadingPage

class NotifiedPartyPageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyPage" - {

    val placeOfLoading1 = arbitrary[PlaceOfLoading].sample.value
    val placeOfLoading2 = arbitrary[PlaceOfLoading].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to place of loading when there is more than one place of loading" in {

        val answers =
          emptyUserAnswers
            .set(PlaceOfLoadingPage(Index(0)), placeOfLoading1).success.value
            .set(PlaceOfLoadingPage(Index(1)), placeOfLoading2).success.value

        NotifiedPartyPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.LoadingPlaceController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to wherever loading place navigates to when there is one place of loading" in {

        val answers =
          emptyUserAnswers
            .set(PlaceOfLoadingPage(Index(0)), placeOfLoading1).success.value

        NotifiedPartyPage(index).navigate(waypoints, answers)
          .mustEqual(LoadingPlacePage(Index(0)).navigate(waypoints, answers))
      }
    }
  }
}
