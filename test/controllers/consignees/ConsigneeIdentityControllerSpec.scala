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
import forms.consignees.ConsigneeIdentityFormProvider
import models.ConsigneeIdentity
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.Breadcrumbs
import pages.consignees.ConsigneeIdentityPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import queries.consignees.ConsigneeKeyQuery
import repositories.SessionRepository
import views.html.consignees.ConsigneeIdentityView

import scala.concurrent.Future

class ConsigneeIdentityControllerSpec extends SpecBase with MockitoSugar {
  
  private val formProvider = new ConsigneeIdentityFormProvider()
  private val form = formProvider()
  private val breadcrumbs = Breadcrumbs.empty

  lazy val consigneeIdentityRoute =
    routes.ConsigneeIdentityController.onPageLoad(breadcrumbs, lrn, index).url

  "ConsigneeIdentity Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, consigneeIdentityRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[ConsigneeIdentityView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, breadcrumbs, lrn, index)(
          request,
          messages(application)
        ).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers
        .set(ConsigneeIdentityPage(index), ConsigneeIdentity.values.head).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, consigneeIdentityRoute)

        val view = application.injector.instanceOf[ConsigneeIdentityView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(
          form.fill(ConsigneeIdentity.values.head),
          breadcrumbs,
          lrn,
          index
        )(request, messages(application)).toString
      }
    }

    "must save the answer and save the key, and redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, consigneeIdentityRoute)
            .withFormUrlEncodedBody(("value", ConsigneeIdentity.values.head.toString))

        val result = route(application, request).value
        val expectedAnswers = emptyUserAnswers
          .set(ConsigneeIdentityPage(index), ConsigneeIdentity.values.head).success.value
          .set(ConsigneeKeyQuery(index), 1).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual ConsigneeIdentityPage(index)
          .navigate(breadcrumbs, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, consigneeIdentityRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[ConsigneeIdentityView]

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
        val request = FakeRequest(GET, consigneeIdentityRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, consigneeIdentityRoute)
            .withFormUrlEncodedBody(("value", ConsigneeIdentity.values.head.toString))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
