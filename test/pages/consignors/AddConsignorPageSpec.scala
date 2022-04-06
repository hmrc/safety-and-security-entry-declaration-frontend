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

package pages.consignors

import base.SpecBase
import controllers.consignors.{routes => consignorRoutes}
import controllers.routes
import models.{GbEori, Index, NormalMode}
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours
import queries.consignors.ConsignorKeyQuery

class AddConsignorPageSpec extends SpecBase with PageBehaviours {

  "AddConsignorPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Consignor Identity for the next index when the answer is yes" in {

        val answers =
          emptyUserAnswers
            .set(ConsignorEORIPage(Index(0)), GbEori("123456789000")).success.value
            .set(ConsignorKeyQuery(Index(0)), 1).success.value
            .set(AddConsignorPage, true).success.value

        AddConsignorPage.navigate(waypoints, answers)
          .mustEqual(consignorRoutes.ConsignorIdentityController.onPageLoad(waypoints, answers.lrn, Index(1)))
      }

      "to Task List when the answer is no" in {

        val answers = emptyUserAnswers.set(AddConsignorPage, false).success.value

        AddConsignorPage.navigate(waypoints, answers)
          .mustEqual(routes.TaskListController.onPageLoad(answers.lrn))
      }
    }
  }
}