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
import controllers.consignors.{routes => consignorRoutes}
import controllers.goods.{routes => goodsRoutes}
import controllers.{routes => baseRoutes}
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class DangerousGoodPageSpec extends SpecBase with PageBehaviours {

  "DangerousGoodPage" - {

    beRetrievable[Boolean](DangerousGoodPage(index))

    beSettable[Boolean](DangerousGoodPage(index))

    beRemovable[Boolean](DangerousGoodPage(index))

    "must navigate in Normal Mode" - {

      "to dangerous good code page when answer is yes" in {

        val answers = emptyUserAnswers.set(DangerousGoodPage(index), true).success.value

        DangerousGoodPage(index)
          .navigate(NormalMode, answers)
          .mustEqual(goodsRoutes.DangerousGoodCodeController.onPageLoad(NormalMode, answers.lrn, index))
      }

      "to consignor identity page when answer is no" in {

        val answers = emptyUserAnswers.set(DangerousGoodPage(index), false).success.value

        DangerousGoodPage(index)
          .navigate(NormalMode, answers)
          .mustEqual(consignorRoutes.ConsignorIdentityController.onPageLoad(NormalMode, answers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        DangerousGoodPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(baseRoutes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
