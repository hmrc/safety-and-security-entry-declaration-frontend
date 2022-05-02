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
import models.ProvideGrossWeight.{Overall, PerItem}
import models.{LocalReferenceNumber, ProvideGrossWeight, UserAnswers}
import pages.{Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case object ProvideGrossWeightPage extends QuestionPage[ProvideGrossWeight] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "provideGrossWeight"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.ProvideGrossWeightController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page = {
    answers.get(this).map {
      case Overall => TotalGrossWeightPage
      case PerItem => CheckPredecPage
    }.orRecover
  }

  override def cleanup(value: Option[ProvideGrossWeight], userAnswers: UserAnswers): Try[UserAnswers] =
    if (value.contains(PerItem)) {
      userAnswers.remove(TotalGrossWeightPage)
    } else {
      super.cleanup(value, userAnswers)
    }
}
