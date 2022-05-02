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

package controllers.predec

import base.SpecBase
import controllers.{routes => baseRoutes}
import forms.predec.CarrierAddressFormProvider
import models.{Address, Country}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.predec.CarrierAddressPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.predec.CarrierAddressView

import scala.concurrent.Future

class CarrierAddressControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new CarrierAddressFormProvider()
  val form = formProvider()
  val country = arbitrary[Country].sample.value
  private val waypoints = EmptyWaypoints

  lazy val carrierAddressRoute =
    routes.CarrierAddressController.onPageLoad(waypoints, lrn).url

  "CarrierAddress Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, carrierAddressRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[CarrierAddressView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn)(
          request,
          messages(application)
        ).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers
        .set(CarrierAddressPage, Address("test", "test", "test", country))
        .success
        .value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, carrierAddressRoute)

        val view = application.injector.instanceOf[CarrierAddressView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(
          form.fill(Address("test", "test", "test", country)),
          waypoints,
          lrn
        )(request, messages(application)).toString
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
          FakeRequest(POST, carrierAddressRoute)
            .withFormUrlEncodedBody(
              "streetAndNumber" -> "test",
              "city" -> "test",
              "postCode" -> "test",
              "country" -> country.code
            )

        val result = route(application, request).value
        val expectedAnswers = emptyUserAnswers
          .set(CarrierAddressPage, Address("test", "test", "test", country))
          .success
          .value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual
          CarrierAddressPage
          .navigate(waypoints, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, carrierAddressRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[CarrierAddressView]

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
        val request = FakeRequest(GET, carrierAddressRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController
          .onPageLoad()
          .url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, carrierAddressRoute)
            .withFormUrlEncodedBody(
              "streetAndNumber" -> "test",
              "city" -> "test",
              "postCode" -> "test",
              "country" -> "GB"
            )

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController
          .onPageLoad()
          .url
      }
    }
  }
}
