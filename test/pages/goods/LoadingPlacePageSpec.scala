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
import models.{Index, PlaceOfUnloading}
import org.scalacheck.Arbitrary.arbitrary
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours
import pages.routedetails.PlaceOfUnloadingPage

class LoadingPlacePageSpec extends SpecBase with PageBehaviours {

  "LoadingPlacePage" - {

    val placeOfUnloading1 = arbitrary[PlaceOfUnloading].sample.value
    val placeOfUnloading2 = arbitrary[PlaceOfUnloading].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Unloading Place when there are more than one places of unloading" - {

        val answers =
          emptyUserAnswers
            .set(PlaceOfUnloadingPage(Index(0)), placeOfUnloading1).success.value
            .set(PlaceOfUnloadingPage(Index(1)), placeOfUnloading2).success.value

        LoadingPlacePage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.UnloadingPlaceController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to wherever Unloading Place navigates to when there is one place of unloading" in {

        val answers = emptyUserAnswers.set(PlaceOfUnloadingPage(Index(0)), placeOfUnloading1).success.value

        LoadingPlacePage(index).navigate(waypoints, answers)
          .mustEqual(UnloadingPlacePage(index).navigate(waypoints, answers))
      }
    }
  }
}
