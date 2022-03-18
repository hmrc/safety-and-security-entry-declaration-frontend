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

package controllers.goods

import base.SpecBase
import controllers.{routes => baseRoutes}
import forms.goods.AddItemContainerNumberFormProvider
import models.NormalMode
import org.scalatestplus.mockito.MockitoSugar
import pages.goods
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.goods.AddItemContainerNumberView

class AddItemContainerNumberControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new AddItemContainerNumberFormProvider()
  val form = formProvider()

  lazy val addContainerRoute = routes.AddItemContainerNumberController.onPageLoad(NormalMode, lrn, index).url

  "Add Item Container Number Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addContainerRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AddItemContainerNumberView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, lrn, index, List.empty)(
          request,
          messages(application)
        ).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addContainerRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual goods.AddItemContainerNumberPage(index)
          .navigate(NormalMode, emptyUserAnswers, index, true)
          .url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addContainerRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[AddItemContainerNumberView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode, lrn, index, List.empty)(
          request,
          messages(application)
        ).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addContainerRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, addContainerRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
