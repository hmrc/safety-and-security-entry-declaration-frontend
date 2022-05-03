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

class AddAnySealsPageSpec extends SpecBase with PageBehaviours {

  "AddAnySealsPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Seal when the answer is yes" in {

        val answers = emptyUserAnswers.set(AddAnySealsPage, true).success.value

        AddAnySealsPage.navigate(waypoints, answers)
          .mustEqual(routes.SealController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Check Transport when the answer is no" in {

        val answers = emptyUserAnswers.set(AddAnySealsPage, false).success.value

        AddAnySealsPage.navigate(waypoints, answers)
          .mustEqual(routes.CheckTransportController.onPageLoad(waypoints, answers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Transport" - {

      val waypoints = Waypoints(List(CheckTransportPage.waypoint))

      "when the answer is yes" - {

        "to Seal with Add Seal added to the waypoints when there are no seals in the user's answers" in {

          val answers = emptyUserAnswers.set(AddAnySealsPage, true).success.value

          val expectedWaypoints = waypoints.setNextWaypoint(AddSealPage.waypoint(NormalMode))

          AddAnySealsPage.navigate(waypoints, answers)
            .mustEqual(routes.SealController.onPageLoad(expectedWaypoints, answers.lrn, index))
        }

        "to Check Transport with the current waypoint removed when there is already a seal in the user's answers" in {

          val answers =
            emptyUserAnswers
              .set(SealPage(index), "seal").success.value
              .set(AddAnySealsPage, true).success.value

          AddAnySealsPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "when the answer is no" - {

        "to Check Transport with the current waypoint removed when there is already a seal in the user's answers" in {

          val answers = emptyUserAnswers.set(AddAnySealsPage, false).success.value

          AddAnySealsPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }
    }

    "must not alter the user's answers when the answer is yes" in {

      val answers =
        emptyUserAnswers
          .set(SealPage(Index(0)), "seal1").success.value
          .set(SealPage(Index(1)), "seal2").success.value

      val result = answers.set(AddAnySealsPage, true).success.value

      result.get(SealPage(Index(0))).value mustEqual "seal1"
      result.get(SealPage(Index(1))).value mustEqual "seal2"
    }

    "must remove all seals when the answer is no" in {

      val answers =
        emptyUserAnswers
          .set(SealPage(Index(0)), "seal1").success.value
          .set(SealPage(Index(1)), "seal2").success.value

      val result = answers.set(AddAnySealsPage, false).success.value

      result.get(SealPage(Index(0))) must not be defined
      result.get(SealPage(Index(1))) must not be defined
    }
  }
}
