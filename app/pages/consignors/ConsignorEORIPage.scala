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

package pages.consignors

import controllers.consignees.{routes => consigneeRoutes}
import controllers.routes
import models.{GbEori, Index, NormalMode, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

case class ConsignorEORIPage(index: Index) extends QuestionPage[GbEori] {

  override def path: JsPath = JsPath \ "consignors" \ index.position \ toString

  override def toString: String = "consignorEORI"

  override protected def navigateInNormalMode(answers: UserAnswers): Call = {
    consigneeRoutes.ConsigneeKnownController.onPageLoad(NormalMode, answers.lrn, index)
  }
}
