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
import models.NormalMode
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class PlaceOfUnloadingPageSpec extends SpecBase with PageBehaviours {

  "PlaceOfUnloadingPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Place of Unloading" in {

        PlaceOfUnloadingPage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AddPlaceOfUnloadingController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }

    "must navigate when the current waypoint is Add Place of Unloading" - {

      val waypoints = Waypoints(List(AddPlaceOfUnloadingPage.waypoint(NormalMode)))

      "to Add Place of Unloading with the current waypoint removed" in {

        PlaceOfUnloadingPage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AddPlaceOfUnloadingController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn))
      }
    }
  }
}
