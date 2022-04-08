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

import controllers.routedetails.routes
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{NonEmptyWaypoints, Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.routedetails.{AllCountriesEnRouteQuery, DeriveNumberOfCountriesEnRoute}

import scala.util.Try

case object GoodsPassThroughOtherCountriesPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "goodsPassThroughOtherCountries"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.GoodsPassThroughOtherCountriesController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true => CountryEnRoutePage(Index(0))
      case false => CustomsOfficeOfFirstEntryPage
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfCountriesEnRoute)
          .map {
            case n if n > 0 => waypoints.next.page
            case _ => CountryEnRoutePage(Index(0))
          }
          .getOrElse(CountryEnRoutePage(Index(0)))

      case false =>
        waypoints.next.page
    }.orRecover

  override def cleanup(value: Option[Boolean], userAnswers: UserAnswers): Try[UserAnswers] =
    if (value.contains(false)) {
      userAnswers.remove(AllCountriesEnRouteQuery)
    } else {
      super.cleanup(value, userAnswers)
    }
}
