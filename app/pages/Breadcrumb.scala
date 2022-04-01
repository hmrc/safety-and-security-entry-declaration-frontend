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

sealed trait BreadcrumbPage extends Page

trait CheckAnswersPage extends BreadcrumbPage {
  val urlFragment: String

  def breadcrumb: Breadcrumb =
    Breadcrumb(this, CheckMode, urlFragment)
}

trait AddItemPage extends BreadcrumbPage {
  def breadcrumb(mode: Mode): Breadcrumb = {
    Breadcrumb(this, mode, urlFragment(mode))
  }

  private def urlFragment(mode: Mode): String =
    mode match {
      case NormalMode => normalModeUrlFragment
      case CheckMode  => checkModeUrlFragment
    }

  val normalModeUrlFragment: String
  val checkModeUrlFragment: String
}

case class Breadcrumb (
  page: BreadcrumbPage,
  mode: Mode,
  urlFragment: String
)

object Breadcrumb {

  def fromString(s: String): Option[Breadcrumb] =
    s match {
      case AddConsigneePage.checkModeUrlFragment =>
        Some(AddConsigneePage.breadcrumb(CheckMode))

      case AddConsigneePage.normalModeUrlFragment =>
        Some(AddConsigneePage.breadcrumb(NormalMode))

      case AddNotifiedPartyPage.checkModeUrlFragment =>
        Some(AddNotifiedPartyPage.breadcrumb(CheckMode))

      case AddNotifiedPartyPage.normalModeUrlFragment =>
        Some(AddNotifiedPartyPage.breadcrumb(NormalMode))

      case CheckConsigneesAndNotifiedPartiesPage.urlFragment =>
        Some(CheckConsigneesAndNotifiedPartiesPage.breadcrumb)

      case other =>
        CheckConsigneePage.breadcrumbFromString(other) orElse
          CheckNotifiedPartyPage.breadcrumbFromString(other) orElse
          None
    }
}
