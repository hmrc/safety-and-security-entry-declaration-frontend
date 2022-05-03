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
import models.Index
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class RemoveSealPageSpec extends SpecBase with PageBehaviours {

  "RemoveSealPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Any Seals when there are no seals left" in {

        RemoveSealPage(Index(0)).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AddAnySealsController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }

      "to Add Seal when there is at least one seal left" in {

        val answers = emptyUserAnswers.set(SealPage(Index(0)), "seal").success.value

        RemoveSealPage(Index(0)).navigate(waypoints, answers)
          .mustEqual(routes.AddSealController.onPageLoad(waypoints, answers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Transport" - {

      val waypoints = Waypoints(List(CheckTransportPage.waypoint))

      "to Add Any Seals when there are no seals left" in {

        RemoveSealPage(Index(0)).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AddAnySealsController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }

      "to Add Seal when there is at least one seal left" in {

        val answers = emptyUserAnswers.set(SealPage(Index(0)), "seal").success.value

        RemoveSealPage(Index(0)).navigate(waypoints, answers)
          .mustEqual(routes.AddSealController.onPageLoad(waypoints, answers.lrn))
      }
    }
  }
}
