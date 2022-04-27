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
import models.{CheckMode, Index, LocalReferenceNumber, NormalMode, ProvideGrossWeight, UserAnswers}
import pages.predec.ProvideGrossWeightPage
import pages.{AddItemPage, NonEmptyWaypoints, Page, QuestionPage, Waypoint, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.goods.DeriveNumberOfContainers

final case class AddItemContainerNumberPage(itemIndex: Index) extends QuestionPage[Boolean] with AddItemPage {

  override val normalModeUrlFragment: String = s"add-container-${itemIndex.position}"
  override val checkModeUrlFragment: String = s"change-container-${itemIndex.position}"

  override def path: JsPath = JsPath \ "addContainerNumber"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.AddItemContainerNumberController.onPageLoad(waypoints, lrn, itemIndex)

  override def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfContainers(itemIndex))
          .map(n => ItemContainerNumberPage(itemIndex, Index(n)))
          .orRecover

      case false =>
        answers.get(ProvideGrossWeightPage).map {
          case ProvideGrossWeight.PerItem => GoodsItemGrossWeightPage(itemIndex)
          case ProvideGrossWeight.Overall => KindOfPackagePage(itemIndex, Index(0))
        }.orRecover
    }.orRecover

  override def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfContainers(itemIndex))
        .map(n => ItemContainerNumberPage(itemIndex, Index(n)))
        .orRecover

      case false =>
        waypoints.next.page
    }.orRecover
}

object AddItemContainerNumberPage {

  def waypointFromString(s: String): Option[Waypoint] = {

    val normalModePattern = """add-container-(\d{1,3})""".r.anchored
    val checkModePattern = """change-container-(\d{1,3})""".r.anchored

    s match {
      case normalModePattern(indexDisplay) =>
        Some(AddItemContainerNumberPage(Index(indexDisplay.toInt - 1)).waypoint(NormalMode))

      case checkModePattern(indexDisplay) =>
        Some(AddItemContainerNumberPage(Index(indexDisplay.toInt - 1)).waypoint(CheckMode))

      case _ =>
        None
    }
  }
}
