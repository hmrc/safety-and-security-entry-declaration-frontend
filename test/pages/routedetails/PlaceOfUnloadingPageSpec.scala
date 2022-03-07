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

package pages.routedetails

import base.SpecBase
import controllers.routedetails.{routes => routedetailsRoutes}
import controllers.routes
import models.{PlaceOfUnloading, CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class PlaceOfUnloadingPageSpec extends SpecBase with PageBehaviours {

  "PlaceOfUnloadingPage" - {

    beRetrievable[PlaceOfUnloading](PlaceOfUnloadingPage(index))

    beSettable[PlaceOfUnloading](PlaceOfUnloadingPage(index))

    beRemovable[PlaceOfUnloading](PlaceOfUnloadingPage(index))

    "must navigate in Normal Mode" - {

      "to Add Place of Unloading" in {

        PlaceOfUnloadingPage(index).navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routedetailsRoutes.AddPlaceOfUnloadingController.onPageLoad(NormalMode, emptyUserAnswers.lrn))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        PlaceOfUnloadingPage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
