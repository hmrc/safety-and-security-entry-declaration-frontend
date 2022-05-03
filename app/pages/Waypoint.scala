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
import pages.goods._
import pages.predec.CheckPredecPage
import pages.routedetails.{AddCountryEnRoutePage, AddPlaceOfLoadingPage, AddPlaceOfUnloadingPage, CheckRouteDetailsPage}
import pages.transport.{AddOverallDocumentPage, AddSealPage, CheckTransportPage}

case class Waypoint (
  page: WaypointPage,
  mode: Mode,
  urlFragment: String
)

object Waypoint {
  
  private def staticFragments: Map[String, Waypoint] =
    Map(
      AddConsigneePage.normalModeUrlFragment -> AddConsigneePage.waypoint(NormalMode),
      AddConsigneePage.checkModeUrlFragment -> AddConsigneePage.waypoint(CheckMode),
      AddNotifiedPartyPage.checkModeUrlFragment -> AddNotifiedPartyPage.waypoint(CheckMode),
      AddNotifiedPartyPage.checkModeUrlFragment -> AddNotifiedPartyPage.waypoint(CheckMode),
      AddNotifiedPartyPage.normalModeUrlFragment -> AddNotifiedPartyPage.waypoint(NormalMode),
      AddConsignorPage.normalModeUrlFragment -> AddConsignorPage.waypoint(NormalMode),
      AddConsignorPage.checkModeUrlFragment -> AddConsignorPage.waypoint(CheckMode),
      AddCountryEnRoutePage.normalModeUrlFragment -> AddCountryEnRoutePage.waypoint(NormalMode),
      AddCountryEnRoutePage.checkModeUrlFragment -> AddCountryEnRoutePage.waypoint(CheckMode),
      AddPlaceOfLoadingPage.normalModeUrlFragment -> AddPlaceOfLoadingPage.waypoint(NormalMode),
      AddPlaceOfLoadingPage.checkModeUrlFragment -> AddPlaceOfLoadingPage.waypoint(CheckMode),
      AddPlaceOfUnloadingPage.normalModeUrlFragment -> AddPlaceOfUnloadingPage.waypoint(NormalMode),
      AddPlaceOfUnloadingPage.checkModeUrlFragment -> AddPlaceOfUnloadingPage.waypoint(CheckMode),
      AddGoodsPage.normalModeUrlFragment -> AddGoodsPage.waypoint(NormalMode),
      AddGoodsPage.checkModeUrlFragment -> AddGoodsPage.waypoint(CheckMode),
      AddSealPage.normalModeUrlFragment -> AddSealPage.waypoint(NormalMode),
      AddSealPage.checkModeUrlFragment -> AddSealPage.waypoint(CheckMode),
      AddOverallDocumentPage.normalModeUrlFragment -> AddOverallDocumentPage.waypoint(NormalMode),
      AddOverallDocumentPage.checkModeUrlFragment -> AddOverallDocumentPage.waypoint(CheckMode),
      CheckPredecPage.urlFragment -> CheckPredecPage.waypoint,
      CheckRouteDetailsPage.urlFragment -> CheckRouteDetailsPage.waypoint,
      CheckConsigneesAndNotifiedPartiesPage.urlFragment -> CheckConsigneesAndNotifiedPartiesPage.waypoint,
      CheckTransportPage.urlFragment -> CheckTransportPage.waypoint,
    )

  def fromString(s: String): Option[Waypoint] =
    staticFragments
      .get(s)
      .orElse(
        CheckConsigneePage.waypointFromString(s)
          .orElse(CheckNotifiedPartyPage.waypointFromString(s))
          .orElse(CheckConsignorPage.waypointFromString(s))
          .orElse(CheckGoodsItemPage.waypointFromString(s))
          .orElse(CheckPackageItemPage.waypointFromString(s))
          .orElse(AddItemContainerNumberPage.waypointFromString(s))
          .orElse(AddPackagePage.waypointFromString(s))
          .orElse(AddDocumentPage.waypointFromString(s))
      )
}
