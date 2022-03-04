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
import models.{CheckMode, Index, NormalMode}
import pages.behaviours.PageBehaviours

class AddGoodsPageSpec extends SpecBase with PageBehaviours {

  "AddGoodsPage" - {

    "must navigate in Normal Mode" - {

      "to Good for the next index if the answer is yes" in {

        val answers = emptyUserAnswers.set(CommodityCodeKnownPage(Index(0)), true).success.value
        
        AddGoodsPage()
          .navigate(NormalMode, answers, addAnother = true)
          .mustEqual(goodsRoutes.CommodityCodeKnownController.onPageLoad(NormalMode, answers.lrn, Index(1)))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddGoodsPage()
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
