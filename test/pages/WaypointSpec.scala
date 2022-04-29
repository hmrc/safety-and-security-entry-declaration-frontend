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
import pages.goods.{AddDocumentPage, AddItemContainerNumberPage, AddPackagePage, CheckGoodsItemPage, CheckPackageItemPage}
import pages.predec.CheckPredecPage
import pages.routedetails._

class WaypointSpec extends SpecBase {

  ".fromString" - {

    "must return CheckPredec when given its waypoint" in {

      Waypoint.fromString("check-predec").value mustEqual CheckPredecPage.waypoint
    }

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

      Waypoint.fromString("add-country").value mustEqual AddCountryEnRoutePage.waypoint(NormalMode)
    }

    "must return AddCountryEnRoutePage when given its check-mode waypoint" in {

      Waypoint.fromString("change-country").value mustEqual AddCountryEnRoutePage.waypoint(CheckMode)
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

    "must return Check Goods Item when given its waypoint" in {

      Waypoint.fromString("check-item-1").value mustEqual CheckGoodsItemPage(Index(0)).waypoint
    }

    "must return Check Package Item when given its waypoint" in {

      Waypoint.fromString("check-package-1-1").value mustEqual CheckPackageItemPage(Index(0), Index(0)).waypoint
    }

    "must return Add Document when given its normal-mode waypoint" in {

      Waypoint.fromString("add-document-1").value mustEqual AddDocumentPage(Index(0)).waypoint(NormalMode)
    }

    "must return Add Document when given its check-mode waypoint" in {

      Waypoint.fromString("change-document-1").value mustEqual AddDocumentPage(Index(0)).waypoint(CheckMode)
    }

    "must return Add Container when given its normal-mode waypoint" in {

      Waypoint.fromString("add-container-1").value mustEqual AddItemContainerNumberPage(Index(0)).waypoint(NormalMode)
    }

    "must return Add Container when given its check-mode waypoint" in {

      Waypoint.fromString("change-container-1").value mustEqual AddItemContainerNumberPage(Index(0)).waypoint(CheckMode)
    }

    "must return Add Package when given its normal-mode waypoint" in {

      Waypoint.fromString("add-package-1").value mustEqual AddPackagePage(Index(0)).waypoint(NormalMode)
    }

    "must return Add Package when given its check-mode waypoint" in {

      Waypoint.fromString("change-package-1").value mustEqual AddPackagePage(Index(0)).waypoint(CheckMode)
    }
  }
}
