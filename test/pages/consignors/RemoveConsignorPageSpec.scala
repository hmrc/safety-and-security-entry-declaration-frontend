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
import controllers.consignors.routes
import models.{GbEori, Index}
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours
import queries.consignors.ConsignorKeyQuery

class RemoveConsignorPageSpec extends SpecBase with PageBehaviours {

  "RemoveConsignorPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Consignor when there is still at least one consignor in the user's answers" in {

        val answers =
          emptyUserAnswers
            .set(ConsignorEORIPage(index), GbEori("123456789000")).success.value
            .set(ConsignorKeyQuery(Index(0)), 1).success.value

        RemoveConsignorPage(index).navigate(waypoints, answers)
          .mustEqual(routes.AddConsignorController.onPageLoad(waypoints, answers.lrn))
      }

      "to Consignor Identity with index 0 when there are no consignors left" in {

        RemoveConsignorPage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.ConsignorIdentityController.onPageLoad(waypoints, emptyUserAnswers.lrn, Index(0)))
      }
    }
  }
}
