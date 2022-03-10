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

package pages.consignors

import base.SpecBase
import controllers.consignors.{routes => consignorRoutes}
import controllers.routes
import models.{CheckMode, GbEori, NormalMode}
import pages.behaviours.PageBehaviours
import queries.consignors.ConsignorKeyQuery

class RemoveConsignorPageSpec extends SpecBase with PageBehaviours {

  "RemoveConsignorPage" - {

    beRetrievable[Boolean](RemoveConsignorPage(index))

    beSettable[Boolean](RemoveConsignorPage(index))

    beRemovable[Boolean](RemoveConsignorPage(index))

    "must navigate in Normal Mode" - {

      "to Add Consignor when there is still at least one consignor in the user's answers" in {

        val answers =
          emptyUserAnswers
            .set(ConsignorEORIPage(index), GbEori("123456789000")).success.value
            .set(ConsignorKeyQuery(index), 1).success.value

        RemoveConsignorPage(index).navigate(NormalMode, answers)
          .mustEqual(consignorRoutes.AddConsignorController.onPageLoad(NormalMode, answers.lrn))
      }

      "to Consignor Identity when there are no consignors left" in {

        RemoveConsignorPage(index).navigate(NormalMode, emptyUserAnswers)
          .mustEqual(consignorRoutes.ConsignorIdentityController.onPageLoad(NormalMode, emptyUserAnswers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        RemoveConsignorPage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
