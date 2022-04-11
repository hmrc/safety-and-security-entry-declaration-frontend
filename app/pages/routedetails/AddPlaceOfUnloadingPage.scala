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
import pages.{AddItemPage, NonEmptyWaypoints, Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.routedetails.DeriveNumberOfPlacesOfUnloading

case object AddPlaceOfUnloadingPage extends QuestionPage[Boolean] with AddItemPage {
  override val normalModeUrlFragment: String = "add-place-of-unloading"
  override val checkModeUrlFragment: String = "change-place-of-unloading"

  override def path: JsPath = JsPath \ "addPlaceOfUnloading"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.AddPlaceOfUnloadingController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfPlacesOfUnloading)
          .map(n => PlaceOfUnloadingPage(Index(n)))
          .orRecover

      case false =>
        CheckRouteDetailsPage
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfPlacesOfUnloading)
          .map(n => PlaceOfUnloadingPage(Index(n)))
          .orRecover

      case false =>
        waypoints.next.page
    }.orRecover
}
