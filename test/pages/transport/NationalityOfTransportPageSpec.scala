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
import models.TransportMode._
import pages.behaviours.PageBehaviours

class NationalityOfTransportPageSpec extends SpecBase with PageBehaviours {
  "NationalityOfTransportPage" - {

    beRetrievable[Country](NationalityOfTransportPage)

    beSettable[Country](NationalityOfTransportPage)

    beRemovable[Country](NationalityOfTransportPage)

    "must navigate in Normal Mode" - {

      "to Roro Unaccompanied Identity if transport mode is Roro Unaccompanied" in {
        val answers = emptyUserAnswers.set(TransportModePage, RoroUnaccompanied).success.value

        NationalityOfTransportPage.navigate(NormalMode, answers)
          .mustEqual(
            transportRoutes.RoroUnaccompaniedIdentityController
              .onPageLoad(NormalMode, answers.lrn)
          )
      }

      "to Roro Accompanied Identity if transport mode is Roro Accompanied" in {
        val answers = emptyUserAnswers.set(TransportModePage, RoroAccompanied).success.value

        NationalityOfTransportPage.navigate(NormalMode, answers)
          .mustEqual(
            transportRoutes.RoroAccompaniedIdentityController
              .onPageLoad(NormalMode, answers.lrn)
          )
      }

      "to Maritime Identity if transport mode is Maritime" in {
        val answers = emptyUserAnswers.set(TransportModePage, Maritime).success.value

        NationalityOfTransportPage.navigate(NormalMode, answers)
          .mustEqual(
            transportRoutes.MaritimeIdentityController
              .onPageLoad(NormalMode, answers.lrn)
          )
      }

      "to Rail Identity if transport mode is Rail" in {
        val answers = emptyUserAnswers.set(TransportModePage, Rail).success.value

        NationalityOfTransportPage.navigate(NormalMode, answers)
          .mustEqual(
            transportRoutes.RailIdentityController
              .onPageLoad(NormalMode, answers.lrn)
          )
      }

      "to Air Identity if transport mode is Air" in {
        val answers = emptyUserAnswers.set(TransportModePage, Air).success.value

        NationalityOfTransportPage.navigate(NormalMode, answers)
          .mustEqual(
            transportRoutes.AirIdentityController
              .onPageLoad(NormalMode, answers.lrn)
          )
      }

      "to Road Identity if transport mode is Road" in {
        val answers = emptyUserAnswers.set(TransportModePage, Road).success.value

        NationalityOfTransportPage.navigate(NormalMode, answers)
          .mustEqual(
            transportRoutes.RoadIdentityController
              .onPageLoad(NormalMode, answers.lrn)
          )
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
