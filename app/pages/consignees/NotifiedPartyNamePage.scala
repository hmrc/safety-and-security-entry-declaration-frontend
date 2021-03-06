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

import controllers.consignees.{routes => consigneeRoutes}
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{Waypoints, NonEmptyWaypoints, Page}
import play.api.libs.json.JsPath
import play.api.mvc.Call

case class NotifiedPartyNamePage(index: Index) extends NotifiedPartyQuestionPage[String] {

  override def path: JsPath = JsPath \ "notifiedParties" \ index.position \ toString

  override def toString: String = "name"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneeRoutes.NotifiedPartyNameController.onPageLoad(waypoints, lrn, index)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    NotifiedPartyAddressPage(index)

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(NotifiedPartyAddressPage(index))
      .map(_ => waypoints.next.page)
      .getOrElse(NotifiedPartyAddressPage(index))
}
