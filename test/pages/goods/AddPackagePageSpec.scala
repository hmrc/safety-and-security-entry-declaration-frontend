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
import models.{Index, KindOfPackage, NormalMode}
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours
import pages.transport.AnyOverallDocumentsPage

class AddPackagePageSpec extends SpecBase with PageBehaviours {

  "AddPackagePage" - {

    val kindOfPackage = KindOfPackage.standardKindsOfPackages.head

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "when the answer is yes" - {

        "to Kind of Package for the next index" in {

          val answers =
            emptyUserAnswers
              .set(KindOfPackagePage(index, Index(0)), kindOfPackage).success.value
              .set(NumberOfPackagesPage(index, Index(0)), 1).success.value
              .set(AddMarkOrNumberPage(index, Index(0)), true).success.value
              .set(MarkOrNumberPage(index, Index(0)), "abc").success.value
              .set(AddPackagePage(index), true).success.value

          AddPackagePage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.KindOfPackageController.onPageLoad(waypoints, answers.lrn, index, Index(1)))
        }
      }

      "when the answer is no" - {

        "to Add Any Documents when the user gave any overall documents" in {

          val answers =
            emptyUserAnswers
              .set(AnyOverallDocumentsPage, true).success.value
              .set(KindOfPackagePage(index, Index(0)), kindOfPackage).success.value
              .set(NumberOfPackagesPage(index, Index(0)), 1).success.value
              .set(AddMarkOrNumberPage(index, Index(0)), true).success.value
              .set(MarkOrNumberPage(index, Index(0)), "abc").success.value
              .set(AddPackagePage(index), false).success.value

          AddPackagePage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.AddAnyDocumentsController.onPageLoad(waypoints, answers.lrn, index))
        }

        "to Document for the first index when the user did not give any overall documents" in {

          val answers =
            emptyUserAnswers
              .set(AnyOverallDocumentsPage, false).success.value
              .set(AddPackagePage(index), false).success.value

          AddPackagePage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.DocumentController.onPageLoad(waypoints, answers.lrn, index, Index(0)))
        }
      }
    }

    "must navigate when the current waypoint is Check Goods Item" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "when the answer is yes" - {

        "to Kind of Package for the next index with Add Package added to the waypoints" in {

          val answers =
            emptyUserAnswers
              .set(KindOfPackagePage(index, Index(0)), kindOfPackage).success.value
              .set(NumberOfPackagesPage(index, Index(0)), 1).success.value
              .set(AddMarkOrNumberPage(index, Index(0)), true).success.value
              .set(MarkOrNumberPage(index, Index(0)), "abc").success.value
              .set(AddPackagePage(index), true).success.value

          val expectedWaypoints = waypoints.setNextWaypoint(AddPackagePage(index).waypoint(NormalMode))

          AddPackagePage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.KindOfPackageController.onPageLoad(expectedWaypoints, answers.lrn, index, Index(1)))
        }
      }

      "when the answer is no" - {

        "to Check Goods Item with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(AddPackagePage(index), false).success.value

          AddPackagePage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
        }
      }
    }
  }
}
