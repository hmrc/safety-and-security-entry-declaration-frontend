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

package controllers.consignees

import base.SpecBase
import controllers.{routes => baseRoutes}
import forms.consignees.AddNotifiedPartyFormProvider
import models.NormalMode
import org.scalatestplus.mockito.MockitoSugar
import pages.consignees.AddNotifiedPartyPage
import play.api.i18n.Messages
import play.api.test.FakeRequest
import play.api.test.Helpers._
import viewmodels.checkAnswers.consignees.AddNotifiedPartySummary
import views.html.consignees.AddNotifiedPartyView

class AddNotifiedPartyControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new AddNotifiedPartyFormProvider()
  val form = formProvider()

  lazy val addNotifiedPartyRoute = routes.AddNotifiedPartyController.onPageLoad(NormalMode, lrn).url

  "AddNotifiedParty Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addNotifiedPartyRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AddNotifiedPartyView]

        implicit val msgs: Messages = messages(application)
        val list = AddNotifiedPartySummary.rows(emptyUserAnswers)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, lrn, list)(request, implicitly).toString
      }
    }

    "mustredirect to the next page when valid data is submitted" in {

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, addNotifiedPartyRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result          = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual AddNotifiedPartyPage.navigate(NormalMode, emptyUserAnswers, addAnother = true).url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addNotifiedPartyRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[AddNotifiedPartyView]

        implicit val msgs: Messages = messages(application)
        val list = AddNotifiedPartySummary.rows(emptyUserAnswers)

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode, lrn, list)(request, implicitly).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addNotifiedPartyRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, addNotifiedPartyRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
