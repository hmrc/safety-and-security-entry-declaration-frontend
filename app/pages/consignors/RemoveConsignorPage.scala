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

package pages.consignors

import controllers.consignors.routes
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{Page, Waypoints}
import play.api.mvc.Call
import queries.consignors.DeriveNumberOfConsignors

final case class RemoveConsignorPage(index: Index) extends Page {

  override def toString: String = "removeConsignor"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.RemoveConsignorController.onPageLoad(waypoints, lrn, index)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(DeriveNumberOfConsignors).map {
      case n if n > 0 => AddConsignorPage
      case _ => ConsignorIdentityPage(Index(0))
    }.getOrElse(ConsignorIdentityPage(Index(0)))
}
