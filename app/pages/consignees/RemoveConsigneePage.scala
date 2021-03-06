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
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{Page, Waypoints}
import play.api.mvc.Call
import queries.consignees.DeriveNumberOfConsignees


final case class RemoveConsigneePage(index: Index) extends Page {

  override def toString: String = "removeConsignee"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.RemoveConsigneeController.onPageLoad(waypoints, lrn, index)

  override def nextPage(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(DeriveNumberOfConsignees).map {
      case n if n > 0 => AddConsigneePage
      case _ => AnyConsigneesKnownPage
    }.getOrElse(AnyConsigneesKnownPage)
}
