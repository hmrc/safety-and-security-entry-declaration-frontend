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
import forms.consignees.NotifiedPartyAddressFormProvider
import models.{Address, Country}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.{Breadcrumbs, consignees}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.consignees.NotifiedPartyAddressView

import scala.concurrent.Future

class NotifiedPartyAddressControllerSpec extends SpecBase with MockitoSugar {

  private val formProvider = new NotifiedPartyAddressFormProvider()
  private val form = formProvider()
  private val country = arbitrary[Country].sample.value
  private val breadcrumbs = Breadcrumbs.empty

  lazy val notifiedPartyAddressRoute =
    routes.NotifiedPartyAddressController.onPageLoad(breadcrumbs, lrn, index).url

  "NotifiedPartyAddress Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, notifiedPartyAddressRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[NotifiedPartyAddressView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, breadcrumbs, lrn, index)(
          request,
          messages(application)
        ).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers
        .set(consignees.NotifiedPartyAddressPage(index), Address("test", "test", "test", country))
        .success
        .value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, notifiedPartyAddressRoute)

        val view = application.injector.instanceOf[NotifiedPartyAddressView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(
          form.fill(Address("test", "test", "test", country)),
          breadcrumbs,
          lrn,
          index
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
          FakeRequest(POST, notifiedPartyAddressRoute)
            .withFormUrlEncodedBody(
              "streetAndNumber" -> "test",
              "city" -> "test",
              "postCode" -> "test",
              "country" -> country.code
            )

        val result = route(application, request).value
        val expectedAnswers = emptyUserAnswers
          .set(consignees.NotifiedPartyAddressPage(index), Address("test", "test", "test", country))
          .success
          .value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual consignees.NotifiedPartyAddressPage(index)
          .navigate(breadcrumbs, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, notifiedPartyAddressRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[NotifiedPartyAddressView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, breadcrumbs, lrn, index)(
          request,
          messages(application)
        ).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, notifiedPartyAddressRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, notifiedPartyAddressRoute)
            .withFormUrlEncodedBody(
              "streetAndNumber" -> "test",
              "city" -> "test",
              "postCode" -> "test",
              "country" -> "GB"
            )

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
