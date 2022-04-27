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
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{JourneyRecoveryPage, Page, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.consignees.{DeriveNumberOfConsignees, DeriveNumberOfNotifiedParties}

final case class ConsignorPage(itemIndex: Index) extends GoodsItemQuestionPage[Int] {

  override def path: JsPath = JsPath \ "goodsItems" \ itemIndex.position \ toString

  override def toString: String = "consignorKey"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.ConsignorController.onPageLoad(waypoints, lrn, itemIndex)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page = {

    val c = answers.get(DeriveNumberOfConsignees).getOrElse(0)
    val n = answers.get(DeriveNumberOfNotifiedParties).getOrElse(0)

    if (c > 0 && n > 0) ConsigneeKnownPage(itemIndex)
    else if (c > 1)     ConsigneePage(itemIndex)
    else if (n > 1)     NotifiedPartyPage(itemIndex)
    else if (c == 1)    ConsigneePage(itemIndex).nextPage(waypoints, answers)
    else if (n == 1)    NotifiedPartyPage(itemIndex).nextPage(waypoints, answers)
    else                JourneyRecoveryPage
  }
}
