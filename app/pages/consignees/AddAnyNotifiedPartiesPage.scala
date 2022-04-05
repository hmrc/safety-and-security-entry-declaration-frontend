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
import pages.{Waypoints, NonEmptyWaypoints, Page, QuestionPage}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.consignees.{AllNotifiedPartiesQuery, DeriveNumberOfConsignees, DeriveNumberOfNotifiedParties}

import scala.util.Try

case object AddAnyNotifiedPartiesPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "addAnyNotifiedParties"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.AddAnyNotifiedPartiesController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true  => NotifiedPartyIdentityPage(Index(0))
      case false => CheckConsigneesAndNotifiedPartiesPage
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfNotifiedParties)
          .map(_ => waypoints.next.page)
          .getOrElse(NotifiedPartyIdentityPage(Index(0)))

      case false =>
        answers.get(DeriveNumberOfConsignees)
          .map(_ => waypoints.next.page)
          .getOrElse(AnyConsigneesKnownPage)
    }.orRecover

  override def cleanup(value: Option[Boolean], userAnswers: UserAnswers): Try[UserAnswers] =
    value.map {
      case true  => super.cleanup(value, userAnswers)
      case false => userAnswers.remove(AllNotifiedPartiesQuery)
    }.getOrElse(super.cleanup(value, userAnswers))
}
