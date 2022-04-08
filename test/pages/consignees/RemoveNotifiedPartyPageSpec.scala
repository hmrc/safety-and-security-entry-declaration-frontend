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
import models.{GbEori, NormalMode}
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours
import queries.consignees.NotifiedPartyKeyQuery

class RemoveNotifiedPartyPageSpec extends SpecBase with PageBehaviours {

  "RemoveNotifiedPartyPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "when there are still notified parties in the user's answers" - {

        "to Add Notified Party" in {
          val answers =
            emptyUserAnswers
              .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
              .set(NotifiedPartyKeyQuery(index), 1).success.value

          RemoveNotifiedPartyPage(index).navigate(waypoints, answers)
            .mustEqual(routes.AddNotifiedPartyController.onPageLoad(waypoints, answers.lrn))
        }
      }

      "when there are no notified parties in the user's answers" - {

        "to Notified Party Identity for Index 0 when the user does not know any consignees" in {

          val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, false).success.value

          RemoveNotifiedPartyPage(index).navigate(waypoints, answers)
            .mustEqual(routes.NotifiedPartyIdentityController.onPageLoad(waypoints, answers.lrn, index))
        }

        "to Add Any Notified Parties when the user knows some consignees" in {

          val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, true).success.value

          RemoveNotifiedPartyPage(index).navigate(waypoints, answers)
            .mustEqual(routes.AddAnyNotifiedPartiesController.onPageLoad(waypoints, answers.lrn))
        }
      }
    }

    "must navigate when the current waypoint is Check Consignees and Notified Parties" - {

      val waypoints = Waypoints(List(CheckConsigneesAndNotifiedPartiesPage.waypoint))

      "when there are still notified parties in the user's answers" - {

        "to Add Notified Party" in {
          val answers =
            emptyUserAnswers
              .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
              .set(NotifiedPartyKeyQuery(index), 1).success.value

          RemoveNotifiedPartyPage(index).navigate(waypoints, answers)
            .mustEqual(routes.AddNotifiedPartyController.onPageLoad(waypoints, answers.lrn))
        }
      }

      "when there are no notified parties in the user's answers" - {

        "to Notified Party Identity with Add Notified Party added to the waypoints when the user does not know any consignees" in {

          val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, false).success.value

          val expectedWaypoints = waypoints.setNextWaypoint(AddNotifiedPartyPage.waypoint(NormalMode))

          RemoveNotifiedPartyPage(index).navigate(waypoints, answers)
            .mustEqual(routes.NotifiedPartyIdentityController.onPageLoad(expectedWaypoints, answers.lrn, index))
        }

        "to Add Any Notified Parties when the user knows some consignees" in {

          val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, true).success.value

          RemoveNotifiedPartyPage(index).navigate(waypoints, answers)
            .mustEqual(routes.AddAnyNotifiedPartiesController.onPageLoad(waypoints, answers.lrn))
        }
      }
    }
  }
}
