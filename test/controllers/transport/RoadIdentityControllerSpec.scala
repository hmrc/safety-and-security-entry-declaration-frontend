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
import forms.transport.RoadIdentityFormProvider
import models.TransportIdentity.RoadIdentity
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.transport.RoadIdentityPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.transport.RoadIdentityView

import scala.concurrent.Future

class RoadIdentityControllerSpec extends SpecBase with MockitoSugar {

  private val waypoints = EmptyWaypoints
  private val formProvider = new RoadIdentityFormProvider()
  private val form = formProvider()

  private lazy val roadIdentityRoute = routes.RoadIdentityController.onPageLoad(waypoints, lrn).url

  private val id = arbitrary[RoadIdentity].sample.value
  private val formData: List[(String, String)] = List(
    "vehicleRegistrationNumber" -> id.vehicleRegistrationNumber,
    "trailerNumber" -> id.trailerNumber
  ) ++ id.ferryCompany.map { fc => "ferryCompany" -> fc }

  private val userAnswers = emptyUserAnswers.set(RoadIdentityPage, id).success.value

  "RoadIdentity Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, roadIdentityRoute)

        val view = application.injector.instanceOf[RoadIdentityView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, roadIdentityRoute)

        val view = application.injector.instanceOf[RoadIdentityView]

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
          FakeRequest(POST, roadIdentityRoute)
            .withFormUrlEncodedBody(formData: _*)

        val result          = route(application, request).value
        val expectedAnswers = emptyUserAnswers.set(RoadIdentityPage, id).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RoadIdentityPage.navigate(waypoints, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, roadIdentityRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[RoadIdentityView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, roadIdentityRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, roadIdentityRoute)
            .withFormUrlEncodedBody(formData: _*)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
