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
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class DangerousGoodCodePageSpec extends SpecBase with PageBehaviours {

  "DangerousGoodCodePage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Payment Method" in {

        DangerousGoodCodePage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(goodsRoutes.PaymentMethodController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Check Goods Item" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "to Check Goods Item with the current waypoint removed" in {

        DangerousGoodCodePage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn, index))
      }
    }
  }
}
