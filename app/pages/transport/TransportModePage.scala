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
import models.{LocalReferenceNumber, NormalMode, TransportMode, UserAnswers}
import models.TransportMode._
import pages.{QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object TransportModePage extends QuestionPage[TransportMode] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "transportMode"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.TransportModeController.onPageLoad(waypoints, lrn)

//  override protected def navigateInNormalMode(answers: UserAnswers): Call =
//    answers.get(TransportModePage) match {
//      case Some(Air) =>
//        transportRoutes.AirIdentityController.onPageLoad(NormalMode, answers.lrn)
//
//      case Some(Rail) =>
//        transportRoutes.RailIdentityController.onPageLoad(NormalMode, answers.lrn)
//
//      case Some(Maritime) =>
//        transportRoutes.MaritimeIdentityController.onPageLoad(NormalMode, answers.lrn)
//
//      case Some(RoroAccompanied) | Some(RoroUnaccompanied) | Some(Road) =>
//        transportRoutes.NationalityOfTransportController.onPageLoad(NormalMode, answers.lrn)
//
//      case None =>
//        routes.JourneyRecoveryController.onPageLoad()
//    }
}
