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
import models.{ConsigneeIdentity, GbEori, Index, NormalMode, NotifiedPartyIdentity}
import pages.{Waypoints, EmptyWaypoints}
import pages.behaviours.PageBehaviours
import queries.consignees.{AllConsigneesQuery, ConsigneeKeyQuery, NotifiedPartyKeyQuery}

class AnyConsigneesKnownPageSpec extends SpecBase with PageBehaviours {

  "AnyConsigneeKnownPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Consignee Identity when answer is yes" in {
        val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, true).success.value

        AnyConsigneesKnownPage
          .navigate(waypoints, answers)
          .mustEqual(routes.ConsigneeIdentityController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Notified Party Identity when answer is no" in {
        val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, false).success.value

        AnyConsigneesKnownPage
          .navigate(waypoints, answers)
          .mustEqual(
            routes.NotifiedPartyIdentityController.onPageLoad(waypoints, answers.lrn, index)
          )
      }
    }

    "must navigate when the current waypoint is Check Consignees and Notified Parties" - {

      val waypoints = Waypoints(List(CheckConsigneesAndNotifiedPartiesPage.waypoint))
      
      "and the answer is yes" - {

        "and there are some consignees" - {

          "to Check Consignees and Notified Parties with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(AnyConsigneesKnownPage, true).success.value
                .set(ConsigneeIdentityPage(Index(0)), ConsigneeIdentity.GBEORI).success.value
                .set(ConsigneeEORIPage(Index(0)), GbEori("123456789000")).success.value
                .set(ConsigneeKeyQuery(Index(0)), 1).success.value

            AnyConsigneesKnownPage.navigate(waypoints, answers)
              .mustEqual(routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(waypoints.pop, answers.lrn))
          }
        }

        "and there are no consignees" - {

          "to Consignee Identity for index 0 with AddConsignee added to the waypoints" in {

            val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, true).success.value
            val expectedWaypoints = waypoints.push(AddConsigneePage.waypoint(NormalMode))

            AnyConsigneesKnownPage.navigate(waypoints, answers)
              .mustEqual(routes.ConsigneeIdentityController.onPageLoad(expectedWaypoints, answers.lrn, Index(0)))
          }
        }
      }
      
      "and the answer is no" - {
        
        "and there are some notified parties" - {

          "to Check Consignees and Notified Parties with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(AnyConsigneesKnownPage, false).success.value
                .set(NotifiedPartyIdentityPage(Index(0)), NotifiedPartyIdentity.GBEORI).success.value
                .set(NotifiedPartyEORIPage(Index(0)), GbEori("123456789000")).success.value
                .set(NotifiedPartyKeyQuery(Index(0)), 1).success.value

            AnyConsigneesKnownPage.navigate(waypoints, answers)
              .mustEqual(routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(waypoints.pop, answers.lrn))
          }
        }

        "and there are no notified parties" - {

          "to Notified Party Identity for index 0" in {

            val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, false).success.value
            val expectedWaypoints = waypoints.push(AddNotifiedPartyPage.waypoint(NormalMode))

            AnyConsigneesKnownPage.navigate(waypoints, answers)
              .mustEqual(routes.NotifiedPartyIdentityController.onPageLoad(expectedWaypoints, answers.lrn, Index(0)))
          }
        }
      }
    }

    "must not alter the user's answers when the answer is yes" in {

      val answers =
        emptyUserAnswers
          .set(AnyConsigneesKnownPage, true).success.value
          .set(ConsigneeIdentityPage(Index(0)), ConsigneeIdentity.GBEORI).success.value
          .set(ConsigneeEORIPage(Index(0)), GbEori("123456789000")).success.value
          .set(ConsigneeKeyQuery(Index(0)), 1).success.value

      val result = AnyConsigneesKnownPage.cleanup(Some(true), answers).success.value

      result mustEqual answers
    }

    "must remove any consignees when the answer is no" in {

      val answers =
        emptyUserAnswers
          .set(AnyConsigneesKnownPage, false).success.value
          .set(ConsigneeIdentityPage(Index(0)), ConsigneeIdentity.GBEORI).success.value
          .set(ConsigneeEORIPage(Index(0)), GbEori("123456789000")).success.value
          .set(ConsigneeKeyQuery(Index(0)), 1).success.value

      val result = AnyConsigneesKnownPage.cleanup(Some(false), answers).success.value

      result mustEqual answers.remove(AllConsigneesQuery).success.value
    }
  }
}
