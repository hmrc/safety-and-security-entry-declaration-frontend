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
import controllers.goods.routes
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours

class RemoveGoodsPageSpec extends SpecBase with PageBehaviours {

  "RemoveGoodsPage" - {

    val commodityCode = "00102030"

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Goods when there is at least one goods item in the user's answers" in {

        val answers = emptyUserAnswers.set(CommodityCodePage(index), commodityCode).success.value

        RemoveGoodsPage(index).navigate(waypoints, answers)
          .mustEqual(routes.AddGoodsController.onPageLoad(waypoints, answers.lrn))
      }

      "to Commodity Code Known for the first index when there are no items in the user's answers" in {

        RemoveGoodsPage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.CommodityCodeKnownController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }
    }
  }
}
