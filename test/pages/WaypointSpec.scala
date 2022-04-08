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
import models.{CheckMode, Index, NormalMode}
import pages.consignees._
import pages.consignors._
import pages.routedetails._

class WaypointSpec extends SpecBase {

  ".fromString" - {

    "must return AddConsigneePage when given its normal-mode waypoint" in {

      Waypoint.fromString("add-consignee").value mustEqual AddConsigneePage.waypoint(NormalMode)
    }

    "must return AddConsigneePage when given its check-mode waypoint" in {

      Waypoint.fromString("change-consignee").value mustEqual AddConsigneePage.waypoint(CheckMode)
    }

    "must return AddNotifiedPartyPage when given its normal-mode waypoint" in {

      Waypoint.fromString("add-notified-party").value mustEqual AddNotifiedPartyPage.waypoint(NormalMode)
    }

    "must return AddNotifiedPartyPage when given its check-mode waypoint" in {

      Waypoint.fromString("change-notified-party").value mustEqual AddNotifiedPartyPage.waypoint(CheckMode)
    }

    "must return AddConsignorPage when given its normal-mode waypoint" in {

      Waypoint.fromString("add-consignor").value mustEqual AddConsignorPage.waypoint(NormalMode)
    }

    "must return AddConsignorPage when given its check-mode waypoint" in {

      Waypoint.fromString("change-consignor").value mustEqual AddConsignorPage.waypoint(CheckMode)
    }

    "must return CheckConsigneesAndNotifiedPartiesPage when given its waypoint" in {

      Waypoint.fromString("check-consignees-notified-parties").value.
        mustEqual(CheckConsigneesAndNotifiedPartiesPage.waypoint)
    }

    "must return CheckConsigneePage when given its waypoint" in {

      Waypoint.fromString("check-consignee-1").value mustEqual CheckConsigneePage(Index(0)).waypoint
    }

    "must return CheckConsignorPage when given its waypoint" in {

      Waypoint.fromString("check-consignor-1").value mustEqual CheckConsignorPage(Index(0)).waypoint
    }

    "must return CheckNotifiedPartyPage when given its waypoint" in {

      Waypoint.fromString("check-notified-party-1").value mustEqual CheckNotifiedPartyPage(Index(0)).waypoint
    }
    
    "must return CheckRouteDetailsPag when given its waypoint" in {
      
      Waypoint.fromString("check-route-details").value mustEqual CheckRouteDetailsPage.waypoint
    }

    "must return AddCountryEnRoutePage when given its normal-mode waypoint" in {

      Waypoint.fromString("add-country-en-route").value mustEqual AddCountryEnRoutePage.waypoint(NormalMode)
    }

    "must return AddCountryEnRoutePage when given its check-mode waypoint" in {

      Waypoint.fromString("change-country-en-route").value mustEqual AddCountryEnRoutePage.waypoint(CheckMode)
    }

    "must return AddPlaceOfLoadingPage when given its normal-mode waypoint" in {

      Waypoint.fromString("add-place-of-loading").value mustEqual AddPlaceOfLoadingPage.waypoint(NormalMode)
    }

    "must return AddPlaceOfLoadingPage when given its check-mode waypoint" in {

      Waypoint.fromString("change-place-of-loading").value mustEqual AddPlaceOfLoadingPage.waypoint(CheckMode)
    }

    "must return AddPlaceOfUnloadingPage when given its normal-mode waypoint" in {

      Waypoint.fromString("add-place-of-unloading").value mustEqual AddPlaceOfUnloadingPage.waypoint(NormalMode)
    }

    "must return AddPlaceOfUnloadingPage when given its check-mode waypoint" in {

      Waypoint.fromString("change-place-of-unloading").value mustEqual AddPlaceOfUnloadingPage.waypoint(CheckMode)
    }
  }
}
