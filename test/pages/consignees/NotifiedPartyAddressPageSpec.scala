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
import models.{Address, CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class NotifiedPartyAddressPageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyAddressPage" - {

    beRetrievable[Address](NotifiedPartyAddressPage(index))

    beSettable[Address](NotifiedPartyAddressPage(index))

    beRemovable[Address](NotifiedPartyAddressPage(index))

    "must navigate in Normal Mode" - {

      "to Check Notified Party" in {

        NotifiedPartyAddressPage(index)
          .navigate(NormalMode, emptyUserAnswers)
          .mustEqual(
            consigneesRoutes.CheckNotifiedPartyController.onPageLoad(emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        NotifiedPartyAddressPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
