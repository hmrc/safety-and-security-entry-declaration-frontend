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
import models.{Index, LocalReferenceNumber, NormalMode, ProvideGrossWeight, UserAnswers}
import pages.{Page, Waypoints}
import pages.predec.ProvideGrossWeightPage
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.DeriveNumberOfContainers

case class AnyShippingContainersPage(itemIndex: Index) extends GoodsItemQuestionPage[Boolean] {

  override def path: JsPath = JsPath \ "goodsItems" \ itemIndex.position \ toString

  override def toString: String = "shippingContainers"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    goodsRoutes.AnyShippingContainersController.onPageLoad(waypoints, lrn, itemIndex)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        ItemContainerNumberPage(itemIndex, Index(0))

      case false =>
        answers.get(ProvideGrossWeightPage).map {
          case ProvideGrossWeight.Overall => KindOfPackagePage(itemIndex, Index(0))
          case ProvideGrossWeight.PerItem => GoodsItemGrossWeightPage(itemIndex)
        }.orRecover
    }.orRecover
}
