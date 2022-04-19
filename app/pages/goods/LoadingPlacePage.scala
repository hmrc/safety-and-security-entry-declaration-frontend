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

package pages.goods

import controllers.goods.{routes => goodsRoutes}
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{Page, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.routedetails.DeriveNumberOfPlacesOfUnloading

final case class LoadingPlacePage(itemIndex: Index) extends GoodsItemQuestionPage[Int] {

  override def path: JsPath = JsPath \ "goodsItems" \ itemIndex.position \ toString

  override def toString: String = "loadingPlace"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    goodsRoutes.LoadingPlaceController.onPageLoad(waypoints, lrn, itemIndex)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(DeriveNumberOfPlacesOfUnloading).map {
      case n if n > 1 => UnloadingPlacePage(itemIndex)
      case _ => UnloadingPlacePage(itemIndex).nextPage(waypoints, answers)
    }.orRecover
}
