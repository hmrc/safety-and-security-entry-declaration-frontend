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

class AddConsigneePageSpec extends SpecBase with PageBehaviours {

  "AddConsigneePage" - {

    "must navigate in Normal Mode" - {

      "to Consignee Identity for the next index when the answer is yes" in {

        val answers = emptyUserAnswers.set(ConsigneeEORIPage(Index(0)), GbEori("123456789000")).success.value

        AddConsigneePage.navigate(NormalMode, answers, addAnother = true)
          .mustEqual(consigneesRoutes.ConsigneeIdentityController.onPageLoad(NormalMode, answers.lrn, Index(1)))
      }

      "to Add Any Notified Parties when the answer is no" in {

        AddConsigneePage.navigate(NormalMode, emptyUserAnswers, addAnother = false)
          .mustEqual(consigneesRoutes.AddAnyNotifiedPartiesController.onPageLoad(NormalMode, emptyUserAnswers.lrn))
      }
    }
  }
}