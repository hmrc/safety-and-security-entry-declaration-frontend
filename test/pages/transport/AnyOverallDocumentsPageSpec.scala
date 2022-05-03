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

package pages.transport

import base.SpecBase
import controllers.transport.routes
import models.{Document, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class AnyOverallDocumentsPageSpec extends SpecBase with PageBehaviours {

  "AnyOverallDocumentsPage" - {

    val doc1 = arbitrary[Document].sample.value
    val doc2 = arbitrary[Document].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Overall Document when the answer is yes" in {

        val answers = emptyUserAnswers.set(AnyOverallDocumentsPage, true).success.value

        AnyOverallDocumentsPage.navigate(waypoints, answers)
          .mustEqual(routes.OverallDocumentController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Add Any Seals when the answer is no" in {

        val answers = emptyUserAnswers.set(AnyOverallDocumentsPage, false).success.value

        AnyOverallDocumentsPage.navigate(waypoints, answers)
          .mustEqual(routes.AddAnySealsController.onPageLoad(waypoints, answers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Transport" - {

      val waypoints = Waypoints(List(CheckTransportPage.waypoint))

      "when the answer is yes" - {

        "to Overall Document with Add Overall Document added to the waypoints when there are none in the user's answers" in {

          val answers = emptyUserAnswers.set(AnyOverallDocumentsPage, true).success.value
          val expectedWaypoints = waypoints.setNextWaypoint(AddOverallDocumentPage.waypoint(NormalMode))

          AnyOverallDocumentsPage.navigate(waypoints, answers)
            .mustEqual(routes.OverallDocumentController.onPageLoad(expectedWaypoints, answers.lrn, index))
        }

        "to Check Transport with the current waypoint removed when there are overall documents in the user's answers" in {

          val answers =
            emptyUserAnswers
              .set(OverallDocumentPage(Index(0)), arbitrary[Document].sample.value).success.value
              .set(AnyOverallDocumentsPage, true).success.value

          AnyOverallDocumentsPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "when the answer is no" - {

        "to Check Transport with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(AnyOverallDocumentsPage, false).success.value

          AnyOverallDocumentsPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }
    }

    "must not alter the user's answers when the answer is yes" in {

      val answers =
        emptyUserAnswers
          .set(OverallDocumentPage(Index(0)), doc1).success.value
          .set(OverallDocumentPage(Index(1)), doc2).success.value

      val result = answers.set(AnyOverallDocumentsPage, true).success.value

      result.get(OverallDocumentPage(Index(0))).value mustEqual doc1
      result.get(OverallDocumentPage(Index(1))).value mustEqual doc2
    }

    "must remove all overall documents when the answer is no" in {

      val answers =
        emptyUserAnswers
          .set(OverallDocumentPage(Index(0)), doc1).success.value
          .set(OverallDocumentPage(Index(1)), doc2).success.value

      val result = answers.set(AnyOverallDocumentsPage, false).success.value

      result.get(OverallDocumentPage(Index(0))) must not be defined
      result.get(OverallDocumentPage(Index(1))) must not be defined
    }
  }
}
