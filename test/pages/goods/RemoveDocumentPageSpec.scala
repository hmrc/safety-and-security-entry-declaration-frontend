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

import controllers.goods.routes
import org.scalacheck.Arbitrary.arbitrary
import base.SpecBase
import models.{Document, NormalMode}
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours
import pages.transport.AnyOverallDocumentsPage

class RemoveDocumentPageSpec extends SpecBase with PageBehaviours {

  "RemoveDocumentPage" - {

    val document = arbitrary[Document].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "when there is at least one document left in the user's answers" - {

        "to Add Document" in {

          val answers = emptyUserAnswers.set(DocumentPage(index, index), document).success.value

          RemoveDocumentPage(index, index).navigate(waypoints, answers)
            .mustEqual(routes.AddDocumentController.onPageLoad(waypoints, answers.lrn, index))
        }
      }

      "when there are no documents left in the user's answers" - {

        "to Add Any Documents when the user gave an overall document" in {

          val answers = emptyUserAnswers.set(AnyOverallDocumentsPage, true).success.value

          RemoveDocumentPage(index, index).navigate(waypoints, answers)
            .mustEqual(routes.AddAnyDocumentsController.onPageLoad(waypoints, answers.lrn, index))
        }

        "to Document for the first index when the user did not give an overall document" in {

          val answers = emptyUserAnswers.set(AnyOverallDocumentsPage, false).success.value

          RemoveDocumentPage(index, index).navigate(waypoints, answers)
            .mustEqual(routes.DocumentController.onPageLoad(waypoints, answers.lrn, index, index))
        }
      }
    }

    "must navigate when the current waypoint is Check Goods Item" - {

      val waypoints = Waypoints(List(CheckGoodsItemPage(index).waypoint))

      "when there is at least one document left in the user's answers" - {

        "to Add Document" in {

          val answers = emptyUserAnswers.set(DocumentPage(index, index), document).success.value

          RemoveDocumentPage(index, index).navigate(waypoints, answers)
            .mustEqual(routes.AddDocumentController.onPageLoad(waypoints, answers.lrn, index))
        }
      }

      "when there are no documents left in the user's answers" - {

        "to Add Any Documents when the user gave an overall document" in {

          val answers = emptyUserAnswers.set(AnyOverallDocumentsPage, true).success.value

          RemoveDocumentPage(index, index).navigate(waypoints, answers)
            .mustEqual(routes.AddAnyDocumentsController.onPageLoad(waypoints, answers.lrn, index))
        }

        "to Document with Add Document added to the waypoints when the user did not give an overall document" in {

          val answers = emptyUserAnswers.set(AnyOverallDocumentsPage, false).success.value

          val expectedWaypoints = waypoints.setNextWaypoint(AddDocumentPage(index).waypoint(NormalMode))

          RemoveDocumentPage(index, index).navigate(waypoints, answers)
            .mustEqual(routes.DocumentController.onPageLoad(expectedWaypoints, answers.lrn, index, index))
        }
      }
    }
  }
}
