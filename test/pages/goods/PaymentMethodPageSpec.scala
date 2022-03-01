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
import models.{PaymentMethod, CheckMode, NormalMode}
import pages.behaviours.PageBehaviours
class PaymentMethodPageSpec extends SpecBase with PageBehaviours {

  "PaymentMethodPage" - {

    beRetrievable[PaymentMethod](PaymentMethodPage(index))

    beSettable[PaymentMethod](PaymentMethodPage(index))

    beRemovable[PaymentMethod](PaymentMethodPage(index))

    "must navigate in Normal Mode" - {

      "to CYA" in {

        PaymentMethodPage(index)
          .navigate(NormalMode, emptyUserAnswers)

          .mustEqual(goodsRoutes.CheckGoodItemController.onPageLoad(NormalMode,emptyUserAnswers.lrn,index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        PaymentMethodPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
