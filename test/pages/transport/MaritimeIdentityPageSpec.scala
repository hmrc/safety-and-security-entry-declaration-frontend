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

package pages.transport

import base.SpecBase
import controllers.transport.{routes => transportRoutes}
import controllers.routes
import models.{CheckMode, NormalMode}
import models.TransportIdentity.MaritimeIdentity
import pages.behaviours.PageBehaviours

class MaritimeIdentityPageSpec extends SpecBase with PageBehaviours {

  "MaritimeIdentityPage" - {

    beRetrievable[MaritimeIdentity](MaritimeIdentityPage)

    beSettable[MaritimeIdentity](MaritimeIdentityPage)

    beRemovable[MaritimeIdentity](MaritimeIdentityPage)

    "must navigate in Normal Mode" - {

      "to AnyOverallDocumentsController" in {

        MaritimeIdentityPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(transportRoutes.AnyOverallDocumentsController.onPageLoad(NormalMode, emptyUserAnswers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        MaritimeIdentityPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
