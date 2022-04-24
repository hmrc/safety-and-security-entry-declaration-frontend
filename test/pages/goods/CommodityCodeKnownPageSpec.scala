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

class CommodityCodeKnownPageSpec extends SpecBase with PageBehaviours {

  "CommodityCodeKnownPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Commodity Code when the answer is yes" in {

        val answers = emptyUserAnswers.set(CommodityCodeKnownPage(index), true).success.value

        CommodityCodeKnownPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.CommodityCodeController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Goods Description when the answer is no" in {

        val answers = emptyUserAnswers.set(CommodityCodeKnownPage(index), false).success.value

        CommodityCodeKnownPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.GoodsDescriptionController.onPageLoad(waypoints, answers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Check Goods Item" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "when the answer is yes" - {

        "and commodity code has already been answered" - {

          "to Check Goods Item with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(CommodityCodePage(index), "00102030").success.value
                .set(CommodityCodeKnownPage(index), true).success.value

            CommodityCodeKnownPage(index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
          }
        }

        "and commodity code has not already been answered" - {

          "to Commodity Code" in {

            val answers = emptyUserAnswers.set(CommodityCodeKnownPage(index), true).success.value

            CommodityCodeKnownPage(index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.CommodityCodeController.onPageLoad(waypoints, answers.lrn, index))
          }
        }
      }

      "when the answer is no" - {

        "and goods description has already been answered" - {

          "to Check Goods Item with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(GoodsDescriptionPage(index), "description").success.value
                .set(CommodityCodeKnownPage(index), false).success.value

            CommodityCodeKnownPage(index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
          }
        }

        "and goods description has not already been answered" - {

          "to Goods Description" in {

            val answers = emptyUserAnswers.set(CommodityCodeKnownPage(index), false).success.value

            CommodityCodeKnownPage(index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.GoodsDescriptionController.onPageLoad(waypoints, answers.lrn, index))
          }
        }
      }
    }

    "must remove goods description when the answer is yes" in {

      val answers = emptyUserAnswers.set(GoodsDescriptionPage(index), "description").success.value

      val result = answers.set(CommodityCodeKnownPage(index), true).success.value

      result.get(GoodsDescriptionPage(index)) must not be defined
    }

    "must remove commodity code when the answer is no" in {

      val answers = emptyUserAnswers.set(CommodityCodePage(index), "00102030").success.value

      val result = answers.set(CommodityCodeKnownPage(index), false).success.value

      result.get(CommodityCodePage(index)) must not be defined
    }
  }
}
