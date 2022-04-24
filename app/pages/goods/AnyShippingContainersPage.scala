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

import controllers.goods.routes
import models.{Index, LocalReferenceNumber, ProvideGrossWeight, UserAnswers}
import pages.predec.ProvideGrossWeightPage
import pages.{NonEmptyWaypoints, Page, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.{AllContainersQuery, DeriveNumberOfContainers}

import scala.util.Try

case class AnyShippingContainersPage(itemIndex: Index) extends GoodsItemQuestionPage[Boolean] {

  override def path: JsPath = JsPath \ "goodsItems" \ itemIndex.position \ toString

  override def toString: String = "shippingContainers"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.AnyShippingContainersController.onPageLoad(waypoints, lrn, itemIndex)

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

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers
          .get(DeriveNumberOfContainers(itemIndex)).map {
            case n if n > 0 => waypoints.next.page
            case _ => ItemContainerNumberPage(itemIndex, Index(0))
        }.getOrElse(ItemContainerNumberPage(itemIndex, Index(0)))

      case false =>
        waypoints.next.page
    }.orRecover

  override def cleanup(value: Option[Boolean], userAnswers: UserAnswers): Try[UserAnswers] =
    if (value.contains(false)) {
      userAnswers.remove(AllContainersQuery(itemIndex))
    } else {
      super.cleanup(value, userAnswers)
    }
}
