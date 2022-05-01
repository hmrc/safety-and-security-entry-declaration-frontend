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
import models.{Document, Index}
import org.scalacheck.Arbitrary.arbitrary
import pages.{EmptyWaypoints, Waypoints}
import pages.behaviours.PageBehaviours

class RemoveOverallDocumentPageSpec extends SpecBase with PageBehaviours {

  "RemoveOverallDocumentPage" - {

    val document = arbitrary[Document].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Any Overall Documents when there are no overall documents left" in {

        RemoveOverallDocumentPage(Index(0)).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AnyOverallDocumentsController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }

      "to Add Overall Document when there is at least one document left" in {

        val answers = emptyUserAnswers.set(OverallDocumentPage(Index(0)), document).success.value

        RemoveOverallDocumentPage(Index(0)).navigate(waypoints, answers)
          .mustEqual(routes.AddOverallDocumentController.onPageLoad(waypoints, answers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Transport" - {

      val waypoints = Waypoints(List(CheckTransportPage.waypoint))

      "to Any Overall Documents when there are no overall documents left" in {

        RemoveOverallDocumentPage(Index(0)).navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AnyOverallDocumentsController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }

      "to Add Overall Document when there is at least one document left" in {

        val answers = emptyUserAnswers.set(OverallDocumentPage(Index(0)), document).success.value

        RemoveOverallDocumentPage(Index(0)).navigate(waypoints, answers)
          .mustEqual(routes.AddOverallDocumentController.onPageLoad(waypoints, answers.lrn))
      }
    }
  }
}
