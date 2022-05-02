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
import models.TransportMode._
import models.{Country, LocalReferenceNumber, UserAnswers}
import pages.{NonEmptyWaypoints, Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object NationalityOfTransportPage extends QuestionPage[Country] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "nationalityOfTransport"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.NationalityOfTransportController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(TransportModePage).map {
      case Air => AirIdentityPage
      case Maritime => MaritimeIdentityPage
      case Rail => RailIdentityPage
      case Road => RoadIdentityPage
      case RoroAccompanied => RoroAccompaniedIdentityPage
      case RoroUnaccompanied => RoroUnaccompaniedIdentityPage
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(TransportModePage).map {
      case Road =>
        answers.get(RoadIdentityPage)
          .map(_ => waypoints.next.page)
          .getOrElse(RoadIdentityPage)

      case RoroAccompanied =>
        answers.get(RoroAccompaniedIdentityPage)
          .map(_ => waypoints.next.page)
          .getOrElse(RoroAccompaniedIdentityPage)

      case RoroUnaccompanied =>
        answers.get(RoroUnaccompaniedIdentityPage)
          .map(_ => waypoints.next.page)
          .getOrElse(RoroUnaccompaniedIdentityPage)

      case Air | Maritime | Rail =>
        waypoints.next.page
    }.orRecover
}
