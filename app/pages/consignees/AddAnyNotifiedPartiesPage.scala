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
import controllers.routes
import models.{Index, NormalMode, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object AddAnyNotifiedPartiesPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "addAnyNotifiedParties"

  override def navigateInNormalMode(answers: UserAnswers): Call =
    answers.get(AddAnyNotifiedPartiesPage) match {
      case Some(true) =>
        consigneesRoutes.NotifiedPartyIdentityController.onPageLoad(NormalMode, answers.lrn, Index(0))
      case Some(false) =>
        consigneesRoutes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(answers.lrn)
      case None =>
        routes.JourneyRecoveryController.onPageLoad()
    }
}
