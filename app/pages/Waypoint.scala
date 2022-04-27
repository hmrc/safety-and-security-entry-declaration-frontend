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

import models.{CheckMode, Mode, NormalMode}
import pages.consignees._
import pages.consignors.{AddConsignorPage, CheckConsignorPage}
import pages.goods.{AddDocumentPage, AddGoodsPage, AddItemContainerNumberPage, AddPackagePage, CheckGoodsItemPage, CheckPackageItemPage}
import pages.routedetails.{AddCountryEnRoutePage, AddPlaceOfLoadingPage, AddPlaceOfUnloadingPage, CheckRouteDetailsPage}

case class Waypoint (
  page: WaypointPage,
  mode: Mode,
  urlFragment: String
)

object Waypoint {

  def fromString(s: String): Option[Waypoint] =
    s match {
      case AddConsigneePage.checkModeUrlFragment =>
        Some(AddConsigneePage.waypoint(CheckMode))

      case AddConsigneePage.normalModeUrlFragment =>
        Some(AddConsigneePage.waypoint(NormalMode))

      case AddNotifiedPartyPage.checkModeUrlFragment =>
        Some(AddNotifiedPartyPage.waypoint(CheckMode))

      case AddNotifiedPartyPage.normalModeUrlFragment =>
        Some(AddNotifiedPartyPage.waypoint(NormalMode))

      case AddConsignorPage.normalModeUrlFragment =>
        Some(AddConsignorPage.waypoint(NormalMode))

      case AddConsignorPage.checkModeUrlFragment =>
        Some(AddConsignorPage.waypoint(CheckMode))

      case AddCountryEnRoutePage.normalModeUrlFragment =>
        Some(AddCountryEnRoutePage.waypoint(NormalMode))

      case AddCountryEnRoutePage.checkModeUrlFragment =>
        Some(AddCountryEnRoutePage.waypoint(CheckMode))

      case AddPlaceOfLoadingPage.normalModeUrlFragment =>
        Some(AddPlaceOfLoadingPage.waypoint(NormalMode))

      case AddPlaceOfLoadingPage.checkModeUrlFragment =>
        Some(AddPlaceOfLoadingPage.waypoint(CheckMode))

      case AddPlaceOfUnloadingPage.normalModeUrlFragment =>
        Some(AddPlaceOfUnloadingPage.waypoint(NormalMode))

      case AddPlaceOfUnloadingPage.checkModeUrlFragment =>
        Some(AddPlaceOfUnloadingPage.waypoint(CheckMode))

      case AddGoodsPage.normalModeUrlFragment =>
        Some(AddGoodsPage.waypoint(NormalMode))

      case AddGoodsPage.checkModeUrlFragment =>
        Some(AddGoodsPage.waypoint(CheckMode))

      case CheckRouteDetailsPage.urlFragment =>
        Some(CheckRouteDetailsPage.waypoint)

      case CheckConsigneesAndNotifiedPartiesPage.urlFragment =>
        Some(CheckConsigneesAndNotifiedPartiesPage.waypoint)

      case other =>
        CheckConsigneePage.waypointFromString(other)
          .orElse(CheckNotifiedPartyPage.waypointFromString(other))
          .orElse(CheckConsignorPage.waypointFromString(other))
          .orElse(CheckGoodsItemPage.waypointFromString(other))
          .orElse(CheckPackageItemPage.waypointFromString(other))
          .orElse(AddItemContainerNumberPage.waypointFromString(other))
          .orElse(AddPackagePage.waypointFromString(other))
          .orElse(AddDocumentPage.waypointFromString(other))
    }
}
