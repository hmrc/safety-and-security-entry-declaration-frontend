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
import models.LodgingPersonType.{Carrier, Representative}
import models.{LocalReferenceNumber, LodgingPersonType, UserAnswers}
import pages.{NonEmptyWaypoints, Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case object LodgingPersonTypePage extends QuestionPage[LodgingPersonType] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "lodgingPersonType"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.LodgingPersonTypeController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case Carrier => ProvideGrossWeightPage
      case Representative => CarrierIdentityPage
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case Carrier =>
        CheckPredecPage

      case Representative =>
        answers.get(CarrierIdentityPage)
        .map(_ => waypoints.next.page)
        .getOrElse(CarrierIdentityPage)
    }.orRecover

  override def cleanup(value: Option[LodgingPersonType], userAnswers: UserAnswers): Try[UserAnswers] =
    if (value.contains(Carrier)) {
      userAnswers
        .remove(CarrierIdentityPage)
        .flatMap(_.remove(CarrierEORIPage))
        .flatMap(_.remove(CarrierNamePage))
        .flatMap(_.remove(CarrierAddressPage))
    } else {
      super.cleanup(value, userAnswers)
    }
}
