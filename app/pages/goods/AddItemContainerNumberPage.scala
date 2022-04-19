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
import models.{Index, LocalReferenceNumber, ProvideGrossWeight, UserAnswers}
import pages.predec.ProvideGrossWeightPage
import pages.{AddItemPage, Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.DeriveNumberOfContainers

final case class AddItemContainerNumberPage(index: Index) extends QuestionPage[Boolean] with AddItemPage {

  override val normalModeUrlFragment: String = s"add-container-number-${index.position}"
  override val checkModeUrlFragment: String = s"change-container-number-${index.position}"

  override def path: JsPath = JsPath \ "addContainerNumber"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    goodsRoutes.AddItemContainerNumberController.onPageLoad(waypoints, lrn, index)

  override def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfContainers(index))
          .map(n => ItemContainerNumberPage(index, Index(n)))
          .orRecover

      case false =>
        answers.get(ProvideGrossWeightPage).map {
          case ProvideGrossWeight.PerItem => GoodsItemGrossWeightPage(index)
          case ProvideGrossWeight.Overall => KindOfPackagePage(index, Index(0))
        }.orRecover
    }.orRecover
}
