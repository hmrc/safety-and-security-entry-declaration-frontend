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

package pages.transport

import controllers.transport.routes
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{AddItemPage, NonEmptyWaypoints, Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.transport.DeriveNumberOfSeals

case object AddSealPage extends QuestionPage[Boolean] with AddItemPage {

  override val normalModeUrlFragment: String = "add-seal"
  override val checkModeUrlFragment: String = "change-seal"

  override def path: JsPath = JsPath \ toString

  override def toString: String = "addSeal"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.AddSealController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfSeals)
          .map(n => SealPage(Index(n)))
          .orRecover

      case false =>
        CheckTransportPage
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
    case true =>
      answers.get(DeriveNumberOfSeals)
        .map(n => SealPage(Index(n)))
        .orRecover

    case false =>
      waypoints.next.page
  }.orRecover
}
