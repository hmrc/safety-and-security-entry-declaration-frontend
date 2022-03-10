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
import models.{CheckMode, Country, NormalMode}
import pages.behaviours.PageBehaviours

class NationalityOfTransportPageSpec extends SpecBase with PageBehaviours {

  "NationalityOfTransportPage" - {

    beRetrievable[Country](NationalityOfTransportPage)

    beSettable[Country](NationalityOfTransportPage)

    beRemovable[Country](NationalityOfTransportPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        NationalityOfTransportPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(transportRoutes.RoroUnaccompaniedIdentityController
            .onPageLoad(NormalMode, emptyUserAnswers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        NationalityOfTransportPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
