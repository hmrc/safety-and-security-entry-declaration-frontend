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

package pages.consignees

import base.SpecBase
import controllers.consignees.routes
import models.NormalMode
import pages.{Waypoints, EmptyWaypoints}
import pages.behaviours.PageBehaviours

class NotifiedPartyEORIPageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyEORIPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Check Notified Party" in {

        NotifiedPartyEORIPage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(
            routes.CheckNotifiedPartyController.onPageLoad(waypoints, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current waypoint is AddNotifiedParty" - {

      val waypoints = Waypoints(List(AddNotifiedPartyPage.waypoint((NormalMode))))

      "to Check Notified Party" in {

        NotifiedPartyEORIPage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(
            routes.CheckNotifiedPartyController.onPageLoad(waypoints, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current waypoint is CheckNotifiedParty" - {

      val waypoints = Waypoints(List(CheckNotifiedPartyPage(index).waypoint))

      "to Check Notified Party with the current waypoint removed" in {

        NotifiedPartyEORIPage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(
            routes.CheckNotifiedPartyController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn, index)
          )
      }
    }
  }
}
