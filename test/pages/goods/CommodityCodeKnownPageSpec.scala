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

class CommodityCodeKnownPageSpec extends SpecBase with PageBehaviours {

  "CommodityCodeKnownPage" - {

    beRetrievable[Boolean](CommodityCodeKnownPage(index))

    beSettable[Boolean](CommodityCodeKnownPage(index))

    beRemovable[Boolean](CommodityCodeKnownPage(index))

    "must navigate in Normal Mode" - {

      "to CommodityCodePage when answer is yes" in {

        val answers = emptyUserAnswers.set(CommodityCodeKnownPage(index), true).success.value

        CommodityCodeKnownPage(index)
          .navigate(NormalMode, answers)
          .mustEqual(goodsRoutes.CommodityCodeController.onPageLoad(NormalMode, answers.lrn, index))
      }

      "to GoodsDescriptionPage when answer is no" in {

        val answers = emptyUserAnswers.set(CommodityCodeKnownPage(index), false).success.value

        CommodityCodeKnownPage(index)
          .navigate(NormalMode, answers)
          .mustEqual(goodsRoutes.GoodsDescriptionController.onPageLoad(NormalMode, answers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        CommodityCodeKnownPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}