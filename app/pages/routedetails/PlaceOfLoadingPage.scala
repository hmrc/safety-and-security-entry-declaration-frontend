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
import models.{Index, LocalReferenceNumber, PlaceOfLoading, UserAnswers}
import pages.{Page, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

final case class PlaceOfLoadingPage(index: Index) extends PlaceOfLoadingQuestionPage[PlaceOfLoading] {

  override def path: JsPath = JsPath \ toString \ index.position

  override def toString: String = "placesOfLoading"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.PlaceOfLoadingController.onPageLoad(waypoints, lrn, index)

  override def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    AddPlaceOfLoadingPage
}
