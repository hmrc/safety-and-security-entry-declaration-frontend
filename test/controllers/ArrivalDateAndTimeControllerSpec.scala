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

package controllers

import base.SpecBase
import forms.ArrivalDateAndTimeFormProvider
import models.{NormalMode, UserAnswers, ArrivalDateAndTime}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.ArrivalDateAndTimePage
import play.api.inject.bind
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.ArrivalDateAndTimeView

import scala.concurrent.Future

class ArrivalDateAndTimeControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new ArrivalDateAndTimeFormProvider()
  val form = formProvider()

  lazy val arrivalDateAndTimeRoute = routes.ArrivalDateAndTimeController.onPageLoad(NormalMode, lrn).url

  val userAnswers = UserAnswers(
    userAnswersId,
    lrn,
    Json.obj(
      ArrivalDateAndTimePage.toString -> Json.obj(
        "date" -> "value 1",
        "time" -> "value 2"
      )
    )
  )

  "ArrivalDateAndTime Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, arrivalDateAndTimeRoute)

        val view = application.injector.instanceOf[ArrivalDateAndTimeView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, lrn)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, arrivalDateAndTimeRoute)

        val view = application.injector.instanceOf[ArrivalDateAndTimeView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(ArrivalDateAndTime("value 1", "value 2")), NormalMode, lrn)(request, messages(application)).toString
      }
    }

    "must save the answer and redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, arrivalDateAndTimeRoute)
            .withFormUrlEncodedBody(("date", "value 1"), ("time", "value 2"))

        val result          = route(application, request).value
        val expectedAnswers = emptyUserAnswers.set(ArrivalDateAndTimePage, ArrivalDateAndTime("value 1", "value 2")).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual ArrivalDateAndTimePage.navigate(NormalMode, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, arrivalDateAndTimeRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[ArrivalDateAndTimeView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode, lrn)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, arrivalDateAndTimeRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, arrivalDateAndTimeRoute)
            .withFormUrlEncodedBody(("date", "value 1"), ("time", "value 2"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
