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
import forms.AddPackageFormProvider
import models.{KindOfPackage, NormalMode}
import org.scalatestplus.mockito.MockitoSugar
import pages.{AddPackagePage, KindOfPackagePage, MarkOrNumberPage, NumberOfPackagesPage}
import play.api.i18n.Messages
import play.api.test.FakeRequest
import play.api.test.Helpers._
import viewmodels.checkAnswers.PackageSummary
import views.html.AddPackageView

class AddPackageControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new AddPackageFormProvider()
  val form = formProvider()

  lazy val addPackageRoute = routes.AddPackageController.onPageLoad(NormalMode, lrn, index).url

  val baseAnswers =
    emptyUserAnswers
      .set(KindOfPackagePage(index, index), KindOfPackage.standardKindsOfPackages.head).success.value
      .set(NumberOfPackagesPage(index, index), 1).success.value
      .set(MarkOrNumberPage(index, index), "Mark or number").success.value

  "AddPackage Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addPackageRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AddPackageView]

        implicit val msgs: Messages = messages(application)
        val list = PackageSummary.rows(baseAnswers, index)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, lrn, index, list)(request, implicitly).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addPackageRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual AddPackagePage(index).navigate(NormalMode, baseAnswers, index, addAnother = true).url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addPackageRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[AddPackageView]

        val result = route(application, request).value

        implicit val msgs: Messages = messages(application)
        val list = PackageSummary.rows(emptyUserAnswers, index)

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode, lrn, index, list)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addPackageRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, addPackageRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
