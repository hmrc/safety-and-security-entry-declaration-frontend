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
import models.{CheckMode, Index, LocalReferenceNumber, NormalMode, UserAnswers}
import pages.transport.AnyOverallDocumentsPage
import pages.{AddItemPage, NonEmptyWaypoints, Page, QuestionPage, Waypoint, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.DeriveNumberOfPackages

case class AddPackagePage(itemIndex: Index) extends QuestionPage[Boolean] with AddItemPage {

  override val normalModeUrlFragment: String = s"add-package-${itemIndex.position}"
  override val checkModeUrlFragment: String = s"change-package-${itemIndex.position}"

  override def path: JsPath = JsPath \ "addPackage"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    goodsRoutes.AddPackageController.onPageLoad(waypoints, lrn, itemIndex)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfPackages(itemIndex))
          .map(n => KindOfPackagePage(itemIndex, Index(n)))
          .orRecover

      case false =>
        answers.get(AnyOverallDocumentsPage).map {
          case true => AddAnyDocumentsPage(itemIndex)
          case false => DocumentPage(itemIndex, Index(0))
        }.orRecover
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfPackages(itemIndex))
          .map(n => KindOfPackagePage(itemIndex, Index(n)))
          .orRecover

      case false =>
        waypoints.next.page
    }.orRecover
}

object AddPackagePage {

  def waypointFromString(s: String): Option[Waypoint] = {

    val normalModePattern = """add-package-(\d{1,3})""".r.anchored
    val checkModePattern = """change-package-(\d{1,3})""".r.anchored

    s match {
      case normalModePattern(indexDisplay) =>
        Some(AddPackagePage(Index(indexDisplay.toInt - 1)).waypoint(NormalMode))

      case checkModePattern(indexDisplay) =>
        Some(AddPackagePage(Index(indexDisplay.toInt - 1)).waypoint(CheckMode))

      case _ =>
        None
    }
  }
}
