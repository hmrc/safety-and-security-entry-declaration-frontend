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

class ShippingContainersPageSpec extends SpecBase with PageBehaviours {

  "ShippingContainersPage" - {
    val container = arbitrary[Container].sample.value


    beRetrievable[Boolean](ShippingContainersPage(index))

    beSettable[Boolean](ShippingContainersPage(index))

    beRemovable[Boolean](ShippingContainersPage(index))

    "must navigate in Normal Mode" - {
      "When we are in a container" - {
        "To container trailer number when we have not entered any containers" in {
          val answers = emptyUserAnswers.set(ShippingContainersPage(index),true).success.value

          ShippingContainersPage(index).navigate(NormalMode, answers)
            .mustEqual(goodsRoutes.ItemContainerNumberController.onPageLoad(NormalMode, answers.lrn, index, Index(0)))
        }

        "To add containers when we have entered some containers" in {
          val answers = emptyUserAnswers.set(ShippingContainersPage(index),true).success.value
            .set(ItemContainerNumberPage(index,index),container).success.value

          ShippingContainersPage(index).navigate(NormalMode, answers)
            .mustEqual(goodsRoutes.AddItemContainerNumberController.onPageLoad(NormalMode, answers.lrn, index))
        }
      }
      "When we are in a trailer" - {
        "to item packaging weight if we have selected weight overall " in {
          val answers = emptyUserAnswers.set(ProvideGrossWeightPage, ProvideGrossWeight.Overall).success.value
            .set(ShippingContainersPage(index),false).success.value

          ShippingContainersPage(index).navigate(NormalMode, answers)
            .mustEqual(goodsRoutes.AddPackageController.onPageLoad(NormalMode, answers.lrn, index))
        }


        "to item gross weight if we have selected weight per item " in {
          val answers = emptyUserAnswers.set(ProvideGrossWeightPage, ProvideGrossWeight.PerItem).success.value
            .set(ShippingContainersPage(index),false).success.value

          ShippingContainersPage(index).navigate(NormalMode, answers)
            .mustEqual(goodsRoutes.GoodsItemGrossWeightController.onPageLoad(NormalMode, answers.lrn, index))
        }
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ShippingContainersPage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
