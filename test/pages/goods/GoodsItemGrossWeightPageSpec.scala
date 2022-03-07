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
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class GoodsItemGrossWeightPageSpec extends SpecBase with PageBehaviours {

  "GoodsItemGrossWeightPage" - {

    beRetrievable[BigDecimal](GoodsItemGrossWeightPage(index))

    beSettable[BigDecimal](GoodsItemGrossWeightPage(index))

    beRemovable[BigDecimal](GoodsItemGrossWeightPage(index))

    "must navigate in Normal Mode" - {

      "to Add Any Documents" in {

        GoodsItemGrossWeightPage(index)
          .navigate(NormalMode, emptyUserAnswers)
          .mustEqual(goodsRoutes.AddAnyDocumentsController.onPageLoad(NormalMode, emptyUserAnswers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        GoodsItemGrossWeightPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
