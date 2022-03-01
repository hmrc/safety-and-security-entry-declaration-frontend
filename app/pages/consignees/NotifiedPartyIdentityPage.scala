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

import controllers.consignees.{routes => consigneeRoutes}
import controllers.routes
import models.NotifiedPartyIdentity.{GBEORI, NameAddress}
import models.{Index, NormalMode, NotifiedPartyIdentity, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

case class NotifiedPartyIdentityPage(index: Index) extends QuestionPage[NotifiedPartyIdentity] {

  override def path: JsPath = JsPath \ "goodsItems" \ index.position \ toString

  override def toString: String = "identity"

  override protected def navigateInNormalMode(answers: UserAnswers): Call = {
    answers.get(NotifiedPartyIdentityPage(index)) match {
      case Some(GBEORI) =>
        consigneeRoutes.NotifiedPartyEORIController.onPageLoad(NormalMode, answers.lrn, index)
      case Some(NameAddress) =>
        consigneeRoutes.NotifiedPartyNameController.onPageLoad(NormalMode, answers.lrn, index)
      case None => routes.JourneyRecoveryController.onPageLoad()
    }
  }
}
