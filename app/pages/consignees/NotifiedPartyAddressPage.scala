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
import models.{Address, Index, LocalReferenceNumber, UserAnswers}
import pages.{Waypoints, Page}
import play.api.libs.json.JsPath
import play.api.mvc.Call

case class NotifiedPartyAddressPage(index: Index) extends NotifiedPartyQuestionPage[Address] {

  override def path: JsPath = JsPath \ "notifiedParties" \ index.position \ toString

  override def toString: String = "address"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.NotifiedPartyAddressController.onPageLoad(waypoints, lrn, index)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    CheckNotifiedPartyPage(index)
}
