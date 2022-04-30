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
import models.{CheckMode, Index, LocalReferenceNumber, Mode, NormalMode, UserAnswers}
import pages.{AddItemPage, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.transport.DeriveNumberOfOverallDocuments

case object AddOverallDocumentPage extends QuestionPage[Boolean] with AddItemPage {

  override val normalModeUrlFragment: String = "add-overall-document"
  override val checkModeUrlFragment: String = "change-overall-document"

  override def path: JsPath = JsPath \ toString

  override def toString: String = "addOverallDocument"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.AddOverallDocumentController.onPageLoad(waypoints, lrn)
//
//  def navigate(mode: Mode, answers: UserAnswers, addAnother: Boolean): Call =
//    if (addAnother) {
//      answers.get(DeriveNumberOfOverallDocuments) match {
//        case Some(size) =>
//          transportRoutes.OverallDocumentController.onPageLoad(mode, answers.lrn, Index(size))
//        case None =>
//          routes.JourneyRecoveryController.onPageLoad()
//      }
//    } else {
//      mode match {
//        case NormalMode =>
//          transportRoutes.AddAnySealsController.onPageLoad(NormalMode, answers.lrn)
//        case CheckMode =>
//          routes.CheckYourAnswersController.onPageLoad(answers.lrn)
//      }
//    }
}
