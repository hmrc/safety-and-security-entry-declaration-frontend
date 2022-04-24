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
import models.{CheckMode, Index, LocalReferenceNumber, NormalMode, UserAnswers}
import pages.{AddItemPage, NonEmptyWaypoints, Page, QuestionPage, Waypoint, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.DeriveNumberOfDocuments

final case class AddDocumentPage(index: Index) extends QuestionPage[Boolean] with AddItemPage {

  override val normalModeUrlFragment: String = s"add-document-${index.position}"
  override val checkModeUrlFragment: String = s"change-document-${index.position}"

  override def path: JsPath = JsPath \ "addDocument"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.AddDocumentController.onPageLoad(waypoints, lrn, index)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfDocuments(index))
          .map(n => DocumentPage(index, Index(n)))
          .orRecover

      case false =>
        DangerousGoodPage(index)
    }.orRecover

  override def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfDocuments(index))
        .map(n => DocumentPage(index, Index(n)))
        .orRecover

      case false =>
        waypoints.next.page
    }.orRecover
}

object AddDocumentPage {

  def waypointFromString(s: String): Option[Waypoint] = {

    val normalModePattern = """add-document-(\d{1,3})""".r.anchored
    val checkModePattern = """change-document-(\d{1,3})""".r.anchored

    s match {
      case normalModePattern(indexDisplay) =>
        Some(AddDocumentPage(Index(indexDisplay.toInt - 1)).waypoint(NormalMode))

      case checkModePattern(indexDisplay) =>
        Some(AddDocumentPage(Index(indexDisplay.toInt - 1)).waypoint(CheckMode))

      case _ =>
        None
    }
  }
}
