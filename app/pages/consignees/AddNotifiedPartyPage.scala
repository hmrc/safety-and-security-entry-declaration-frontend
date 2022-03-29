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
import controllers.routes
import models.{CheckMode, Index, LocalReferenceNumber, Mode, NormalMode, UserAnswers}
import pages.{AddItemPage, Breadcrumb, Breadcrumbs, DataPage, JourneyRecoveryPage, Page}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.consignees.DeriveNumberOfNotifiedParties

case object AddNotifiedPartyPage extends AddItemPage {

  override val urlFragment: String = "add-notified-party"

  override def path: JsPath = JsPath \ "addNotifiedParty"

  override def route(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.AddNotifiedPartyController.onPageLoad(breadcrumbs, lrn)

  override protected def nextPageNormalMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    answers.get(AddNotifiedPartyPage).map {
      case true =>
        answers.get(DeriveNumberOfNotifiedParties).map {
          case n => NotifiedPartyIdentityPage(Index(n))
          case _ => JourneyRecoveryPage
        }.orRecover

      case false =>
        CheckConsigneesAndNotifiedPartiesPage
    }.getOrElse(CheckConsigneesAndNotifiedPartiesPage)
}
