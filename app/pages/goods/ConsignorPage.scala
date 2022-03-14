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

package pages.goods

import controllers.goods.{routes => goodsRoutes}
import controllers.routes
import models.{Index, NormalMode, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.consignees.{DeriveNumberOfConsignees, DeriveNumberOfNotifiedParties}

final case class ConsignorPage(itemIndex: Index) extends QuestionPage[Int] {

  override def path: JsPath = JsPath \ "goodsItems" \ itemIndex.position \ toString

  override def toString: String = "consignorKey"

  override def navigateInNormalMode(answers: UserAnswers): Call = {

    val c = answers.get(DeriveNumberOfConsignees).getOrElse(0)
    val n = answers.get(DeriveNumberOfNotifiedParties).getOrElse(0)

    if (c > 0 && n > 0) { goodsRoutes.ConsigneeKnownController.onPageLoad(NormalMode, answers.lrn, itemIndex) }
    else if (c > 1)     { goodsRoutes.ConsigneeController.onPageLoad(NormalMode, answers.lrn, itemIndex) }
    else if (n > 1)     { goodsRoutes.NotifiedPartyController.onPageLoad(NormalMode, answers.lrn, itemIndex) }
    else if (c == 1)    { ConsigneePage(itemIndex).navigate(NormalMode, answers) }
    else if (n == 1)    { NotifiedPartyPage(itemIndex).navigate(NormalMode, answers) }
    else                { routes.JourneyRecoveryController.onPageLoad() }
  }
}
