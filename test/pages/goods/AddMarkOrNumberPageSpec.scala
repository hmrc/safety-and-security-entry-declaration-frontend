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
import models.Index
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class AddMarkOrNumberPageSpec extends SpecBase with PageBehaviours {

  "AddMarkOrNumberPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Mark or Number if the answer is yes" in {

        val answers = emptyUserAnswers.set(AddMarkOrNumberPage(index, index), true).success.value

        AddMarkOrNumberPage(index, index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.MarkOrNumberController.onPageLoad(waypoints, answers.lrn, index, index))
      }

      "to Check Package Item if the answer is no" in {

        val answers = emptyUserAnswers.set(AddMarkOrNumberPage(index, index), false).success.value

        AddMarkOrNumberPage(index, index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.CheckPackageItemController.onPageLoad(waypoints, answers.lrn, index, index))
      }
    }

    "must navigate when the current waypoint is Check Package Item" - {

      val waypoints = Waypoints(List(CheckPackageItemPage(index, index).waypoint))

      "when the answer is yes" - {

        "and there is already a mark or number for this package" - {

          "to Check Package Item with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(AddMarkOrNumberPage(index, index), true).success.value
                .set(MarkOrNumberPage(index, index), "abc").success.value

            AddMarkOrNumberPage(index, index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.CheckPackageItemController.onPageLoad(EmptyWaypoints, answers.lrn, index, index))
          }
        }

        "when there is not already a mark or number for this package" - {

          "to Mark or Number for this package" in {

            val answers = emptyUserAnswers.set(AddMarkOrNumberPage(index, index), true).success.value

            AddMarkOrNumberPage(index, index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.MarkOrNumberController.onPageLoad(waypoints, answers.lrn, index, index))
          }
        }
      }

      "when the answer is no" - {

        "to Check Package Item with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(AddMarkOrNumberPage(index, index), false).success.value

          AddMarkOrNumberPage(index, index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckPackageItemController.onPageLoad(EmptyWaypoints, answers.lrn, index, index))
        }
      }
    }

    "must not alter the user's answers when the answer is yes" in {

      val answers = emptyUserAnswers.set(MarkOrNumberPage(index, index), "mark").success.value

      val result = AddMarkOrNumberPage(index, index).cleanup(Some(true), answers).success.value

      result mustEqual answers
    }

    "must remove the mark or number for this package when the answer is no" in {

      val answers =
        emptyUserAnswers
          .set(MarkOrNumberPage(Index(0), Index(0)), "mark1").success.value
          .set(MarkOrNumberPage(Index(0), Index(1)), "mark2").success.value
          .set(MarkOrNumberPage(Index(1), Index(0)), "mark3").success.value

      val result = AddMarkOrNumberPage(Index(0), Index(1)).cleanup(Some(false), answers).success.value

      result mustEqual answers.remove(MarkOrNumberPage(Index(0), Index(1))).success.value
    }
  }
}
