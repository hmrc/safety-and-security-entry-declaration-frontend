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

package controllers.routedetails

import base.SpecBase
import controllers.{routes => baseRoutes}
import forms.routedetails.CustomsOfficeOfFirstEntryFormProvider
import models.CustomsOffice
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.routedetails.CustomsOfficeOfFirstEntryPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.routedetails.CustomsOfficeOfFirstEntryView

import scala.concurrent.Future

class CustomsOfficeOfFirstEntryControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new CustomsOfficeOfFirstEntryFormProvider()
  val form = formProvider()
  val customsOffice = arbitrary[CustomsOffice].sample.value
  val waypoints = EmptyWaypoints
  
  lazy val customsOfficeOfFirstEntryRoute =
    routes.CustomsOfficeOfFirstEntryController.onPageLoad(waypoints, lrn).url

  "CustomsOfficeOfFirstEntry Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, customsOfficeOfFirstEntryRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[CustomsOfficeOfFirstEntryView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn)(
          request,
          messages(application)
        ).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers =
        emptyUserAnswers.set(CustomsOfficeOfFirstEntryPage, customsOffice).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, customsOfficeOfFirstEntryRoute)

        val view = application.injector.instanceOf[CustomsOfficeOfFirstEntryView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(customsOffice), waypoints, lrn)(
          request,
          messages(application)
        ).toString
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
          FakeRequest(POST, customsOfficeOfFirstEntryRoute)
            .withFormUrlEncodedBody(("value", customsOffice.code))

        val result = route(application, request).value
        val expectedAnswers =
          emptyUserAnswers.set(CustomsOfficeOfFirstEntryPage, customsOffice).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual CustomsOfficeOfFirstEntryPage
          .navigate(waypoints, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, customsOfficeOfFirstEntryRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[CustomsOfficeOfFirstEntryView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn)(
          request,
          messages(application)
        ).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, customsOfficeOfFirstEntryRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, customsOfficeOfFirstEntryRoute)
            .withFormUrlEncodedBody(("value", customsOffice.code))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
