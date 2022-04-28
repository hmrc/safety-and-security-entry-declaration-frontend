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
import models.{GbEori, Index, NormalMode, TraderIdentity}
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}
import queries.consignees._

class AddAnyNotifiedPartiesPageSpec extends SpecBase with PageBehaviours {

  "AddAnyNotifiedPartiesPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Notified Party Identity when the answer is yes" in {

        val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, true).success.value

        AddAnyNotifiedPartiesPage.navigate(waypoints, answers)
          .mustEqual(routes.NotifiedPartyIdentityController.onPageLoad(waypoints, answers.lrn, Index(0)))
      }

      "to Check Consignees and Notified Parties when the answer is no" in {

        val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, false).success.value

        AddAnyNotifiedPartiesPage.navigate(waypoints, answers)
          .mustEqual(routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(waypoints, answers.lrn))
      }
    }

    "must navigate when the current waypoint is CheckConsigneesAndNotifiedParties" - {

      val waypoints = Waypoints(List(CheckConsigneesAndNotifiedPartiesPage.waypoint))

      "when the answer is yes" - {

        "and there are already some notified parties" - {

          "to Check Consignees and Notified Parties with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(NotifiedPartyIdentityPage(index), TraderIdentity.GBEORI).success.value
                .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
                .set(NotifiedPartyKeyQuery(index), 1).success.value
                .set(AddAnyNotifiedPartiesPage, true).success.value

            AddAnyNotifiedPartiesPage.navigate(waypoints, answers)
              .mustEqual(routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(EmptyWaypoints, answers.lrn))
          }
        }

        "and there are no notified parties" - {

          "to Notified Party for index 0 with Add Notified Party added to the waypoints" in {

            val answers =
              emptyUserAnswers
                .set(AddAnyNotifiedPartiesPage, true).success.value

            val expectedWaypoints = waypoints.setNextWaypoint(AddNotifiedPartyPage.waypoint(NormalMode))

            AddAnyNotifiedPartiesPage.navigate(waypoints, answers)
              .mustEqual(routes.NotifiedPartyIdentityController.onPageLoad(expectedWaypoints, answers.lrn, index))
          }
        }

        "and notified parties have been added then removed so there are none left" - {

          "to Notified Party for index 0 with Add Notified Party added to the waypoints" in {

            val answers =
              emptyUserAnswers
                .set(NotifiedPartyIdentityPage(index), TraderIdentity.GBEORI).success.value
                .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
                .set(NotifiedPartyKeyQuery(index), 1).success.value
                .set(AddAnyNotifiedPartiesPage, true).success.value
                .remove(NotifiedPartyQuery(Index(0))).success.value

            val expectedWaypoints = waypoints.setNextWaypoint(AddNotifiedPartyPage.waypoint(NormalMode))

            AddAnyNotifiedPartiesPage.navigate(waypoints, answers)
              .mustEqual(routes.NotifiedPartyIdentityController.onPageLoad(expectedWaypoints, answers.lrn, index))
          }
        }
      }

      "when the answer is no" - {

        "and there are some consignees" - {

          "to Check Consignees and Notified Parties with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(ConsigneeIdentityPage(index), TraderIdentity.GBEORI).success.value
                .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value
                .set(ConsigneeKeyQuery(index), 1).success.value
                .set(AddAnyNotifiedPartiesPage, false).success.value

            AddAnyNotifiedPartiesPage.navigate(waypoints, answers)
              .mustEqual(routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(EmptyWaypoints, answers.lrn))
          }
        }

        "and there are no consignees" - {

          "to Any Consignees Known" in {

            val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, false).success.value

            AddAnyNotifiedPartiesPage.navigate(waypoints, answers)
              .mustEqual(routes.AnyConsigneesKnownController.onPageLoad(waypoints, answers.lrn))
          }
        }

        "and consignees have been added then removed so there are none left" - {

          "to Any Consignees Known" in {

            val answers =
              emptyUserAnswers
                .set(ConsigneeIdentityPage(index), TraderIdentity.GBEORI).success.value
                .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value
                .set(ConsigneeKeyQuery(index), 1).success.value
                .set(AddAnyNotifiedPartiesPage, false).success.value
                .remove(ConsigneeQuery(Index(0))).success.value

            AddAnyNotifiedPartiesPage.navigate(waypoints, answers)
              .mustEqual(routes.AnyConsigneesKnownController.onPageLoad(waypoints, answers.lrn))
          }
        }
      }
    }

    "must not alter the user's answers when the answer is yes" in {

      val answers =
        emptyUserAnswers
          .set(NotifiedPartyIdentityPage(index), TraderIdentity.GBEORI).success.value
          .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
          .set(NotifiedPartyKeyQuery(index), 1).success.value
          .set(AddAnyNotifiedPartiesPage, true).success.value

      val result = AddAnyNotifiedPartiesPage.cleanup(Some(true), answers).success.value

      result mustEqual answers
    }

    "must remove all notified parties when the answer is no" in {

      val answers =
        emptyUserAnswers
          .set(NotifiedPartyIdentityPage(index), TraderIdentity.GBEORI).success.value
          .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
          .set(NotifiedPartyKeyQuery(index), 1).success.value
          .set(AddAnyNotifiedPartiesPage, false).success.value

      val result = AddAnyNotifiedPartiesPage.cleanup(Some(false), answers).success.value

      result mustEqual answers.remove(AllNotifiedPartiesQuery).success.value
    }
  }
}
