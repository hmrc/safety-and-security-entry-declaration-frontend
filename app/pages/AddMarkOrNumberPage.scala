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
import models.{Index, NormalMode, UserAnswers}
import play.api.libs.json.JsPath
import play.api.mvc.Call

case class AddMarkOrNumberPage(itemIndex: Index, packageIndex: Index)
  extends QuestionPage[Boolean] {

  override def path: JsPath =
    JsPath \ "goodsItems" \ itemIndex.position \ "packages" \ packageIndex.position \ toString

  override def toString: String = "addMarkOrNumber"

  override protected def navigateInNormalMode(answers: UserAnswers): Call =
    answers.get(AddMarkOrNumberPage(itemIndex, packageIndex)) match {
      case Some(true) =>
        routes.MarkOrNumberController.onPageLoad(NormalMode, answers.lrn, itemIndex, packageIndex)
      case Some(false) =>
        routes.CheckPackageItemController.onPageLoad(
          NormalMode,
          answers.lrn,
          itemIndex,
          packageIndex
        )
      case _ => routes.JourneyRecoveryController.onPageLoad()
    }
}
