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

import controllers.predec.{routes => predecRoutes}
import controllers.predec.{routes => predecRoutes}
import controllers.routes
import models.{NormalMode, ProvideGrossWeight, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object ProvideGrossWeightPage extends QuestionPage[ProvideGrossWeight] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "provideGrossWeight"

  override protected def navigateInNormalMode(answers: UserAnswers): Call =
    answers.get(ProvideGrossWeightPage) match {
      case Some(ProvideGrossWeight.PerItem) =>
        predecRoutes.CheckPredecController.onPageLoad(answers.lrn)
      case Some(ProvideGrossWeight.Overall) =>
        predecRoutes.TotalGrossWeightController.onPageLoad(NormalMode, answers.lrn)
      case _ =>
        routes.JourneyRecoveryController.onPageLoad()
    }
}
