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

class ConsigneeKnownPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeKnownPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Consignee when the answer is yes" in {

        val answers = emptyUserAnswers.set(ConsigneeKnownPage(index), true).success.value

        ConsigneeKnownPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.ConsigneeController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Notified Party when the answer is yes" in {

        val answers = emptyUserAnswers.set(ConsigneeKnownPage(index), false).success.value

        ConsigneeKnownPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.NotifiedPartyController.onPageLoad(waypoints, answers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Check Goods Item" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "when the answer is yes" - {

        "to Check Goods Item with the current waypoint removed when the consignee has already been chosen" in {

          val answers =
            emptyUserAnswers
              .set(ConsigneePage(index), 1).success.value
              .set(ConsigneeKnownPage(index), true).success.value

          ConsigneeKnownPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
        }

        "to Consignee when the consignee has not already been chosen" in {

          val answers = emptyUserAnswers.set(ConsigneeKnownPage(index), true).success.value

          ConsigneeKnownPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.ConsigneeController.onPageLoad(waypoints, answers.lrn, index))
        }
      }

      "when the answer is no" - {

        "to Check Goods Item with the current waypoint removed when the notified party has already been chosen" in {

          val answers =
            emptyUserAnswers
              .set(NotifiedPartyPage(index), 1).success.value
              .set(ConsigneeKnownPage(index), false).success.value

          ConsigneeKnownPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
        }

        "to Notified Party when the notified party has not already been chosen" in {

          val answers = emptyUserAnswers.set(ConsigneeKnownPage(index), false).success.value

          ConsigneeKnownPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.NotifiedPartyController.onPageLoad(waypoints, answers.lrn, index))
        }
      }
    }

    "must remove the notified party when the answer is yes" in {

      val answers = emptyUserAnswers.set(NotifiedPartyPage(index), 1).success.value

      val result = answers.set(ConsigneeKnownPage(index), true).success.value

      result.get(NotifiedPartyPage(index)) must not be defined
    }

    "must remove consignee when the answer is no" in {

      val answers = emptyUserAnswers.set(ConsigneePage(index), 1).success.value

      val result = answers.set(ConsigneeKnownPage(index), false).success.value

      result.get(ConsigneePage(index)) must not be defined
    }
  }
}
