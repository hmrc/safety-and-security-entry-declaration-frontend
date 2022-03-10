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
import queries.consignees.DeriveNumberOfNotifiedParties

final case class RemoveNotifiedPartyPage(index: Index) extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ "notifiedParties" \ index.position \ toString

  override def toString: String = "removeNotifiedParty"

  override protected def navigateInNormalMode(answers: UserAnswers): Call =
    answers.get(DeriveNumberOfNotifiedParties) match {
      case Some(size) if size > 0 =>
        consigneesRoutes.AddNotifiedPartyController.onPageLoad(NormalMode, answers.lrn)
      case _ =>
        answers.get(AnyConsigneesKnownPage) match {
          case Some(true) =>
            consigneesRoutes.AddAnyNotifiedPartiesController.onPageLoad(NormalMode, answers.lrn)
          case Some(false) =>
            consigneesRoutes.NotifiedPartyIdentityController.onPageLoad(NormalMode, answers.lrn, Index(0))
          case None =>
            routes.JourneyRecoveryController.onPageLoad()
        }
    }
}
