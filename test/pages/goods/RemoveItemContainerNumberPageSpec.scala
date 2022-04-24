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
import models.Container
import org.scalacheck.Arbitrary.arbitrary
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours

class RemoveItemContainerNumberPageSpec extends SpecBase with PageBehaviours {

  "Remove Item Container Number Page" - {

    val container = arbitrary[Container].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Item container Number when there is still at least one in the user's answers" in {

        val answers =
          emptyUserAnswers
            .set(ItemContainerNumberPage(index, index), container).success.value

        RemoveItemContainerNumberPage(index, index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.AddItemContainerNumberController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Any Containers when there are none left in the user's answers" in {

        RemoveItemContainerNumberPage(index, index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(goodsRoutes.AnyShippingContainersController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Check Goods Item" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "to Add Item container Number when there is still at least one in the user's answers" in {

        val answers =
          emptyUserAnswers
            .set(ItemContainerNumberPage(index, index), container).success.value

        RemoveItemContainerNumberPage(index, index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.AddItemContainerNumberController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Any Containers when there are none left in the user's answers" in {

        RemoveItemContainerNumberPage(index, index).navigate(waypoints, emptyUserAnswers)
          .mustEqual(goodsRoutes.AnyShippingContainersController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }
    }
  }
}
