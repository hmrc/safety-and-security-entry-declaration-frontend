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
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.transport.AnyOverallDocumentsPage
import pages.{Page, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.DeriveNumberOfDocuments

final case class RemoveDocumentPage(itemIndex: Index, documentIndex: Index) extends GoodsItemQuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "removeDocument"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    goodsRoutes.RemoveDocumentController.onPageLoad(waypoints, lrn, itemIndex, documentIndex)

  override def nextPage(waypoints: Waypoints, answers: UserAnswers): Page = {

    val noDocumentsRoute: Page =
      answers.get(AnyOverallDocumentsPage).map {
        case true => AddAnyDocumentsPage(itemIndex)
        case false => DocumentPage(itemIndex, Index(0))
      }.orRecover

    answers.get(DeriveNumberOfDocuments(itemIndex)).map {
      case n if n > 0 => AddDocumentPage(itemIndex)
      case _ => noDocumentsRoute
    }.getOrElse(noDocumentsRoute)
  }
}
