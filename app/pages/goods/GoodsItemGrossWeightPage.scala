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
import pages.preDeclaration.OverallCrnKnownPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

final case class GoodsItemGrossWeightPage(index: Index) extends QuestionPage[BigDecimal] {

  override def path: JsPath = JsPath \ "goodsItems" \ index.position \ toString

  override def toString: String = "grossWeight"

  override protected def navigateInNormalMode(answers: UserAnswers): Call =
    answers.get(OverallCrnKnownPage) match {
      case Some(true) => goodsRoutes.AddAnyDocumentsController.onPageLoad(NormalMode, answers.lrn, index)
      case Some(false) => goodsRoutes.GoodsItemCrnKnownController.onPageLoad(NormalMode, answers.lrn, index)
      case _ => routes.JourneyRecoveryController.onPageLoad()
    }
}