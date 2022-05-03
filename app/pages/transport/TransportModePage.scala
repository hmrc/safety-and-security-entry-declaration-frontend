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
import queries.Settable

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

      case Road =>
        answers.get(NationalityOfTransportPage)
          .map {
            _ =>
              answers.get(RoadIdentityPage)
                .map(_ => waypoints.next.page)
                .getOrElse(RoadIdentityPage)
          }.getOrElse(NationalityOfTransportPage)

      case RoroAccompanied =>
        answers.get(NationalityOfTransportPage)
          .map {
            _ =>
              answers.get(RoroAccompaniedIdentityPage)
                .map(_ => waypoints.next.page)
                .getOrElse(RoroAccompaniedIdentityPage)
          }.getOrElse(NationalityOfTransportPage)

      case RoroUnaccompanied =>
        answers.get(NationalityOfTransportPage)
          .map {
            _ =>
              answers.get(RoroUnaccompaniedIdentityPage)
                .map(_ => waypoints.next.page)
                .getOrElse(RoroUnaccompaniedIdentityPage)
          }.getOrElse(NationalityOfTransportPage)
    }.orRecover

  override def cleanup(value: Option[TransportMode], userAnswers: UserAnswers): Try[UserAnswers] =
    value.map {
      case Air =>
        userAnswers
          .remove(NationalityOfTransportPage)
          .flatMap(x => cleanOtherModes(AirIdentityPage, x))

      case Maritime =>
        userAnswers
          .remove(NationalityOfTransportPage)
          .flatMap(x => cleanOtherModes(MaritimeIdentityPage, x))

      case Rail =>
        userAnswers
          .remove(NationalityOfTransportPage)
          .flatMap(x => cleanOtherModes(RailIdentityPage, x))

      case Road =>
        cleanOtherModes(RoadIdentityPage, userAnswers)

      case RoroAccompanied =>
        cleanOtherModes(RoroAccompaniedIdentityPage, userAnswers)

      case RoroUnaccompanied =>
        cleanOtherModes(RoroUnaccompaniedIdentityPage, userAnswers)
    }.getOrElse(super.cleanup(value, userAnswers))

  private def cleanOtherModes(pageToKeep: Settable[_], answers: UserAnswers): Try[UserAnswers] = {

    val allPages: List[Settable[_]] = List(
      AirIdentityPage,
      MaritimeIdentityPage,
      RailIdentityPage,
      RoadIdentityPage,
      RoroAccompaniedIdentityPage,
      RoroUnaccompaniedIdentityPage
    )

    allPages
      .filterNot(_ == pageToKeep)
      .foldLeft(Try(answers)) {
        (accumulatedAnswers, page) =>
          accumulatedAnswers.flatMap(_.remove(page))
      }
  }

}
