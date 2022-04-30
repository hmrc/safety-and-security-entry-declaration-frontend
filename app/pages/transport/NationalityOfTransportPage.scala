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
import models.{Country, LocalReferenceNumber, NormalMode, UserAnswers}
import models.TransportMode._
import pages.{QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object NationalityOfTransportPage extends QuestionPage[Country] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "nationalityOfTransport"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.NationalityOfTransportController.onPageLoad(waypoints, lrn)

//  override protected def navigateInNormalMode(answers: UserAnswers): Call = {
//    answers.get(TransportModePage) map {
//      case Air =>
//        transportRoutes.AirIdentityController.onPageLoad(NormalMode, answers.lrn)
//      case Maritime =>
//        transportRoutes.MaritimeIdentityController.onPageLoad(NormalMode, answers.lrn)
//      case Rail =>
//        transportRoutes.RailIdentityController.onPageLoad(NormalMode, answers.lrn)
//      case Road =>
//        transportRoutes.RoadIdentityController.onPageLoad(NormalMode, answers.lrn)
//      case RoroAccompanied =>
//        transportRoutes.RoroAccompaniedIdentityController.onPageLoad(NormalMode, answers.lrn)
//      case RoroUnaccompanied =>
//        transportRoutes.RoroUnaccompaniedIdentityController.onPageLoad(NormalMode, answers.lrn)
//    } getOrElse {
//      routes.JourneyRecoveryController.onPageLoad()
//    }
//  }
}
