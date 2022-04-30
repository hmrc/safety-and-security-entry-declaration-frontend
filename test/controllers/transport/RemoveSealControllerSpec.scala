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
import forms.transport.RemoveSealFormProvider
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{never, times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.transport.{RemoveSealPage, SealPage}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.transport.RemoveSealView

import scala.concurrent.Future

class RemoveSealControllerSpec extends SpecBase with MockitoSugar {

  private val waypoints = EmptyWaypoints
  val formProvider = new RemoveSealFormProvider()
  val form = formProvider()

  lazy val removeSealRoute = routes.RemoveSealController.onPageLoad(waypoints, lrn, index).url

  "RemoveSeal Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, removeSealRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[RemoveSealView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, index)(request, messages(application)).toString
      }
    }

    "must remove the seal and redirect to the next page when the answer is yes" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val answers = emptyUserAnswers.set(SealPage(index), "abc").success.value

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removeSealRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result          = route(application, request).value
        val expectedAnswers = emptyUserAnswers.remove(SealPage(index)).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemoveSealPage(index).navigate(waypoints, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must not remove the seal and redirect to the next page when the answer is no" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val answers = emptyUserAnswers.set(SealPage(index), "abc").success.value

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removeSealRoute)
            .withFormUrlEncodedBody(("value", "false"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemoveSealPage(index).navigate(waypoints, answers).url
        verify(mockSessionRepository, never()).set(any())
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, removeSealRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[RemoveSealView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn, index)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, removeSealRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, removeSealRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
