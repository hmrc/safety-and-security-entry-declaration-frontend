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

package controllers

import base.SpecBase
import forms.RemovePackageFormProvider
import models.{KindOfPackage, NormalMode}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{never, times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.{KindOfPackagePage, NumberOfPackagesPage, RemovePackagePage}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import queries.PackageQuery
import repositories.SessionRepository
import views.html.RemovePackageView

import scala.concurrent.Future

class RemovePackageControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new RemovePackageFormProvider()
  val form = formProvider()

  lazy val removePackageRoute = routes.RemovePackageController.onPageLoad(NormalMode, lrn, index, index).url

  private val baseAnswers =
    emptyUserAnswers
      .set(KindOfPackagePage(index, index), KindOfPackage.allKindsOfPackage.head).success.value
      .set(NumberOfPackagesPage(index, index), 1).success.value

  "RemovePackage Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, removePackageRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[RemovePackageView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, lrn, index, index)(request, messages(application)).toString
      }
    }

    "must remove the package and redirect to the next page when the answer is yes" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removePackageRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result          = route(application, request).value
        val expectedAnswers = baseAnswers.remove(PackageQuery(index, index)).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemovePackagePage(index, index).navigate(NormalMode, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must redirect to the next page without removing the package when the answer is no" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removePackageRoute)
            .withFormUrlEncodedBody(("value", "false"))

        val result          = route(application, request).value
        val expectedAnswers = baseAnswers

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemovePackagePage(index, index).navigate(NormalMode, expectedAnswers).url
        verify(mockSessionRepository, never()).set(any())
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, removePackageRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[RemovePackageView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode, lrn, index, index)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, removePackageRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, removePackageRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
