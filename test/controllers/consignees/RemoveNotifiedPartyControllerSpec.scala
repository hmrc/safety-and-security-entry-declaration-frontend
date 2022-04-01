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
import forms.consignees.RemoveNotifiedPartyFormProvider
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{never, times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.{Breadcrumbs, EmptyBreadcrumbs}
import pages.consignees.{NotifiedPartyNamePage, RemoveNotifiedPartyPage}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import queries.consignees.NotifiedPartyQuery
import repositories.SessionRepository
import views.html.consignees.RemoveNotifiedPartyView

import scala.concurrent.Future

class RemoveNotifiedPartyControllerSpec extends SpecBase with MockitoSugar {

  private val formProvider = new RemoveNotifiedPartyFormProvider()
  private val form = formProvider()
  private val breadcrumbs = EmptyBreadcrumbs
  private val baseAnswers = emptyUserAnswers.set(NotifiedPartyNamePage(index),"Test LTD.").success.value

  lazy val removeNotifiedPartyRoute = routes.RemoveNotifiedPartyController.onPageLoad(breadcrumbs, lrn, index).url

  "RemoveNotifiedParty Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, removeNotifiedPartyRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[RemoveNotifiedPartyView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, breadcrumbs, lrn, index)(request, messages(application)).toString
      }
    }

    "must remove the notified party and redirect to the next page when the answer is yes" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removeNotifiedPartyRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = baseAnswers.remove(NotifiedPartyQuery(index)).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemoveNotifiedPartyPage(index)
          .navigate(breadcrumbs, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must redirect to the next page without removing the notified party when the answer is no" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removeNotifiedPartyRoute)
            .withFormUrlEncodedBody(("value", "false"))

        val result = route(application, request).value
        val expectedAnswers = baseAnswers

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemoveNotifiedPartyPage(index)
          .navigate(breadcrumbs, expectedAnswers)
          .url
        verify(mockSessionRepository, never()).set(any())
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, removeNotifiedPartyRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[RemoveNotifiedPartyView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, breadcrumbs, lrn, index)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, removeNotifiedPartyRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, removeNotifiedPartyRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
