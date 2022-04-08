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
import controllers.routedetails.routes
import models.{Index, NormalMode, PlaceOfLoading}
import org.scalacheck.Arbitrary.arbitrary
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours

class RemovePlaceOfLoadingPageSpec extends SpecBase with PageBehaviours {

  "RemovePlaceOfLoadingPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Place of Loading with index 0 when there are no places of loading left" in {

        RemovePlaceOfLoadingPage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.PlaceOfLoadingController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }

      "to Add Place of Loading when there is at least one place of loading" in {

        val placeOfLoading = arbitrary[PlaceOfLoading].sample.value
        val answers = emptyUserAnswers.set(PlaceOfLoadingPage(index), placeOfLoading).success.value

        RemovePlaceOfLoadingPage(index).navigate(waypoints, answers)
          .mustEqual(routes.AddPlaceOfLoadingController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Route Details" - {

      val waypoints = Waypoints(List(CheckRouteDetailsPage.waypoint))

      "when there are no places of loading" - {

        "to Place of Loading with index 0 and Add Place of Loading added to the waypoints" in {

          val expectedWaypoints = waypoints.setNextWaypoint(AddPlaceOfLoadingPage.waypoint(NormalMode))

          RemovePlaceOfLoadingPage(index).navigate(waypoints, emptyUserAnswers)
            .mustEqual(routes.PlaceOfLoadingController.onPageLoad(expectedWaypoints, emptyUserAnswers.lrn, Index(0)))
        }
      }

      "when there is at least one place of loading" - {

        "to Add Place of Loading" in {

          val placeOfLoading = arbitrary[PlaceOfLoading].sample.value
          val answers = emptyUserAnswers.set(PlaceOfLoadingPage(index), placeOfLoading).success.value

          RemovePlaceOfLoadingPage(index).navigate(waypoints, answers)
            .mustEqual(routes.AddPlaceOfLoadingController.onPageLoad(waypoints, emptyUserAnswers.lrn))
        }
      }
    }
  }
}
