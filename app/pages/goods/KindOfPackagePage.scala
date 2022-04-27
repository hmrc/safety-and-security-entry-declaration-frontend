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

import controllers.goods.routes
import models.KindOfPackage.{bulkKindsOfPackage, unpackedKindsOfPackage}
import models.{Index, KindOfPackage, LocalReferenceNumber, UserAnswers}
import pages.{NonEmptyWaypoints, Page, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case class KindOfPackagePage(itemIndex: Index, packageIndex: Index) extends PackageQuestionPage[KindOfPackage] {

  override def path: JsPath =
    JsPath \ "goodsItems" \ itemIndex.position \ "packages" \ packageIndex.position \ toString

  override def toString: String = "kindOfPackage"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.KindOfPackageController.onPageLoad(waypoints, lrn, itemIndex, packageIndex)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case b if bulkKindsOfPackage.contains(b) =>
        AddMarkOrNumberPage(itemIndex, packageIndex)

      case u if unpackedKindsOfPackage.contains(u) =>
        NumberOfPiecesPage(itemIndex, packageIndex)

      case _ =>
        NumberOfPackagesPage(itemIndex, packageIndex)
    }.orRecover

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case b if bulkKindsOfPackage.contains(b) =>
        answers.get(AddMarkOrNumberPage(itemIndex, packageIndex))
          .map(_ => waypoints.next.page)
          .getOrElse(AddMarkOrNumberPage(itemIndex, packageIndex))

      case u if unpackedKindsOfPackage.contains(u) =>
        answers.get(NumberOfPiecesPage(itemIndex, packageIndex))
          .map(_ => waypoints.next.page)
          .getOrElse(NumberOfPiecesPage(itemIndex, packageIndex))

      case _ =>
        answers.get(NumberOfPackagesPage(itemIndex, packageIndex))
          .map(_ => waypoints.next.page)
          .getOrElse(NumberOfPackagesPage(itemIndex, packageIndex))
    }.orRecover

  override def cleanup(value: Option[KindOfPackage], userAnswers: UserAnswers): Try[UserAnswers] =
    value.map {
      case b if bulkKindsOfPackage.contains(b) =>
        userAnswers
          .remove(NumberOfPackagesPage(itemIndex, packageIndex))
          .flatMap(_.remove(NumberOfPiecesPage(itemIndex, packageIndex)))

      case u if unpackedKindsOfPackage.contains(u) =>
        userAnswers.remove(NumberOfPackagesPage(itemIndex, packageIndex))

      case _ =>
        userAnswers
          .remove(NumberOfPiecesPage(itemIndex, packageIndex))
          .flatMap(_.remove(AddMarkOrNumberPage(itemIndex, packageIndex)))
    }.getOrElse(super.cleanup(value, userAnswers))
}
