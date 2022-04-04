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
import pages.{AddItemPage, Waypoints, NonEmptyWaypoints, Page, QuestionPage}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.consignees.DeriveNumberOfNotifiedParties

case object AddNotifiedPartyPage extends QuestionPage[Boolean] with AddItemPage {

  override val normalModeUrlFragment: String = "add-notified-party"
  override val checkModeUrlFragment: String = "change-notified-party"

  override def path: JsPath = JsPath \ "addNotifiedParty"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.AddNotifiedPartyController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(AddNotifiedPartyPage).map {
      case true =>
        answers.get(DeriveNumberOfNotifiedParties)
          .map(n => NotifiedPartyIdentityPage(Index(n)))
          .orRecover

      case false =>
        CheckConsigneesAndNotifiedPartiesPage
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfNotifiedParties)
          .map(n => NotifiedPartyIdentityPage(Index(n)))
          .orRecover

      case false =>
        CheckConsigneesAndNotifiedPartiesPage
    }.orRecover
}
