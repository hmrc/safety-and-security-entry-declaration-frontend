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

package pages.preDeclaration

import controllers.preDeclaration.{routes => preDecRoutes}
import controllers.routes
import models.{NormalMode, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object OverallCrnKnownPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "overallCrnKnown"

  override protected def navigateInNormalMode(answers: UserAnswers): Call =
    answers.get(OverallCrnKnownPage) match {
      case Some(true)  => preDecRoutes.OverallCrnController.onPageLoad(NormalMode, answers.lrn)
      case Some(false) => preDecRoutes.ProvideGrossWeightController.onPageLoad(NormalMode, answers.lrn)
      case _           => routes.JourneyRecoveryController.onPageLoad()
    }
}
