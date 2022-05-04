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
import models.{NormalMode, PlaceOfUnloading}
import org.scalacheck.Arbitrary.arbitrary
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours

class RemovePlaceOfUnloadingPageSpec extends SpecBase with PageBehaviours {

  "RemovePlaceOfUnloadingPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Place of Unloading with index 0 when there are no places of loading left" in {

        RemovePlaceOfUnloadingPage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.PlaceOfUnloadingController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }

      "to Add Place of Unloading with index 0 when there is at least one place of loading left" in {

        val placeOfUnloading = arbitrary[PlaceOfUnloading].sample.value
        val answers = emptyUserAnswers.set(PlaceOfUnloadingPage(index), placeOfUnloading).success.value

        RemovePlaceOfUnloadingPage(index).navigate(waypoints, answers)
          .mustEqual(routes.AddPlaceOfUnloadingController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Route Details" - {

      val waypoints = Waypoints(List(CheckRouteDetailsPage.waypoint))

      "when there are no places of unloading" - {

        "to Place of Unloading with Add Place of Unloading added to the waypoints" in {

          val expectedWaypoints = waypoints.setNextWaypoint(AddPlaceOfUnloadingPage.waypoint(NormalMode))

          RemovePlaceOfUnloadingPage(index).navigate(waypoints, emptyUserAnswers)
            .mustEqual(routes.PlaceOfUnloadingController.onPageLoad(expectedWaypoints, emptyUserAnswers.lrn, index))
        }
      }

      "to Add Place of Unloading when there is at least one place of loading left" in {

        val placeOfUnloading = arbitrary[PlaceOfUnloading].sample.value
        val answers = emptyUserAnswers.set(PlaceOfUnloadingPage(index), placeOfUnloading).success.value

        RemovePlaceOfUnloadingPage(index).navigate(waypoints, answers)
          .mustEqual(routes.AddPlaceOfUnloadingController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }
  }
}
