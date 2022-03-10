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

package pages.consignees

import base.SpecBase
import controllers.consignees.{routes => consigneesRoutes}
import controllers.routes
import models.{CheckMode, GbEori, Index, NormalMode}
import pages.behaviours.PageBehaviours
import queries.consignees.ConsigneeKeyQuery

class RemoveConsigneePageSpec extends SpecBase with PageBehaviours {

  "RemoveConsigneePage" - {

    beRetrievable[Boolean](RemoveConsigneePage(index))

    beSettable[Boolean](RemoveConsigneePage(index))

    beRemovable[Boolean](RemoveConsigneePage(index))

    "must navigate in Normal Mode" - {

      "to Add Consignee when there is still at least one consignee in the user's answers" in {

        val answers =
          emptyUserAnswers
            .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value
            .set(ConsigneeKeyQuery(Index(0)), 1).success.value

        RemoveConsigneePage(index).navigate(NormalMode, answers)
          .mustEqual(consigneesRoutes.AddConsigneeController.onPageLoad(NormalMode, answers.lrn))
      }

      "to Consignee Known when there are no consignees left" in {

        RemoveConsigneePage(index).navigate(NormalMode, emptyUserAnswers)
          .mustEqual(consigneesRoutes.AnyConsigneesKnownController.onPageLoad(NormalMode, emptyUserAnswers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        RemoveConsigneePage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
