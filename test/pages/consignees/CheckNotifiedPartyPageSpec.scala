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

package pages.consignees

import base.SpecBase
import controllers.consignees.routes
import generators.Generators
import models.{Index, NormalMode}
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.{Waypoints, EmptyWaypoints}

class CheckNotifiedPartyPageSpec extends SpecBase with ScalaCheckPropertyChecks with Generators {

  ".fromString" - {

    "must read from a valid waypoint" in {

      forAll(Gen.choose(1, 999)) {
        index =>
          val waypoint = s"check-notified-party-$index"

          CheckNotifiedPartyPage.waypointFromString(waypoint).value
            .mustEqual(CheckNotifiedPartyPage(Index(index - 1)).waypoint)
      }
    }

    "must not read from an invalid waypoint" in {

      forAll(nonEmptyString) {
        s =>

          whenever(!s.startsWith("check-notified-party-")) {
            CheckNotifiedPartyPage.waypointFromString(s) must not be defined
          }
      }
    }
  }


  "must navigate when there are no waypoints" - {

    val waypoints = EmptyWaypoints

    "to Add Notified Party" in {

      CheckNotifiedPartyPage(index).navigate(waypoints, emptyUserAnswers)
        .mustEqual(routes.AddNotifiedPartyController.onPageLoad(waypoints, emptyUserAnswers.lrn))
    }
  }

  "must navigate when the current waypoint is AddNotifiedParty" - {

    val waypoints = Waypoints(List(AddNotifiedPartyPage.waypoint(NormalMode)))

    "to Add Notified Party with the first waypoint removed" in {

      CheckNotifiedPartyPage(index).navigate(waypoints, emptyUserAnswers)
        .mustEqual(routes.AddNotifiedPartyController.onPageLoad(waypoints.pop, emptyUserAnswers.lrn))
    }
  }
}
