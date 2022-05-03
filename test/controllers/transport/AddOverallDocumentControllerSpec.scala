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
import forms.transport.AddOverallDocumentFormProvider
import models.Document
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.transport.{AddOverallDocumentPage, OverallDocumentPage}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.transport.AddOverallDocumentView

import scala.concurrent.Future

class AddOverallDocumentControllerSpec extends SpecBase with MockitoSugar {
  private val waypoints = EmptyWaypoints
  private val formProvider = new AddOverallDocumentFormProvider()
  private val form = formProvider()

  private lazy val addOverallDocumentRoute = {
    routes.AddOverallDocumentController.onPageLoad(waypoints, lrn).url
  }

  "AddOverallDocument Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addOverallDocumentRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AddOverallDocumentView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, Nil)(request, messages(application)).toString
      }
    }

    "must save the answer and redirect to the next page with the correct next index if yes is selected" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)
      val doc = arbitrary[Document].sample.value
      val answers = emptyUserAnswers.set(OverallDocumentPage(index), doc).success.value

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()


      running(application) {
        val request = {
          FakeRequest(POST, addOverallDocumentRoute).withFormUrlEncodedBody(("value", "true"))
        }

        val result = route(application, request).value
        val expectedAnswers = answers.set(AddOverallDocumentPage, true).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual AddOverallDocumentPage.navigate(waypoints, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must redirect to the AddAnySeals page if no is selected" in {
      val doc = arbitrary[Document].sample.value
      val answers = emptyUserAnswers.set(OverallDocumentPage(index), doc).success.value
      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = {
          FakeRequest(POST, addOverallDocumentRoute).withFormUrlEncodedBody(("value", "false"))
        }

        val result = route(application, request).value
        val expectedRedirect = {
          routes.AddAnySealsController.onPageLoad(waypoints, answers.lrn).url
        }

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual expectedRedirect
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addOverallDocumentRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[AddOverallDocumentView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn, Nil)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addOverallDocumentRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, addOverallDocumentRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
