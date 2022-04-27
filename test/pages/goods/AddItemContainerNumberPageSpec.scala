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
import models.{CheckMode, Container, Index, NormalMode, ProvideGrossWeight}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import pages.behaviours.PageBehaviours
import pages.predec.ProvideGrossWeightPage
import pages.{EmptyWaypoints, Waypoints}

class AddItemContainerNumberPageSpec extends SpecBase with PageBehaviours {

  "Add Item Container Number Page" - {

    val container = arbitrary[Container].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "when the answer is yes" - {

        "to Item Container Number for the next index" in {

          val answers =
            emptyUserAnswers
              .set(ItemContainerNumberPage(index, Index(0)), Container("abc")).success.value
              .set(AddItemContainerNumberPage(index), true).success.value

          AddItemContainerNumberPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.ItemContainerNumberController.onPageLoad(waypoints, answers.lrn, index, Index(1)))
        }
      }

      "when the answer is no" - {

        "to Goods Item Gross Weight when the user chose to give weight per item" in {

          val answers =
            emptyUserAnswers
              .set(ProvideGrossWeightPage, ProvideGrossWeight.PerItem).success.value
              .set(ItemContainerNumberPage(index, Index(0)), Container("abc")).success.value
              .set(AddItemContainerNumberPage(index), false).success.value

          AddItemContainerNumberPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.GoodsItemGrossWeightController.onPageLoad(waypoints, answers.lrn, index))
        }

        "to Kind of Package for the first index when the user chose to give weight overall" in {

          val answers =
            emptyUserAnswers
              .set(ProvideGrossWeightPage, ProvideGrossWeight.Overall).success.value
              .set(ItemContainerNumberPage(index, Index(0)), Container("abc")).success.value
              .set(AddItemContainerNumberPage(index), false).success.value

          AddItemContainerNumberPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.KindOfPackageController.onPageLoad(waypoints, answers.lrn, index, Index(0)))
        }
      }
    }

    "must navigate when the current waypoint is Check Goods Item" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "when the answer is yes" - {

        "to Item Container Number for the next index with Add Item Container Number added to the waypoints" in {

          val answers =
            emptyUserAnswers
              .set(ItemContainerNumberPage(index, index), container).success.value
              .set(AddItemContainerNumberPage(index), true).success.value

          val expectedWaypoints = waypoints.setNextWaypoint(AddItemContainerNumberPage(index).waypoint(NormalMode))

          AddItemContainerNumberPage(index).navigate(waypoints, answers)
            .mustEqual(
              goodsRoutes.ItemContainerNumberController.onPageLoad(expectedWaypoints, answers.lrn, index, Index(1))
            )
        }

        "when the answer is no" - {

          "to Check Goods Item with the current waypoint removed" in {

            val answers = emptyUserAnswers.set(AddItemContainerNumberPage(index), false).success.value

            AddItemContainerNumberPage(index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
          }
        }
      }
    }

    ".waypointFromString" - {

      "must read from a valid waypoint" in {

        forAll(Gen.choose(1, 999)) {
          index =>
            val normalModeWaypoint = s"add-container-$index"
            val checkModeWaypoint = s"change-container-$index"

            AddItemContainerNumberPage.waypointFromString(normalModeWaypoint).value
              .mustEqual(AddItemContainerNumberPage(Index(index - 1)).waypoint(NormalMode))

            AddItemContainerNumberPage.waypointFromString(checkModeWaypoint).value
              .mustEqual(AddItemContainerNumberPage(Index(index - 1)).waypoint(CheckMode))
        }
      }

      "must not read from an invalid waypoint" in {

        forAll(nonEmptyString) {
          s =>
            whenever(!s.startsWith("add-container-") && !s.startsWith("change-container-")) {
              AddItemContainerNumberPage.waypointFromString(s) must not be defined
            }
        }
      }
    }
  }
}
