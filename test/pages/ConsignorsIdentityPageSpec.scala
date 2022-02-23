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

package pages

import base.SpecBase
import controllers.routes
import models.ConsignorsIdentity.{GBEORI, NameAddress}
import models.{CheckMode, ConsignorsIdentity, NormalMode}
import pages.behaviours.PageBehaviours

class ConsignorsIdentitySpec extends SpecBase with PageBehaviours {

  "ConsignorsIdentityPage" - {

    beRetrievable[ConsignorsIdentity](ConsignorsIdentityPage(index))

    beSettable[ConsignorsIdentity](ConsignorsIdentityPage(index))

    beRemovable[ConsignorsIdentity](ConsignorsIdentityPage(index))

    "must navigate in Normal Mode" - {
      "to consignors EORI when answer is `I'll provide consignors EORI`" in {
        val answers = emptyUserAnswers.set(ConsignorsIdentityPage(index), GBEORI).success.value

        ConsignorsIdentityPage(index)
          .navigate(NormalMode, answers)
          .mustEqual(routes.ConsignorEORIController.onPageLoad(NormalMode, answers.lrn, index))
      }

      "to consignors name when answer is `I'll provide consignors name and address`" in {
        val answers = emptyUserAnswers.set(ConsignorsIdentityPage(index), NameAddress).success.value

        ConsignorsIdentityPage(index)
          .navigate(NormalMode, answers)
          .mustEqual(routes.ConsignorNameController.onPageLoad(NormalMode, answers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsignorsIdentityPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
