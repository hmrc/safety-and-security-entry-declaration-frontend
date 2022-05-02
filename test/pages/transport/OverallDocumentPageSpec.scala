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
import models.NormalMode
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class OverallDocumentPageSpec extends SpecBase with PageBehaviours {
  private val page = OverallDocumentPage(index)

  "OverallDocumentPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Overall Document" in {

        page.navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AddOverallDocumentController.onPageLoad(waypoints, emptyUserAnswers.lrn))
      }
    }

    "must navigate when the current waypoint is Add Overall Document" - {

      val waypoints = Waypoints(List(AddOverallDocumentPage.waypoint(NormalMode)))

      "to Add Overall Document with the current waypoint removed" in {

        page.navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.AddOverallDocumentController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn))
      }
    }
  }
}
