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
import models.CheckMode
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class ItemContainerNumberPageSpec extends SpecBase with PageBehaviours {

  "ItemContainerNumberPage" - {

    "must navigate when there are no waypoints" - {
      
      val waypoints = EmptyWaypoints

      "to AddItem list" in {

        ItemContainerNumberPage(index,index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(goodsRoutes.AddItemContainerNumberController.onPageLoad(waypoints,emptyUserAnswers.lrn,index))
      }
    }

    "must navigate when the current waypoint is Add Container" - {

      val waypoints = Waypoints(List(AddItemContainerNumberPage(index).waypoint(CheckMode)))

      "to Add Container with the current waypoint removed" in {

        GoodsDescriptionPage(index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(goodsRoutes.AddItemContainerNumberController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn, index))
      }
    }
  }
}
