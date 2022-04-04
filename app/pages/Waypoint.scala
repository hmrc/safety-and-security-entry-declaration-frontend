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

      case CheckConsigneesAndNotifiedPartiesPage.urlFragment =>
        Some(CheckConsigneesAndNotifiedPartiesPage.waypoint)

      case other =>
        CheckConsigneePage.waypointFromString(other) orElse
          CheckNotifiedPartyPage.waypointFromString(other) orElse
          None
    }
}
