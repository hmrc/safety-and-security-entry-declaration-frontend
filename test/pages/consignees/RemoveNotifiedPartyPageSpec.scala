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
import models.{CheckMode, GbEori, NormalMode}
import pages.behaviours.PageBehaviours
import queries.consignees.NotifiedPartyKeyQuery

class RemoveNotifiedPartyPageSpec extends SpecBase with PageBehaviours {

  "RemoveNotifiedPartyPage" - {

    beRetrievable[Boolean](RemoveNotifiedPartyPage(index))

    beSettable[Boolean](RemoveNotifiedPartyPage(index))

    beRemovable[Boolean](RemoveNotifiedPartyPage(index))

    "must navigate in Normal Mode" - {

      "when there are still notified parties in the user's answers" - {

        "to Add Notified Party" in {
          val answers =
            emptyUserAnswers
              .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
              .set(NotifiedPartyKeyQuery(index), 1).success.value

          RemoveNotifiedPartyPage(index).navigate(NormalMode, answers)
            .mustEqual(consigneesRoutes.AddNotifiedPartyController.onPageLoad(NormalMode, answers.lrn))
        }
      }

      "when there are no notified parties in the user's answers" - {

        "to Notified Party Identity for Index 0 when the user does not know any consignees" in {

          val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, false).success.value

          RemoveNotifiedPartyPage(index).navigate(NormalMode, answers)
            .mustEqual(consigneesRoutes.NotifiedPartyIdentityController.onPageLoad(NormalMode, answers.lrn, index))
        }

        "to Add Any Notified Parties when the user knows some consignees" in {

          val answers = emptyUserAnswers.set(AnyConsigneesKnownPage, true).success.value

          RemoveNotifiedPartyPage(index).navigate(NormalMode, answers)
            .mustEqual(consigneesRoutes.AddAnyNotifiedPartiesController.onPageLoad(NormalMode, answers.lrn))
        }
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        RemoveNotifiedPartyPage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
