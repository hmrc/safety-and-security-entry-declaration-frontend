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

package pages

import base.SpecBase
import models.{CheckMode, NormalMode}
import org.scalatest.EitherValues
import pages.consignees.{AddConsigneePage, AddNotifiedPartyPage}
import play.api.mvc.QueryStringBindable

class WaypointsSpec extends SpecBase with EitherValues {

  private val waypoint1 = AddConsigneePage.waypoint(NormalMode)
  private val waypoint2 = AddNotifiedPartyPage.waypoint(CheckMode)

  ".push" - {

    "must add the waypoint to the head of the list" - {

      "when the list is empty" in {

        EmptyWaypoints.push(waypoint1) mustEqual Waypoints(List(waypoint1))
      }

      "when the list is not empty" in {

        Waypoints(List(waypoint1)).push(waypoint2) mustEqual Waypoints(List(waypoint2, waypoint1))
      }
    }
  }

  ".pop" - {

    "when there are no waypoints" - {

      "must return empty waypoints" in {

        EmptyWaypoints.pop mustEqual EmptyWaypoints
      }
    }

    "when there is a single waypoint" - {

      "must return empty waypoints" in {

        Waypoints(List(waypoint1)).pop mustEqual EmptyWaypoints
      }
    }

    "when there are multiple waypoints" - {

      "must return waypoints with the first one removed" in {

        Waypoints(List(waypoint1, waypoint2)).pop mustEqual Waypoints(List(waypoint2))
      }
    }
  }

  ".fromString" - {

    "must read from a comma-separated list of valid waypoints" in {

      val string = s"${waypoint1.urlFragment},${waypoint2.urlFragment}"

      Waypoints.fromString(string).value mustEqual Waypoints(List(waypoint1, waypoint2))
    }

    "must not read from an invalid string" in {

      Waypoints.fromString("invalid") must not be defined
    }
  }

  "must bind from a query string" in {

    val bindable = implicitly[QueryStringBindable[Waypoints]]

    val data = Map("key" -> List(s"${waypoint1.urlFragment},${waypoint2.urlFragment}"))
    val expectedResult = Waypoints(List(waypoint1, waypoint2))

    bindable.bind("key", data).value.value mustEqual expectedResult
  }
}
