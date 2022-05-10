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
import config.IndexLimits.maxGoods
import controllers.{routes => baseRoutes}
import forms.goods.DangerousGoodCodeFormProvider
import models.Index
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.{EmptyWaypoints, Waypoints, goods}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import services.DangerousGoodsService
import views.html.goods.DangerousGoodCodeView

import scala.concurrent.Future

class DangerousGoodCodeControllerSpec extends SpecBase with MockitoSugar {

  val waypoints: Waypoints = EmptyWaypoints

  val dangerousGood = arbitraryDangerousGood.arbitrary.sample.value

  lazy val dangerousGoodCodeRoute =
    routes.DangerousGoodCodeController.onPageLoad(waypoints, lrn, index).url

  "DangerousGoodCode Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, dangerousGoodCodeRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[DangerousGoodCodeView]
        val service = application.injector.instanceOf[DangerousGoodsService]
        val formProvider = new DangerousGoodCodeFormProvider(service)
        val form = formProvider()

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, index)(
          request,
          messages(application)
        ).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers =
        emptyUserAnswers.set(goods.DangerousGoodCodePage(index), dangerousGood).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, dangerousGoodCodeRoute)

        val view = application.injector.instanceOf[DangerousGoodCodeView]
        val service = application.injector.instanceOf[DangerousGoodsService]
        val formProvider = new DangerousGoodCodeFormProvider(service)
        val form = formProvider()

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(dangerousGood), waypoints, lrn, index)(
          request,
          messages(application)
        ).toString
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
          FakeRequest(POST, dangerousGoodCodeRoute)
            .withFormUrlEncodedBody(("value", dangerousGood.code))

        val result = route(application, request).value
        val expectedAnswers =
          emptyUserAnswers.set(goods.DangerousGoodCodePage(index), dangerousGood).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual goods.DangerousGoodCodePage(index)
          .navigate(waypoints, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, dangerousGoodCodeRoute)
            .withFormUrlEncodedBody(("value", ""))

        val service = application.injector.instanceOf[DangerousGoodsService]
        val formProvider = new DangerousGoodCodeFormProvider(service)
        val form = formProvider()

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[DangerousGoodCodeView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn, index)(
          request,
          messages(application)
        ).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, dangerousGoodCodeRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, dangerousGoodCodeRoute)
            .withFormUrlEncodedBody(("value", "answer"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a GET if the item index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(GET, routes.DangerousGoodCodeController.onPageLoad(waypoints, lrn, Index(maxGoods)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if the item index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, routes.DangerousGoodCodeController.onPageLoad(waypoints, lrn, Index(maxGoods)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
