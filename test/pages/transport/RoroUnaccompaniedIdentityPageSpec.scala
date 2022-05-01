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

package pages.transport

import base.SpecBase
import controllers.transport.routes
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours

class RoroUnaccompaniedIdentityPageSpec extends SpecBase with PageBehaviours {

  "RoroUnaccompaniedIdentityPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Any Overall Documents" in {

        RoroUnaccompaniedIdentityPage.navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AnyOverallDocumentsController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Transport" - {

      val waypoints = Waypoints(List(CheckTransportPage.waypoint))

      "to Check Transport with the current waypoint removed" in {

        RoroUnaccompaniedIdentityPage.navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn))
      }
    }
  }
}
