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
import models.{Document, Index, NormalMode}
import org.scalacheck.Arbitrary.{arbFunction0, arbitrary}
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}
import queries.AllDocumentsQuery

class AddAnyDocumentsPageSpec extends SpecBase with PageBehaviours {

  "AddAnyDocumentsPage" - {

    val document1 = arbitrary[Document].sample.value
    val document2 = arbitrary[Document].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Document for the first index when the answer is yes" in {

        val answers = emptyUserAnswers.set(AddAnyDocumentsPage(index), true).success.value

        AddAnyDocumentsPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.DocumentController.onPageLoad(waypoints, answers.lrn, index, Index(0)))
      }

      "to Dangerous Goods when the answer is no" in {

        val answers = emptyUserAnswers.set(AddAnyDocumentsPage(index), false).success.value

        AddAnyDocumentsPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.DangerousGoodController.onPageLoad(waypoints, answers.lrn, index))
      }
    }

    "must navigate when the current waypoint is CheckGoodsItem" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "when the answer is yes" - {

        "and there are already some documents" - {

          "to Check Goods Item with the current waypoint removed" in {

            val answers =
              emptyUserAnswers
                .set(DocumentPage(index, index), document1).success.value
                .set(AddAnyDocumentsPage(index), true).success.value

            AddAnyDocumentsPage(index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
          }
        }

        "and there are no documents" - {

          "to Document for index 0 with Add Document added to the waypoints" in {

            val answers = emptyUserAnswers.set(AddAnyDocumentsPage(index), true).success.value

            val expectedWaypoints = waypoints.setNextWaypoint(AddDocumentPage(index).waypoint(NormalMode))

            AddAnyDocumentsPage(index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.DocumentController.onPageLoad(expectedWaypoints, answers.lrn, index, Index(0)))
          }
        }

        "and documents have been added and removed so there are none left" - {

          "to Document for index 0 with Add Document added to the waypoints" in {

            val answers =
              emptyUserAnswers
                .set(DocumentPage(index, index), document1).success.value
                .remove(DocumentPage(index, index)).success.value
                .set(AddAnyDocumentsPage(index), true).success.value

            val expectedWaypoints = waypoints.setNextWaypoint(AddDocumentPage(index).waypoint(NormalMode))

            AddAnyDocumentsPage(index).navigate(waypoints, answers)
              .mustEqual(goodsRoutes.DocumentController.onPageLoad(expectedWaypoints, answers.lrn, index, Index(0)))
          }
        }
      }

      "and the answer is no" - {

        "to Check Goods Item with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(AddAnyDocumentsPage(index), false).success.value

          AddAnyDocumentsPage(index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(EmptyWaypoints, answers.lrn, index))
        }
      }
    }

    "must not alter the user's answers when the answer is yes" in {

      val answers =
        emptyUserAnswers
          .set(AddAnyDocumentsPage(Index(0)), true).success.value
          .set(AddAnyDocumentsPage(Index(1)), true).success.value
          .set(DocumentPage(Index(0), Index(0)), document1).success.value
          .set(DocumentPage(Index(1), Index(0)), document2).success.value

      val result = answers.set(AddAnyDocumentsPage(Index(0)), true).success.value

      result mustEqual answers
    }

    "must remove all documents for this goods item when the answer is no" in {

      val answers =
        emptyUserAnswers
          .set(AddAnyDocumentsPage(Index(0)), true).success.value
          .set(AddAnyDocumentsPage(Index(1)), true).success.value
          .set(DocumentPage(Index(0), Index(0)), document1).success.value
          .set(DocumentPage(Index(1), Index(0)), document2).success.value

      val result = AddAnyDocumentsPage(Index(0)).cleanup(Some(false), answers).success.value

      result mustEqual answers.remove(AllDocumentsQuery(Index(0))).success.value
    }
  }
}
