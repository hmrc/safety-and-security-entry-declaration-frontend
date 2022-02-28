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
import models.NotifiedPartyIdentity.{GBEORI, NameAddress}
import models.{CheckMode, NormalMode, NotifiedPartyIdentity}
import pages.behaviours.PageBehaviours

class NotifiedPartyIdentityPageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyIdentityPage" - {

    beRetrievable[NotifiedPartyIdentity](NotifiedPartyIdentityPage(index))

    beSettable[NotifiedPartyIdentity](NotifiedPartyIdentityPage(index))

    beRemovable[NotifiedPartyIdentity](NotifiedPartyIdentityPage(index))

    "must navigate in Normal Mode" - {

      "to `consignee EORI page` when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(NotifiedPartyIdentityPage(index), GBEORI).success.value

        NotifiedPartyIdentityPage(index)
          .navigate(NormalMode, answers)
          .mustEqual(consigneesRoutes.NotifiedPartyEORIController.onPageLoad(NormalMode, answers.lrn, index))
      }

      "to `consignee name` when answered `name & address`" in {
        val answers =
          emptyUserAnswers.set(NotifiedPartyIdentityPage(index), NameAddress).success.value

        NotifiedPartyIdentityPage(index)
          .navigate(NormalMode, answers)
          .mustEqual(consigneesRoutes.NotifiedPartyNameController.onPageLoad(NormalMode, answers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        NotifiedPartyIdentityPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
