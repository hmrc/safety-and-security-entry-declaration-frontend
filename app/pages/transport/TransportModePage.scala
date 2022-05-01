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
import models.{LocalReferenceNumber, TransportMode, UserAnswers}
import pages.{NonEmptyWaypoints, Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case object TransportModePage extends QuestionPage[TransportMode] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "transportMode"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.TransportModeController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case Air => AirIdentityPage
      case Maritime => MaritimeIdentityPage
      case Rail => RailIdentityPage
      case Road | RoroAccompanied | RoroUnaccompanied => NationalityOfTransportPage
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case Air =>
        answers.get(AirIdentityPage)
          .map(_ => waypoints.next.page)
          .getOrElse(AirIdentityPage)

      case Maritime =>
        answers.get(MaritimeIdentityPage)
          .map(_ => waypoints.next.page)
          .getOrElse(MaritimeIdentityPage)

      case Rail =>
        answers.get(RailIdentityPage)
          .map(_ => waypoints.next.page)
          .getOrElse(RailIdentityPage)

      case Road | RoroAccompanied | RoroUnaccompanied =>
        answers.get(NationalityOfTransportPage)
          .map(_ => waypoints.next.page)
          .getOrElse(NationalityOfTransportPage)
    }.orRecover

  override def cleanup(value: Option[TransportMode], userAnswers: UserAnswers): Try[UserAnswers] =
    value.map {
      case Air =>
        userAnswers
          .remove(NationalityOfTransportPage)
          .flatMap(_.remove(MaritimeIdentityPage))
          .flatMap(_.remove(RailIdentityPage))
          .flatMap(_.remove(RoadIdentityPage))
          .flatMap(_.remove(RoroAccompaniedIdentityPage))
          .flatMap(_.remove(RoroUnaccompaniedIdentityPage))

      case Maritime =>
        userAnswers
          .remove(NationalityOfTransportPage)
          .flatMap(_.remove(AirIdentityPage))
          .flatMap(_.remove(RailIdentityPage))
          .flatMap(_.remove(RoadIdentityPage))
          .flatMap(_.remove(RoroAccompaniedIdentityPage))
          .flatMap(_.remove(RoroUnaccompaniedIdentityPage))

      case Rail =>
        userAnswers
          .remove(NationalityOfTransportPage)
          .flatMap(_.remove(AirIdentityPage))
          .flatMap(_.remove(MaritimeIdentityPage))
          .flatMap(_.remove(RoadIdentityPage))
          .flatMap(_.remove(RoroAccompaniedIdentityPage))
          .flatMap(_.remove(RoroUnaccompaniedIdentityPage))

      case Road =>
        userAnswers
          .remove(AirIdentityPage)
          .flatMap(_.remove(MaritimeIdentityPage))
          .flatMap(_.remove(RailIdentityPage))
          .flatMap(_.remove(RoroAccompaniedIdentityPage))
          .flatMap(_.remove(RoroUnaccompaniedIdentityPage))

      case RoroAccompanied =>
        userAnswers
          .remove(AirIdentityPage)
          .flatMap(_.remove(MaritimeIdentityPage))
          .flatMap(_.remove(RailIdentityPage))
          .flatMap(_.remove(RoadIdentityPage))
          .flatMap(_.remove(RoroUnaccompaniedIdentityPage))

      case RoroUnaccompanied =>
        userAnswers
          .remove(AirIdentityPage)
          .flatMap(_.remove(MaritimeIdentityPage))
          .flatMap(_.remove(RailIdentityPage))
          .flatMap(_.remove(RoadIdentityPage))
          .flatMap(_.remove(RoroAccompaniedIdentityPage))
    }.getOrElse(super.cleanup(value, userAnswers))
}
