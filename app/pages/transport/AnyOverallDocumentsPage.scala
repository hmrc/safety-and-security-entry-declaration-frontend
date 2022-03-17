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

import controllers.routes
import controllers.transport.{routes => transportRoutes}
import models.{Index, NormalMode, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object AnyOverallDocumentsPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "anyOverallDocuments"

  override protected def navigateInNormalMode(answers: UserAnswers): Call = {
    answers.get(AnyOverallDocumentsPage) map {
      case true =>
        transportRoutes.OverallDocumentController.onPageLoad(NormalMode, answers.lrn, Index(0))
      case _ =>
        transportRoutes.AddAnySealsController.onPageLoad(NormalMode, answers.lrn)
    } getOrElse {
      routes.JourneyRecoveryController.onPageLoad()
    }
  }
}
