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
import forms.routedetails.RemovePlaceOfLoadingFormProvider
import models.PlaceOfLoading
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{never, times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.routedetails.{PlaceOfLoadingPage, RemovePlaceOfLoadingPage}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.routedetails.RemovePlaceOfLoadingView

import scala.concurrent.Future

class RemovePlaceOfLoadingControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new RemovePlaceOfLoadingFormProvider()
  val form = formProvider()
  val waypoints = EmptyWaypoints
  
  lazy val removePlaceOfLoadingRoute = routes.RemovePlaceOfLoadingController.onPageLoad(waypoints, lrn, index).url

  private val placeOfLoading = arbitrary[PlaceOfLoading].sample.value
  private val baseAnswers = emptyUserAnswers.set(PlaceOfLoadingPage(index), placeOfLoading).success.value

  "RemovePlaceOfLoading Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, removePlaceOfLoadingRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[RemovePlaceOfLoadingView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, index)(request, messages(application)).toString
      }
    }

    "must delete a place of loading and redirect to the next page when the user answers yes" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removePlaceOfLoadingRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result          = route(application, request).value
        val expectedAnswers = baseAnswers.remove(PlaceOfLoadingPage(index)).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemovePlaceOfLoadingPage(index).navigate(waypoints, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must not delete a place of loading and redirect to the next page when the user answers no" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removePlaceOfLoadingRoute)
            .withFormUrlEncodedBody(("value", "false"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemovePlaceOfLoadingPage(index).navigate(waypoints, baseAnswers).url
        verify(mockSessionRepository, never()).set(any())
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, removePlaceOfLoadingRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[RemovePlaceOfLoadingView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn, index)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, removePlaceOfLoadingRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, removePlaceOfLoadingRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
