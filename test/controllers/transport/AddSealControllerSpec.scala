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

package controllers.transport

import base.SpecBase
import controllers.{routes => baseRoutes}
import forms.transport.AddSealFormProvider
import models.NormalMode
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.transport.{AddSealPage, SealPage}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.transport.AddSealView

class AddSealControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new AddSealFormProvider()
  val form = formProvider()

  lazy val addSealRoute = routes.AddSealController.onPageLoad(NormalMode, lrn).url

  "AddSeal Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addSealRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AddSealView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, lrn, Nil)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(AddSealPage, true).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addSealRoute)

        val view = application.injector.instanceOf[AddSealView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(true), NormalMode, lrn, Nil)(request, messages(application)).toString
      }
    }

    "must redirect to the Seal page with the correct next index if yes is selected" in {
      val content = arbitrary[String].sample.value
      val answers = emptyUserAnswers.set(SealPage(index), content).success.value
      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = {
          FakeRequest(POST, addSealRoute).withFormUrlEncodedBody(("value", "true"))
        }

        val result = route(application, request).value
        val expectedRedirect = {
          routes.SealController.onPageLoad(
            NormalMode,
            answers.lrn,
            index + 1
          ).url
        }

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual expectedRedirect
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addSealRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[AddSealView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode, lrn, Nil)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addSealRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, addSealRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
