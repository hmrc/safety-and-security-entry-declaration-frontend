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

package pages

import controllers.routes
import models.{Index, KindOfPackage, NormalMode, UserAnswers}
import models.KindOfPackage._
import play.api.libs.json.JsPath
import play.api.mvc.Call

case class KindOfPackagePage(itemIndex: Index, packageIndex: Index) extends QuestionPage[KindOfPackage] {

  override def path: JsPath = JsPath \ "goodsItems" \ itemIndex.position \ "packages" \ packageIndex.position \ toString

  override def toString: String = "kindOfPackage"

  override protected def navigateInNormalMode(answers: UserAnswers): Call =
    answers.get(KindOfPackagePage(itemIndex, packageIndex)) match {
      case Some(b) if bulkKindsOfPackage.contains(b) =>
        routes.AddMarkOrNumberController.onPageLoad(NormalMode, answers.lrn, itemIndex, packageIndex)
      case Some(s) if standardKindsOfPackages.contains(s) =>
        routes.NumberOfPackagesController.onPageLoad(NormalMode, answers.lrn, itemIndex, packageIndex)
      case Some(u) if unpackedKindsOfPackage.contains(u) =>
        routes.NumberOfPiecesController.onPageLoad(NormalMode, answers.lrn, itemIndex, packageIndex)
    }
}
