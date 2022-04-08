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

package pages.routedetails

import base.SpecBase
import controllers.routedetails.routes
import models.{Country, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary
import pages.{EmptyWaypoints, Waypoints}

class AddCountryEnRoutePageSpec extends SpecBase {

  "AddCountryEnRoutePage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Country En Route for the next index when the answer is yes" in {

        val country = arbitrary[Country].sample.value
        val answers =
          emptyUserAnswers
            .set(CountryEnRoutePage(Index(0)), country).success.value
            .set(AddCountryEnRoutePage, true).success.value

        AddCountryEnRoutePage.navigate(waypoints, answers)
          .mustEqual(routes.CountryEnRouteController.onPageLoad(waypoints, answers.lrn, Index(1)))
      }

      "to Customs Office of First Entry when the answer is no" in {

        val answers = emptyUserAnswers.set(AddCountryEnRoutePage, false).success.value

        AddCountryEnRoutePage.navigate(waypoints, answers)
          .mustEqual(routes.CustomsOfficeOfFirstEntryController.onPageLoad(waypoints, answers.lrn))
      }
    }

    "when the current waypoint is Check Route Details" - {

      val waypoints = Waypoints(List(CheckRouteDetailsPage.waypoint))

      "when the answer is yes" - {

        "to Country En Route for the next index with AddCountryEnRoute added to the waypoints" in {

          val country = arbitrary[Country].sample.value
          val answers =
            emptyUserAnswers
              .set(CountryEnRoutePage(Index(0)), country).success.value
              .set(AddCountryEnRoutePage, true).success.value

          val expectedWaypoints = waypoints.setNextWaypoint(AddCountryEnRoutePage.waypoint(NormalMode))

          AddCountryEnRoutePage.navigate(waypoints, answers)
            .mustEqual(routes.CountryEnRouteController.onPageLoad(expectedWaypoints, answers.lrn, Index(1)))
        }
      }

      "when the answer is no" - {

        "to Check Route Details with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(AddCountryEnRoutePage, false).success.value

          AddCountryEnRoutePage.navigate(waypoints, answers)
            .mustEqual(routes.CheckRouteDetailsController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }
    }
  }
}
