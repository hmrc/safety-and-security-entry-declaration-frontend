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
import models.Country
import models.TransportIdentity._
import models.TransportMode._
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class TransportModePageSpec extends SpecBase with PageBehaviours {

  "TransportModePage" - {

    val fullAnswers =
      emptyUserAnswers
        .set(NationalityOfTransportPage, arbitrary[Country].sample.value).success.value
        .set(AirIdentityPage, arbitrary[AirIdentity].sample.value).success.value
        .set(MaritimeIdentityPage, arbitrary[MaritimeIdentity].sample.value).success.value
        .set(RailIdentityPage, arbitrary[RailIdentity].sample.value).success.value
        .set(RoadIdentityPage, arbitrary[RoadIdentity].sample.value).success.value
        .set(RoroAccompaniedIdentityPage, arbitrary[RoroAccompaniedIdentity].sample.value).success.value
        .set(RoroUnaccompaniedIdentityPage, arbitrary[RoroUnaccompaniedIdentity].sample.value).success.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "and the user chooses Air" - {

        "to Air Identity" in {

          val answers = emptyUserAnswers.set(TransportModePage, Air).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.AirIdentityController.onPageLoad(waypoints, answers.lrn))
        }
      }
      
      "and the user chooses Maritime" - {

        "to Maritime Identity" in {

          val answers = emptyUserAnswers.set(TransportModePage, Maritime).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.MaritimeIdentityController.onPageLoad(waypoints, answers.lrn))
        }
      }

      "and the user chooses Rail" - {

        "to Rail Identity" in {

          val answers = emptyUserAnswers.set(TransportModePage, Rail).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.RailIdentityController.onPageLoad(waypoints, answers.lrn))
        }
      }

      "and the user chooses Road" - {

        "to Nationality of Transport" in {

          val answers = emptyUserAnswers.set(TransportModePage, Road).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.NationalityOfTransportController.onPageLoad(waypoints, answers.lrn))
        }
      }

      "and the user chooses Roro Accompanied" - {

        "to Nationality of Transport" in {

          val answers = emptyUserAnswers.set(TransportModePage, RoroAccompanied).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.NationalityOfTransportController.onPageLoad(waypoints, answers.lrn))
        }
      }

      "and the user chooses Roro Unaccompanied" - {

        "to Nationality of Transport" in {

          val answers = emptyUserAnswers.set(TransportModePage, RoroUnaccompanied).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.NationalityOfTransportController.onPageLoad(waypoints, answers.lrn))
        }
      }
    }

    "when the current waypoint is Check Transport" - {

      val waypoints = Waypoints(List(CheckTransportPage.waypoint))

      "and the user chooses Air" - {

        "to Air Identity when that has not been answered" in {

          val answers = emptyUserAnswers.set(TransportModePage, Air).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.AirIdentityController.onPageLoad(waypoints, answers.lrn))
        }

        "to Check Transport with the current waypoint removed when Air Identity has been answered" in {

          val answers =
            emptyUserAnswers
              .set(AirIdentityPage, arbitrary[AirIdentity].sample.value).success.value
              .set(TransportModePage, Air).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "and the user chooses Maritime" - {

        "to Maritime Identity when that has not been answered" in {

          val answers = emptyUserAnswers.set(TransportModePage, Maritime).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.MaritimeIdentityController.onPageLoad(waypoints, answers.lrn))
        }

        "to Check Transport with the current waypoint removed when Maritime Identity has been answered" in {

          val answers =
            emptyUserAnswers
              .set(MaritimeIdentityPage, arbitrary[MaritimeIdentity].sample.value).success.value
              .set(TransportModePage, Maritime).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "and the user chooses Rail" - {

        "to Rail Identity when that has not been answered" in {

          val answers = emptyUserAnswers.set(TransportModePage, Rail).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.RailIdentityController.onPageLoad(waypoints, answers.lrn))
        }

        "to Check Transport with the current waypoint removed when Rail Identity has been answered" in {

          val answers =
            emptyUserAnswers
              .set(RailIdentityPage, arbitrary[RailIdentity].sample.value).success.value
              .set(TransportModePage, Rail).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "and the user chooses Road" - {

        "to Nationality of Transport when that has not been answered" in {

          val answers = emptyUserAnswers.set(TransportModePage, Road).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.NationalityOfTransportController.onPageLoad(waypoints, answers.lrn))
        }

        "to Road Identity when Nationality has been answered but Road Identity has not" in {

          val answers =
            emptyUserAnswers
              .set(NationalityOfTransportPage, arbitrary[Country].sample.value).success.value
              .set(TransportModePage, Road).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.RoadIdentityController.onPageLoad(waypoints, answers.lrn))
        }

        "to Check Transport with the current waypoint removed when Nationality of Transport and Road have been answered" in {

          val answers =
            emptyUserAnswers
              .set(NationalityOfTransportPage, arbitrary[Country].sample.value).success.value
              .set(RoadIdentityPage, arbitrary[RoadIdentity].sample.value).success.value
              .set(TransportModePage, Road).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "and the user chooses Roro Accompanied" - {

        "to Nationality of Transport when that has not been answered" in {

          val answers = emptyUserAnswers.set(TransportModePage, RoroAccompanied).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.NationalityOfTransportController.onPageLoad(waypoints, answers.lrn))
        }

        "to Roro when Nationality has been answered but Roro has not" in {

          val answers =
            emptyUserAnswers
              .set(NationalityOfTransportPage, arbitrary[Country].sample.value).success.value
              .set(TransportModePage, RoroAccompanied).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.RoroAccompaniedIdentityController.onPageLoad(waypoints, answers.lrn))
        }

        "to Check Transport with the current waypoint removed when Nationality and Roro have been answered" in {

          val answers =
            emptyUserAnswers
              .set(NationalityOfTransportPage, arbitrary[Country].sample.value).success.value
              .set(RoroAccompaniedIdentityPage, arbitrary[RoroAccompaniedIdentity].sample.value).success.value
              .set(TransportModePage, RoroAccompanied).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }

      "and the user chooses Roro Unaccompanied" - {

        "to Nationality of Transport when that has not been answered" in {

          val answers = emptyUserAnswers.set(TransportModePage, RoroUnaccompanied).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.NationalityOfTransportController.onPageLoad(waypoints, answers.lrn))
        }

        "to Check Transport when Nationality of Transport has been answered but Roro has not" in {

          val answers =
            emptyUserAnswers
              .set(NationalityOfTransportPage, arbitrary[Country].sample.value).success.value
              .set(TransportModePage, RoroUnaccompanied).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.RoroUnaccompaniedIdentityController.onPageLoad(waypoints, answers.lrn))
        }

        "to Check Transport with the current waypoint removed when Nationality of Transport has been answered" in {

          val answers =
            emptyUserAnswers
              .set(NationalityOfTransportPage, arbitrary[Country].sample.value).success.value
              .set(RoroUnaccompaniedIdentityPage, arbitrary[RoroUnaccompaniedIdentity].sample.value).success.value
              .set(TransportModePage, RoroUnaccompanied).success.value

          TransportModePage.navigate(waypoints, answers)
            .mustEqual(routes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn))
        }
      }
    }

    "when the user chooses Air" - {

      "must remove Nationality of Transport and all other types' details" in {

        val result = fullAnswers.set(TransportModePage, Air).success.value

        result.get(AirIdentityPage) mustBe defined

        result.get(NationalityOfTransportPage) must not be defined
        result.get(MaritimeIdentityPage) must not be defined
        result.get(RoadIdentityPage) must not be defined
        result.get(RailIdentityPage) must not be defined
        result.get(RoroAccompaniedIdentityPage) must not be defined
        result.get(RoroUnaccompaniedIdentityPage) must not be defined
      }
    }

    "when the user chooses Maritime" - {

      "must remove Nationality of Transport and all other types' details" in {

        val result = fullAnswers.set(TransportModePage, Maritime).success.value

        result.get(MaritimeIdentityPage) mustBe defined

        result.get(NationalityOfTransportPage) must not be defined
        result.get(AirIdentityPage) must not be defined
        result.get(RoadIdentityPage) must not be defined
        result.get(RailIdentityPage) must not be defined
        result.get(RoroAccompaniedIdentityPage) must not be defined
        result.get(RoroUnaccompaniedIdentityPage) must not be defined
      }
    }

    "when the user chooses Rail" - {

      "must remove Nationality of Transport and all other types' details" in {

        val result = fullAnswers.set(TransportModePage, Rail).success.value

        result.get(RailIdentityPage) mustBe defined

        result.get(NationalityOfTransportPage) must not be defined
        result.get(AirIdentityPage) must not be defined
        result.get(MaritimeIdentityPage) must not be defined
        result.get(RoadIdentityPage) must not be defined
        result.get(RoroAccompaniedIdentityPage) must not be defined
        result.get(RoroUnaccompaniedIdentityPage) must not be defined
      }
    }

    "when the user chooses Road" - {

      "must remove all other types' details" in {

        val result = fullAnswers.set(TransportModePage, Road).success.value

        result.get(RoadIdentityPage) mustBe defined
        result.get(NationalityOfTransportPage) mustBe defined

        result.get(AirIdentityPage) must not be defined
        result.get(MaritimeIdentityPage) must not be defined
        result.get(RailIdentityPage) must not be defined
        result.get(RoroAccompaniedIdentityPage) must not be defined
        result.get(RoroUnaccompaniedIdentityPage) must not be defined
      }
    }

    "when the user chooses Roro Accompanied" - {

      "must remove all other types' details" in {

        val result = fullAnswers.set(TransportModePage, RoroAccompanied).success.value

        result.get(RoroAccompaniedIdentityPage) mustBe defined
        result.get(NationalityOfTransportPage) mustBe defined

        result.get(AirIdentityPage) must not be defined
        result.get(MaritimeIdentityPage) must not be defined
        result.get(RailIdentityPage) must not be defined
        result.get(RoadIdentityPage) must not be defined
        result.get(RoroUnaccompaniedIdentityPage) must not be defined
      }
    }

    "when the user chooses Roro Unaccompanied" - {

      "must remove all other types' details" in {

        val result = fullAnswers.set(TransportModePage, RoroUnaccompanied).success.value

        result.get(RoroUnaccompaniedIdentityPage) mustBe defined
        result.get(NationalityOfTransportPage) mustBe defined

        result.get(AirIdentityPage) must not be defined
        result.get(MaritimeIdentityPage) must not be defined
        result.get(RailIdentityPage) must not be defined
        result.get(RoadIdentityPage) must not be defined
        result.get(RoroAccompaniedIdentityPage) must not be defined
      }
    }
  }
}
