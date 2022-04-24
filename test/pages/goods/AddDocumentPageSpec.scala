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
import models.{CheckMode, Document, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours

class AddDocumentPageSpec extends SpecBase with PageBehaviours {

  "AddDocumentPage" - {

    val document = arbitrary[Document].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Document for the next index when the answer is yes" in {

        val answers =
          emptyUserAnswers
            .set(DocumentPage(index, Index(0)), document).success.value
            .set(AddDocumentPage(index), true).success.value

        AddDocumentPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.DocumentController.onPageLoad(waypoints, answers.lrn, index, Index(1)))
      }

      "to Dangerous Goods when the answer is no" in {

        val answers =
          emptyUserAnswers
            .set(DocumentPage(index, Index(0)), document).success.value
            .set(AddDocumentPage(index), false).success.value

        AddDocumentPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.DangerousGoodController.onPageLoad(waypoints, answers.lrn, index))
      }
    }

    "must navigate when the current waypoint is Check Goods Item" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "when the answer is yes" - {

        "to Document for the next index with AddDocument added to the waypoints" in {

          val answers =
            emptyUserAnswers
              .set(DocumentPage(index, Index(0)), document).success.value
              .set(AddDocumentPage(index), true).success.value

          val expectedWaypoints = waypoints.setNextWaypoint(AddDocumentPage(index).waypoint(NormalMode))

          AddDocumentPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.DocumentController.onPageLoad(expectedWaypoints, answers.lrn, index, Index(1)))
        }
      }

      "when the answer is no" - {

        "to Check Goods Item with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(AddDocumentPage(index), false).success.value

          AddDocumentPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
        }
      }
    }

    ".waypointFromString" - {

      "must read from a valid waypoint" in {

        forAll(Gen.choose(1, 999)) {
          index =>
            val normalModeWaypoint = s"add-document-$index"
            val checkModeWaypoint = s"change-document-$index"

            AddDocumentPage.waypointFromString(normalModeWaypoint).value
              .mustEqual(AddDocumentPage(Index(index - 1)).waypoint(NormalMode))

            AddDocumentPage.waypointFromString(checkModeWaypoint).value
              .mustEqual(AddDocumentPage(Index(index - 1)).waypoint(CheckMode))
        }
      }

      "must not read from an invalid waypoint" in {

        forAll(nonEmptyString) {
          s =>
            whenever(!s.startsWith("add-document-") && !s.startsWith("change-document-")) {
              AddDocumentPage.waypointFromString(s) must not be defined
            }
        }
      }
    }
  }
}
