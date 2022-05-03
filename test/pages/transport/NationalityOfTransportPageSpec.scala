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
import models.TransportIdentity.{RoadIdentity, RoroAccompaniedIdentity, RoroUnaccompaniedIdentity}
import models.TransportMode._
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class NationalityOfTransportPageSpec extends SpecBase with PageBehaviours {

  "NationalityOfTransportPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Air Identity if transport mode is Air" in {

        val answers = emptyUserAnswers.set(TransportModePage, Air).success.value

        NationalityOfTransportPage.navigate(waypoints, answers)
          .mustEqual(routes.AirIdentityController.onPageLoad(waypoints, answers.lrn))
      }

      "to Rail Identity if transport mode is Rail" in {

        val answers = emptyUserAnswers.set(TransportModePage, Rail).success.value

        NationalityOfTransportPage.navigate(waypoints, answers)
          .mustEqual(routes.RailIdentityController.onPageLoad(waypoints, answers.lrn))
      }

      "to Road Identity if transport mode is Road" in {

        val answers = emptyUserAnswers.set(TransportModePage, Road).success.value

        NationalityOfTransportPage.navigate(waypoints, answers)
          .mustEqual(routes.RoadIdentityController.onPageLoad(waypoints, answers.lrn))
      }

      "to Roro Accompanied Identity if transport mode is Roro Accompanied" in {

        val answers = emptyUserAnswers.set(TransportModePage, RoroAccompanied).success.value

        NationalityOfTransportPage.navigate(waypoints, answers)
          .mustEqual(routes.RoroAccompaniedIdentityController.onPageLoad(waypoints, answers.lrn))
      }

      "to Roro Accompanied Identity if transport mode is Roro Unaccompanied" in {

        val answers = emptyUserAnswers.set(TransportModePage, RoroAccompanied).success.value

        NationalityOfTransportPage.navigate(waypoints, answers)
          .mustEqual(routes.RoroAccompaniedIdentityController.onPageLoad(waypoints, answers.lrn))
      }

      "to Roro Unaccompanied Identity if transport mode is Roro Unaccompanied" in {

        val answers = emptyUserAnswers.set(TransportModePage, RoroUnaccompanied).success.value

        NationalityOfTransportPage.navigate(waypoints, answers)
          .mustEqual(routes.RoroUnaccompaniedIdentityController.onPageLoad(waypoints, answers.lrn))
      }
    }

    "must navigate when the current waypoint is Check Transport" - {

      val waypoints = Waypoints(List(CheckTransportPage.waypoint))

      "when the transport mode is Air" - {

        "to Check Transport with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(TransportModePage, Air).success.value

          NationalityOfTransportPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "when the transport mode is Maritime" - {

        "to Check Transport with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(TransportModePage, Maritime).success.value

          NationalityOfTransportPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "when the transport mode is Rail" - {

        "to Check Transport with the current waypoint removed" in {

          val answers = emptyUserAnswers.set(TransportModePage, Rail).success.value

          NationalityOfTransportPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "when the transport mode is Road" - {

        "to Check Transport with the current waypoint removed when Road Identity has been answered" in {

          val answers =
            emptyUserAnswers
              .set(RoadIdentityPage, arbitrary[RoadIdentity].sample.value).success.value
              .set(TransportModePage, Road).success.value

          NationalityOfTransportPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }

        "to Road Identity when it has not been answered" in {

          val answers = emptyUserAnswers.set(TransportModePage, Road).success.value

          NationalityOfTransportPage.navigate(waypoints, answers)
            .mustEqual(routes.RoadIdentityController.onPageLoad(waypoints, answers.lrn))
        }
      }

      "when the transport mode is Roro Accompanied" - {

        "to Check Transport with the current waypoint removed when Roro Accompanied Identity has been answered" in {

          val answers =
            emptyUserAnswers
              .set(RoroAccompaniedIdentityPage, arbitrary[RoroAccompaniedIdentity].sample.value).success.value
              .set(TransportModePage, RoroAccompanied).success.value

          NationalityOfTransportPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }

        "to Roro Accompanied Identity when it has not been answered" in {

          val answers = emptyUserAnswers.set(TransportModePage, RoroAccompanied).success.value

          NationalityOfTransportPage.navigate(waypoints, answers)
            .mustEqual(routes.RoroAccompaniedIdentityController.onPageLoad(waypoints, answers.lrn))
        }
      }

      "when the transport mode is Roro Unaccompanied" - {

        "to Check Transport with the current waypoint removed when Roro Unaccompanied Identity has been answered" in {

          val answers =
            emptyUserAnswers
              .set(RoroUnaccompaniedIdentityPage, arbitrary[RoroUnaccompaniedIdentity].sample.value).success.value
              .set(TransportModePage, RoroUnaccompanied).success.value

          NationalityOfTransportPage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }

        "to Roro Unaccompanied Identity when it has not been answered" in {

          val answers = emptyUserAnswers.set(TransportModePage, RoroUnaccompanied).success.value

          NationalityOfTransportPage.navigate(waypoints, answers)
            .mustEqual(routes.RoroUnaccompaniedIdentityController.onPageLoad(waypoints, answers.lrn))
        }
      }
    }
  }
}
