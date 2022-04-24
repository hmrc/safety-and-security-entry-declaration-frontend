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
import pages.{NonEmptyWaypoints, Page, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case class AddMarkOrNumberPage(itemIndex: Index, packageIndex: Index) extends PackageQuestionPage[Boolean] {

  override def path: JsPath =
    JsPath \ "goodsItems" \ itemIndex.position \ "packages" \ packageIndex.position \ toString

  override def toString: String = "addMarkOrNumber"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    goodsRoutes.AddMarkOrNumberController.onPageLoad(waypoints, lrn, itemIndex, packageIndex)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true => MarkOrNumberPage(itemIndex, packageIndex)
      case false => CheckPackageItemPage(itemIndex, packageIndex)
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true =>
        answers.get(MarkOrNumberPage(itemIndex, packageIndex))
        .map(_ => waypoints.next.page)
        .getOrElse(MarkOrNumberPage(itemIndex, packageIndex))

      case false =>
        waypoints.next.page
    }.orRecover

  override def cleanup(value: Option[Boolean], userAnswers: UserAnswers): Try[UserAnswers] =
    if (value.contains(false)) {
      userAnswers.remove(MarkOrNumberPage(itemIndex, packageIndex))
    } else {
      super.cleanup(value, userAnswers)
    }
}
