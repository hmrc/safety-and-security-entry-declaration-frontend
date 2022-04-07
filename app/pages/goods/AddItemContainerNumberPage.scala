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
import models.{Index, Mode, NormalMode, ProvideGrossWeight, UserAnswers}
import pages.Page
import pages.predec.ProvideGrossWeightPage
import play.api.mvc.Call
import queries.DeriveNumberOfContainers

final case class AddItemContainerNumberPage(index: Index) extends Page {

  def navigate(mode: Mode, answers: UserAnswers, itemIndex: Index, addAnother: Boolean): Call = {
    if (addAnother) {
      answers.get(DeriveNumberOfContainers(itemIndex)) match {
        case Some(size) =>
          goodsRoutes.ItemContainerNumberController.onPageLoad(mode, answers.lrn, itemIndex, Index(size))
        case None => routes.JourneyRecoveryController.onPageLoad()
      }
    } else {
      answers.get(ProvideGrossWeightPage) match {
        case Some(ProvideGrossWeight.PerItem) =>
          goodsRoutes.GoodsItemGrossWeightController.onPageLoad(NormalMode, answers.lrn,itemIndex)
        case Some(ProvideGrossWeight.Overall) => goodsRoutes.KindOfPackageController.onPageLoad(NormalMode, answers.lrn, index, Index(0))
        case _ =>
          routes.JourneyRecoveryController.onPageLoad()
      }
    }
  }
}
