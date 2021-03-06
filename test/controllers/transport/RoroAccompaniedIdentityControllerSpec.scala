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
import forms.transport.RoroAccompaniedIdentityFormProvider
import models.TransportIdentity.RoroAccompaniedIdentity
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.transport.RoroAccompaniedIdentityPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.transport.RoroAccompaniedIdentityView

import scala.concurrent.Future

class RoroAccompaniedIdentityControllerSpec extends SpecBase with MockitoSugar {

  private val waypoints = EmptyWaypoints
  private val formProvider = new RoroAccompaniedIdentityFormProvider()
  private val form = formProvider()

  private lazy val roroAccompaniedIdentityRoute = {
    routes.RoroAccompaniedIdentityController.onPageLoad(waypoints, lrn).url
  }

  private val id = arbitrary[RoroAccompaniedIdentity].sample.value
  private val formData: List[(String, String)] = List(
    "vehicleRegistrationNumber" -> id.vehicleRegistrationNumber,
    "trailerNumber" -> id.trailerNumber
  ) ++ id.ferryCompany.map { fc => "ferryCompany" -> fc }

  private val userAnswers = emptyUserAnswers.set(RoroAccompaniedIdentityPage, id).success.value

  "RoroAccompaniedIdentity Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, roroAccompaniedIdentityRoute)

        val view = application.injector.instanceOf[RoroAccompaniedIdentityView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, roroAccompaniedIdentityRoute)

        val view = application.injector.instanceOf[RoroAccompaniedIdentityView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(id), waypoints, lrn)(request, messages(application)).toString
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
          FakeRequest(POST, roroAccompaniedIdentityRoute)
            .withFormUrlEncodedBody(formData: _*)

        val result          = route(application, request).value
        val expectedAnswers = emptyUserAnswers.set(RoroAccompaniedIdentityPage, id).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RoroAccompaniedIdentityPage.navigate(waypoints, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, roroAccompaniedIdentityRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[RoroAccompaniedIdentityView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, roroAccompaniedIdentityRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, roroAccompaniedIdentityRoute)
            .withFormUrlEncodedBody(formData: _*)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
