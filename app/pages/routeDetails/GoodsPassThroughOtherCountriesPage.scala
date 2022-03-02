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

package pages.routeDetails

import controllers.routeDetails.{routes => routeDetailRoutes}
import controllers.routes
import models.{Index, NormalMode, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object GoodsPassThroughOtherCountriesPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "goodsPassThroughOtherCountries"

  override protected def navigateInNormalMode(answers: UserAnswers): Call = {
    answers.get(GoodsPassThroughOtherCountriesPage) match {
      case Some(true) =>
        routeDetailRoutes.CountryEnRouteController.onPageLoad(NormalMode, answers.lrn, Index(0))
      case Some(false) =>
        routeDetailRoutes.CustomsOfficeOfFirstEntryController.onPageLoad(NormalMode, answers.lrn)
      case None => routes.JourneyRecoveryController.onPageLoad()
    }
  }
}