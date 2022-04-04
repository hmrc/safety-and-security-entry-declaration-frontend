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

import controllers.consignees.{routes => consigneeRoutes}
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{Waypoints, NonEmptyWaypoints, Page, QuestionPage}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.consignees.{AllConsigneesQuery, DeriveNumberOfConsignees, DeriveNumberOfNotifiedParties}

import scala.util.Try

case object AnyConsigneesKnownPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "anyConsigneesKnown"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneeRoutes.AnyConsigneesKnownController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(AnyConsigneesKnownPage).map {
      case true => ConsigneeIdentityPage(Index(0))
      case false => NotifiedPartyIdentityPage(Index(0))
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfConsignees)
          .map(_ => waypoints.current.page)
          .getOrElse(ConsigneeIdentityPage(Index(0)))

      case false =>
        answers.get(DeriveNumberOfNotifiedParties)
          .map(_ => waypoints.current.page)
          .getOrElse(NotifiedPartyIdentityPage(Index(0)))
    }.orRecover

  override def cleanup(value: Option[Boolean], userAnswers: UserAnswers): Try[UserAnswers] = {
    value.map {
      case false => userAnswers.remove(AllConsigneesQuery)
      case true => super.cleanup(value, userAnswers)
    }.getOrElse(super.cleanup(value, userAnswers))
  }
}
