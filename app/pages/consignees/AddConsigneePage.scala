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
import queries.consignees.DeriveNumberOfConsignees

case object AddConsigneePage extends QuestionPage[Boolean] with AddItemPage {

  override val normalModeUrlFragment: String = "add-consignee"
  override val checkModeUrlFragment: String = "change-consignee"

  override def path: JsPath = JsPath \ "addConsignee"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.AddConsigneeController.onPageLoad(waypoints, lrn)

  override def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfConsignees)
          .map(n => ConsigneeIdentityPage(Index(n)))
          .orRecover

      case false =>
        AddAnyNotifiedPartiesPage
    }.orRecover

  override def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfConsignees)
          .map(n => ConsigneeIdentityPage(Index(n)))
          .orRecover

      case false =>
        waypoints.next.page
    }.orRecover
}
