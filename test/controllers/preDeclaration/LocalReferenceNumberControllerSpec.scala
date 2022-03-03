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

package controllers.preDeclaration

import base.SpecBase
import forms.preDeclaration.LocalReferenceNumberFormProvider
import models.NormalMode
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.preDeclaration.LocalReferenceNumberPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.preDeclaration.LocalReferenceNumberView

import scala.concurrent.Future

class LocalReferenceNumberControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new LocalReferenceNumberFormProvider()
  val form = formProvider()

  lazy val localReferenceNumberRoute =
    routes.LocalReferenceNumberController.onPageLoad(NormalMode).url

  "LocalReferenceNumber Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, localReferenceNumberRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[LocalReferenceNumberView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode)(
          request,
          messages(application)
        ).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, localReferenceNumberRoute)
            .withFormUrlEncodedBody(("value", lrn.value))

        val result = route(application, request).value
        val expectedAnswers = emptyUserAnswers.set(LocalReferenceNumberPage, lrn).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual LocalReferenceNumberPage
          .navigate(NormalMode, expectedAnswers)
          .url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, localReferenceNumberRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[LocalReferenceNumberView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode)(
          request,
          messages(application)
        ).toString
      }
    }
  }
}
