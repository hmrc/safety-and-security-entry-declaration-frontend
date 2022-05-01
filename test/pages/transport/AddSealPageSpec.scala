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

class AddSealPageSpec extends SpecBase with PageBehaviours {

  "AddSealPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Seal for the next index when the answer is yes" in {

        val answers =
          emptyUserAnswers
            .set(SealPage(Index(0)), "seal1").success.value
            .set(AddSealPage, true).success.value

        AddSealPage.navigate(waypoints, answers)
          .mustEqual(routes.SealController.onPageLoad(waypoints, answers.lrn, Index(1)))
      }

      "to Check Transport when the answer is no" in {

        val answers =
          emptyUserAnswers
            .set(SealPage(Index(0)), "seal1").success.value
            .set(AddSealPage, false).success.value

        AddSealPage.navigate(waypoints, answers)
          .mustEqual(routes.CheckTransportController.onPageLoad(waypoints, answers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Transport" - {

      val waypoints = Waypoints(List(CheckTransportPage.waypoint))

      "when the answer is yes" - {

        "to Seal for the next index with Add Seal added to the waypoints" in {

          val answers =
            emptyUserAnswers
              .set(SealPage(Index(0)), "seal1").success.value
              .set(AddSealPage, true).success.value

          val expectedWaypoints = waypoints.setNextWaypoint(AddSealPage.waypoint(NormalMode))

          AddSealPage.navigate(waypoints, answers)
            .mustEqual(routes.SealController.onPageLoad(expectedWaypoints, answers.lrn, Index(1)))
        }
      }

      "when the answer is no" - {

        "to Check Transport with the current waypoint removed" in {

          val answers =
            emptyUserAnswers
              .set(SealPage(Index(0)), "seal1").success.value
              .set(AddSealPage, false).success.value

          AddSealPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }
    }
  }
}
