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
import models.{Index, LocalReferenceNumber, Mode, UserAnswers}
import pages.{AddItemPage, QuestionPage, Waypoints}
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

//  def navigate(mode: Mode, answers: UserAnswers, addAnother: Boolean): Call =
//    if (addAnother) {
//      answers.get(DeriveNumberOfSeals) match {
//        case Some(size) => transportRoutes.SealController.onPageLoad(mode, answers.lrn, Index(size))
//        case None => routes.JourneyRecoveryController.onPageLoad()
//      }
//    } else {
//      transportRoutes.CheckTransportController.onPageLoad(answers.lrn)
//    }
}
