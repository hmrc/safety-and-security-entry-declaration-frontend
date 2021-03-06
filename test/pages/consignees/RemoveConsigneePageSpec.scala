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
import models.{GbEori, Index}
import pages.{Waypoints, EmptyWaypoints}
import pages.behaviours.PageBehaviours
import queries.consignees.ConsigneeKeyQuery

class RemoveConsigneePageSpec extends SpecBase with PageBehaviours {

  "RemoveConsigneePage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Consignee when there is still at least one consignee in the user's answers" in {

        val answers =
          emptyUserAnswers
            .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value
            .set(ConsigneeKeyQuery(Index(0)), 1).success.value

        RemoveConsigneePage(index).navigate(waypoints, answers)
          .mustEqual(routes.AddConsigneeController.onPageLoad(waypoints, answers.lrn))
      }

      "to Consignee Known when there are no consignees left" in {

        RemoveConsigneePage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AnyConsigneesKnownController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Consignees and Notified Parties" - {

      val waypoints = Waypoints(List(CheckConsigneesAndNotifiedPartiesPage.waypoint))

      "to Add Consignee when there is still at least one consignee in the user's answers" in {

        val answers =
          emptyUserAnswers
            .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value
            .set(ConsigneeKeyQuery(Index(0)), 1).success.value

        RemoveConsigneePage(index).navigate(waypoints, answers)
          .mustEqual(routes.AddConsigneeController.onPageLoad(waypoints, answers.lrn))
      }

      "to Consignee Known when there are no consignees left" in {

        RemoveConsigneePage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AnyConsigneesKnownController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }
  }
}
