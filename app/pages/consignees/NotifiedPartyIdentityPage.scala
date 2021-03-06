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
import models.TraderIdentity.{GBEORI, NameAddress}
import models.{Index, LocalReferenceNumber, NormalMode, TraderIdentity, UserAnswers}
import pages.{NonEmptyWaypoints, Page, Waypoint, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case class NotifiedPartyIdentityPage(index: Index) extends NotifiedPartyQuestionPage[TraderIdentity] {

  override val addItemWaypoint: Waypoint = AddNotifiedPartyPage.waypoint(NormalMode)

  override def path: JsPath = JsPath \ "notifiedParties" \ index.position \ toString

  override def toString: String = "identity"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneeRoutes.NotifiedPartyIdentityController.onPageLoad(waypoints, lrn, index)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(NotifiedPartyIdentityPage(index)).map {
      case GBEORI => NotifiedPartyEORIPage(index)
      case NameAddress => NotifiedPartyNamePage(index)
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case GBEORI =>
        answers.get(NotifiedPartyEORIPage(index))
          .map(_ => waypoints.next.page)
          .getOrElse(NotifiedPartyEORIPage(index))

      case NameAddress =>
        answers.get(NotifiedPartyNamePage(index))
          .map(_ => waypoints.next.page)
          .getOrElse(NotifiedPartyNamePage(index))
    }.orRecover


  override def cleanup(value: Option[TraderIdentity], userAnswers: UserAnswers): Try[UserAnswers] = {
    value.map {
      case GBEORI =>
        userAnswers
          .remove(NotifiedPartyNamePage(index))
          .flatMap(_.remove(NotifiedPartyAddressPage(index)))

      case NameAddress =>
        userAnswers.remove(NotifiedPartyEORIPage(index))
    }.getOrElse(super.cleanup(value, userAnswers))
  }
}
