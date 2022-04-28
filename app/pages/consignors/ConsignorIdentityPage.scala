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
import models.TraderIdentity.{GBEORI, NameAddress}
import models.{Index, LocalReferenceNumber, TraderIdentity, UserAnswers}
import pages.{NonEmptyWaypoints, Page, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case class ConsignorIdentityPage(index: Index) extends ConsignorQuestionPage[TraderIdentity] {

  override def path: JsPath = JsPath \ "consignors" \ index.position \ toString

  override def toString: String = "consignorIdentity"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.ConsignorIdentityController.onPageLoad(waypoints, lrn, index)

  override def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case GBEORI => ConsignorEORIPage(index)
      case NameAddress => ConsignorNamePage(index)
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case GBEORI =>
        answers.get(ConsignorEORIPage(index))
        .map(_ => waypoints.next.page)
        .getOrElse(ConsignorEORIPage(index))

      case NameAddress =>
        answers.get(ConsignorNamePage(index))
        .map(_ => waypoints.next.page)
        .getOrElse(ConsignorNamePage(index))
    }.orRecover

  override def cleanup(value: Option[TraderIdentity], userAnswers: UserAnswers): Try[UserAnswers] = {
    value.map {
      case GBEORI =>
        userAnswers
          .remove(ConsignorNamePage(index))
          .flatMap(_.remove(ConsignorAddressPage(index)))

      case NameAddress =>
        userAnswers.remove(ConsignorEORIPage(index))
    }.getOrElse(super.cleanup(value, userAnswers))
  }
}
