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
import models.DangerousGood
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class DangerousGoodPageSpec extends SpecBase with PageBehaviours {

  "DangerousGoodPage" - {

    val dangerousGoodsCode = arbitrary[DangerousGood].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints


      "to dangerous good code page when answer is yes" in {

        val answers = emptyUserAnswers.set(DangerousGoodPage(index), true).success.value

        DangerousGoodPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.DangerousGoodCodeController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to payment method when answer is no" in {

        val answers = emptyUserAnswers.set(DangerousGoodPage(index), false).success.value

        DangerousGoodPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.PaymentMethodController.onPageLoad(waypoints, answers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Check Goods Item" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "when the answer is yes" - {

        "to Check Goods item with the current waypoint removed when Dangerous Goods Code is already answered" in {

          val answers =
            emptyUserAnswers
              .set(DangerousGoodCodePage(index), dangerousGoodsCode).success.value
              .set(DangerousGoodPage(index), true).success.value

          DangerousGoodPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
        }

        "to Dangerous Goods Code when it is not already answered" in {

          val answers = emptyUserAnswers.set(DangerousGoodPage(index), true).success.value

          DangerousGoodPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.DangerousGoodCodeController.onPageLoad(waypoints, answers.lrn, index))
        }
      }

      "when the answer is no" - {

        "to Check Goods Item with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(DangerousGoodPage(index), false).success.value

          DangerousGoodPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
        }
      }
    }

    "must not alter the user's answers when the answer is yes" in {

      val answers = emptyUserAnswers.set(DangerousGoodCodePage(index), dangerousGoodsCode).success.value

      val result = answers.set(DangerousGoodPage(index), true).success.value

      result.get(DangerousGoodCodePage(index)).value mustEqual dangerousGoodsCode
    }

    "must remove Dangerous Goods Code when the answer is no" in {

      val answers = emptyUserAnswers.set(DangerousGoodCodePage(index), dangerousGoodsCode).success.value

      val result = answers.set(DangerousGoodPage(index), false).success.value

      result.get(DangerousGoodCodePage(index)) must not be defined
    }
  }
}
