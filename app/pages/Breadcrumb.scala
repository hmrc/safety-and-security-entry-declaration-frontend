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

import models.UserAnswers
import pages.consignees.{AddConsigneePage, AddNotifiedPartyPage, CheckConsigneePage, CheckConsigneesAndNotifiedPartiesPage, CheckNotifiedPartyPage}

sealed trait Breadcrumb[A] extends DataPage[A] {

  val urlFragment: String
}

object Breadcrumb {

  def fromString(s: String): Option[Breadcrumb[_]] =
    s match {
      case AddConsigneePage.urlFragment =>
        Some(AddConsigneePage)

      case AddNotifiedPartyPage.urlFragment =>
        Some(AddNotifiedPartyPage)

      case CheckConsigneesAndNotifiedPartiesPage.urlFragment =>
        Some(CheckConsigneesAndNotifiedPartiesPage)

      case x =>
        CheckConsigneePage.fromString(x) orElse
          CheckNotifiedPartyPage.fromString(x) orElse
          None
    }
}

trait CheckAnswersPage extends Breadcrumb[Nothing]

trait AddItemPage extends Breadcrumb[Boolean] {

  override protected def updateBreadcrumbs(
    breadcrumbs: Breadcrumbs,
    target: DataPage[_],
    answers: UserAnswers
  ): Breadcrumbs =
    answers.get(this).map {
      answer =>
        if (answer && breadcrumbs.list.nonEmpty) { breadcrumbs.push(this) }
        else                                     { super.updateBreadcrumbs(breadcrumbs, target, answers) }
    }.getOrElse(throw new Exception(s"Could not find an answer for ${this.toString}"))
}
