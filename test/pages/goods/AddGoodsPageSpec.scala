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

package pages.goods

import base.SpecBase
import controllers.goods.{routes => goodsRoutes}
import controllers.routes
import models.Index
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours

class AddGoodsPageSpec extends SpecBase with PageBehaviours {

  "AddGoodsPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Initialise Goods Item for the next index if the answer is yes" in {

        val answers =
          emptyUserAnswers
            .set(CommodityCodeKnownPage(Index(0)), true).success.value
            .set(AddGoodsPage, true).success.value

        AddGoodsPage.navigate(waypoints, answers)
          .mustEqual(goodsRoutes.InitialiseGoodsItemController.initialise(waypoints, answers.lrn, Index(1)))
      }

      "to Task List when the answer is no" in {

        val answers =
          emptyUserAnswers
            .set(CommodityCodeKnownPage(Index(0)), true).success.value
            .set(AddGoodsPage, false).success.value

        AddGoodsPage.navigate(waypoints, answers)
          .mustEqual(routes.TaskListController.onPageLoad(answers.lrn))
      }
    }
  }
}
