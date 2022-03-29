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

package pages.consignees

import controllers.consignees.{routes => consigneesRoutes}
import models.{Index, LocalReferenceNumber, NormalMode, UserAnswers}
import pages.DataPage
import pages.Breadcrumbs
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.consignees.DeriveNumberOfConsignees


final case class RemoveConsigneePage(index: Index) extends DataPage[Boolean] {

  override def path: JsPath = JsPath \ "consignees" \ index.position \ toString

  override def toString: String = "removeConsignee"

  override def route(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.RemoveConsigneeController.onPageLoad(breadcrumbs, lrn, index)

  override protected def nextPageNormalMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    answers.get(DeriveNumberOfConsignees).map {
      case n if n > 0 => AddConsigneePage
      case _ => AnyConsigneesKnownPage
    }.getOrElse(AnyConsigneesKnownPage)
}
