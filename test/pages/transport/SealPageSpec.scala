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
import models.{Index, NormalMode}
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class SealPageSpec extends SpecBase with PageBehaviours {

  "SealPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Seal" in {

        SealPage(Index(0)).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AddSealController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }

    "must navigate when the current waypoint is Add Seal" - {

      val waypoints = Waypoints(List(AddSealPage.waypoint(NormalMode)))

      "to Add Seal with the current waypoint removed" in {

        SealPage(Index(0)).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AddSealController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn))
      }
    }
  }
}
