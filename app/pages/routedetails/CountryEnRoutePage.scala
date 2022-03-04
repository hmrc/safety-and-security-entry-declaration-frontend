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

package pages.routedetails

import controllers.routedetails.{routes => routedetailsRoutes}
import models.{Country, Index, NormalMode, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

case class CountryEnRoutePage(index: Index) extends QuestionPage[Country] {

  override def path: JsPath = JsPath \ toString \ index.position

  override def toString: String = "countriesEnRoute"

  override protected def navigateInNormalMode(answers: UserAnswers): Call =
    routedetailsRoutes.AddCountryEnRouteController.onPageLoad(NormalMode, answers.lrn)
}