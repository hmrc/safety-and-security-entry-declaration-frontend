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

package controllers.consignors

import base.SpecBase
import controllers.{routes => baseRoutes}
import forms.consignors.AddConsignorFormProvider
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.consignors.AddConsignorPage
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import viewmodels.checkAnswers.consignors.AddConsignorSummary
import views.html.consignors.AddConsignorView

import scala.concurrent.Future

class AddConsignorControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new AddConsignorFormProvider()
  val form = formProvider()
  private val waypoints = EmptyWaypoints

  lazy val addConsignorRoute = routes.AddConsignorController.onPageLoad(waypoints, lrn).url

  "AddConsignor Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addConsignorRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AddConsignorView]

        implicit val msgs: Messages = messages(application)
        val list = AddConsignorSummary.rows(emptyUserAnswers, waypoints, AddConsignorPage)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, list)(request, implicitly).toString
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
          FakeRequest(POST, addConsignorRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result          = route(application, request).value
        val expectedAnswers = emptyUserAnswers.set(AddConsignorPage, true).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual AddConsignorPage.navigate(waypoints, emptyUserAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addConsignorRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[AddConsignorView]

        implicit val msgs: Messages = messages(application)
        val list = AddConsignorSummary.rows(emptyUserAnswers, waypoints, AddConsignorPage)

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn, list)(request, implicitly).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addConsignorRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, addConsignorRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
