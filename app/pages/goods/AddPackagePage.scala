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
import controllers.routes
import models.{CheckMode, GrossWeight, Index, Mode, NormalMode, UserAnswers}
import pages.Page
import pages.preDeclaration.{GrossWeightPage, OverallCrnKnownPage}
import play.api.mvc.Call
import queries.DeriveNumberOfPackages

case class AddPackagePage(itemIndex: Index) extends Page {

  def navigate(mode: Mode, answers: UserAnswers, itemIndex: Index, addAnother: Boolean): Call =
    if (addAnother) {
      answers.get(DeriveNumberOfPackages(itemIndex)) match {
        case Some(size) =>
          goodsRoutes.KindOfPackageController.onPageLoad(mode, answers.lrn, itemIndex, Index(size))
        case None => routes.JourneyRecoveryController.onPageLoad()
      }
    } else {
      mode match {
        case NormalMode =>
          (answers.get(GrossWeightPage), answers.get(OverallCrnKnownPage)) match {
            case (Some(GrossWeight.PerItem), _) =>
              goodsRoutes.GoodsItemGrossWeightController.onPageLoad(NormalMode, answers.lrn, itemIndex)
            case (_, Some(true)) =>
              goodsRoutes.AddAnyDocumentsController.onPageLoad(NormalMode, answers.lrn, itemIndex)
            case (_, Some(false)) =>
              goodsRoutes.GoodsItemCrnKnownController.onPageLoad(NormalMode, answers.lrn, itemIndex)
            case _ => routes.JourneyRecoveryController.onPageLoad()
          }

        case CheckMode =>
          routes.CheckYourAnswersController.onPageLoad(answers.lrn)
      }
    }
}
