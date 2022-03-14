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

class ConsigneeKnownPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeKnownPage" - {

    beRetrievable[Boolean](ConsigneeKnownPage(index))

    beSettable[Boolean](ConsigneeKnownPage(index))

    beRemovable[Boolean](ConsigneeKnownPage(index))

    "must navigate in Normal Mode" - {

      "to Consignee when the answer is yes" in {

        val answers = emptyUserAnswers.set(ConsigneeKnownPage(index), true).success.value

        ConsigneeKnownPage(index).navigate(NormalMode, answers)
          .mustEqual(goodsRoutes.ConsigneeController.onPageLoad(NormalMode, answers.lrn, index))
      }

      "to Notified Party when the answer is no" in {

        val answers = emptyUserAnswers.set(ConsigneeKnownPage(index), false).success.value

        ConsigneeKnownPage(index).navigate(NormalMode, answers)
          .mustEqual(goodsRoutes.NotifiedPartyController.onPageLoad(NormalMode, answers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneeKnownPage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
