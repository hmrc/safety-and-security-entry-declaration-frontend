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

import base.SpecBase
import controllers.goods.{routes => goodsRoutes}
import controllers.routes
import models.{CheckMode, Container, Index, NormalMode, ProvideGrossWeight}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.predec.ProvideGrossWeightPage

class AddItemContainerNumberPageSpec extends SpecBase with PageBehaviours {

  "Add Item Container Number Page" - {

    "must navigate in Normal Mode" - {

      "to Item Container Number for the next index if the answer is yes" in {

        val container = arbitrary[Container].sample.value

        val answers = emptyUserAnswers.set(ItemContainerNumberPage(index, Index(0)), container).success.value

        AddItemContainerNumberPage(index)
          .navigate(NormalMode, answers, index, addAnother = true)
          .mustEqual(goodsRoutes.ItemContainerNumberController.onPageLoad(NormalMode, answers.lrn, index, Index(1)))
      }

      "to item packaging when weight is overall" in {

        val container = arbitrary[Container].sample.value

        val answers = emptyUserAnswers.set(ProvideGrossWeightPage, ProvideGrossWeight.Overall).success.value
          .set(ItemContainerNumberPage(index, Index(0)), container).success.value

        AddItemContainerNumberPage(index)
          .navigate(NormalMode, answers, index, addAnother = false)
          .mustEqual(goodsRoutes.AddPackageController.onPageLoad(NormalMode, answers.lrn, index))
      }

      "to item gross weight when weight is per item" in {

        val container = arbitrary[Container].sample.value

        val answers = emptyUserAnswers.set(ProvideGrossWeightPage, ProvideGrossWeight.PerItem).success.value
          .set(ItemContainerNumberPage(index, Index(0)), container).success.value

        AddItemContainerNumberPage(index)
          .navigate(NormalMode, answers, index, addAnother = false)
          .mustEqual(goodsRoutes.GoodsItemGrossWeightController.onPageLoad(NormalMode, answers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddItemContainerNumberPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
