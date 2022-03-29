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

package pages.consignees

import controllers.consignees.{routes => consigneesRoutes}
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{Breadcrumbs, DataPage}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.consignees.{DeriveNumberOfConsignees, DeriveNumberOfNotifiedParties}

case object AddAnyNotifiedPartiesPage extends DataPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "addAnyNotifiedParties"

  override def route(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.AddAnyNotifiedPartiesController.onPageLoad(breadcrumbs, lrn)

  override protected def nextPageNormalMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    answers.get(this).map {
      case true  => NotifiedPartyIdentityPage(Index(0))
      case false => CheckConsigneesAndNotifiedPartiesPage
    }.orRecover

  override protected def nextPageCheckMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfNotifiedParties).map {
          case n if n > 0 => CheckConsigneesAndNotifiedPartiesPage
          case _ => NotifiedPartyIdentityPage(Index(0))
        }.getOrElse(NotifiedPartyIdentityPage(Index(0)))

      case false =>
        answers.get(DeriveNumberOfConsignees).map {
          case n if n > 0 => CheckConsigneesAndNotifiedPartiesPage
          case _ => AnyConsigneesKnownPage
        }.getOrElse(AnyConsigneesKnownPage)
    }.orRecover
}
