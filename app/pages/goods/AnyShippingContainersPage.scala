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
import models.{Index, NormalMode, ProvideGrossWeight, UserAnswers}
import pages.QuestionPage
import pages.predec.ProvideGrossWeightPage
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.DeriveNumberOfContainers

case class AnyShippingContainersPage(itemIndex: Index) extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ "goodsItems" \ itemIndex.position \ toString

  override def toString: String = "shippingContainers"

  override protected def navigateInNormalMode(answers: UserAnswers): Call =
    answers.get(AnyShippingContainersPage(itemIndex)).map{
      case true => {
        answers.get(DeriveNumberOfContainers(itemIndex)) match {
          case Some(size) =>
            goodsRoutes.AddItemContainerNumberController.onPageLoad(NormalMode, answers.lrn, itemIndex)
          case None =>  goodsRoutes.ItemContainerNumberController.onPageLoad(NormalMode,answers.lrn,itemIndex,Index(0))
        }
      }
      case false => {
        answers.get(ProvideGrossWeightPage) match {
          case Some(ProvideGrossWeight.PerItem) =>
            goodsRoutes.GoodsItemGrossWeightController.onPageLoad(NormalMode, answers.lrn,itemIndex)
          case Some(ProvideGrossWeight.Overall) =>
            goodsRoutes.AddPackageController.onPageLoad(NormalMode, answers.lrn,itemIndex)
          case _ =>
            routes.JourneyRecoveryController.onPageLoad()
        }
      }
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())
}
