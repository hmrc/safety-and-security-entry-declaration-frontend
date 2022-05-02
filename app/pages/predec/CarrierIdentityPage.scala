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

package pages.predec

import controllers.predec.routes
import models.TraderIdentity.{GBEORI, NameAddress}
import models.{LocalReferenceNumber, TraderIdentity, UserAnswers}
import pages.{NonEmptyWaypoints, Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case object CarrierIdentityPage extends QuestionPage[TraderIdentity] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "carrierIdentity"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.CarrierIdentityController.onPageLoad(waypoints, lrn)

  override def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case GBEORI => CarrierEORIPage
      case NameAddress => CarrierNamePage
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case GBEORI =>
        answers.get(CarrierEORIPage)
          .map(_ => waypoints.next.page)
          .getOrElse(CarrierEORIPage)

      case NameAddress =>
        answers.get(CarrierNamePage)
          .map(_ => waypoints.next.page)
          .getOrElse(CarrierNamePage)
    }.orRecover

  override def cleanup(value: Option[TraderIdentity], userAnswers: UserAnswers): Try[UserAnswers] = {
    value.map {
      case GBEORI =>
        userAnswers
          .remove(CarrierNamePage)
          .flatMap(_.remove(CarrierAddressPage))

      case NameAddress =>
        userAnswers.remove(CarrierEORIPage)
    }.getOrElse(super.cleanup(value, userAnswers))
  }
}
