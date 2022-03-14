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
import forms.goods.ConsignorFormProvider
import models.{GbEori, Index, NormalMode, TraderWithEori}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.consignors.ConsignorEORIPage
import pages.goods.ConsignorPage
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import queries.consignors.ConsignorKeyQuery
import repositories.SessionRepository
import viewmodels.RadioOptions
import views.html.goods.ConsignorView

import scala.concurrent.Future

class ConsignorControllerSpec extends SpecBase with MockitoSugar {

  lazy val consignorRoute = routes.ConsignorController.onPageLoad(NormalMode, lrn, index).url

  val key1 = 1
  val key2 = 2
  val eori1 = GbEori("123456789000")
  val eori2 = GbEori("123456789001")
  private val consignors = List(
    TraderWithEori(key1, eori1.value),
    TraderWithEori(key2, eori2.value)
  )

  val baseAnswers =
    emptyUserAnswers
      .set(ConsignorKeyQuery(Index(0)), key1).success.value
      .set(ConsignorEORIPage(Index(0)),eori1).success.value
      .set(ConsignorKeyQuery(Index(1)), key2).success.value
      .set(ConsignorEORIPage(Index(1)), eori2).success.value

  val formProvider = new ConsignorFormProvider()
  val form = formProvider(consignors.map(_.key))

  "Consignor Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, consignorRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[ConsignorView]
        implicit val msgs: Messages = messages(application)
        val radioOptions = RadioOptions(consignors.map(c => c.key.toString -> c.displayName).toMap)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, lrn, index, radioOptions)(request, implicitly).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = baseAnswers.set(ConsignorPage(index), consignors.head.key).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, consignorRoute)

        val view = application.injector.instanceOf[ConsignorView]
        implicit val msgs: Messages = messages(application)
        val radioOptions = RadioOptions(consignors.map(c => c.key.toString -> c.displayName).toMap)

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(
          form.fill(consignors.head.key),
          NormalMode,
          lrn,
          index,
          radioOptions
        )(request, implicitly).toString
      }
    }

    "must save the answer and redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, consignorRoute)
            .withFormUrlEncodedBody(("value", consignors.head.key.toString))

        val result          = route(application, request).value
        val expectedAnswers = baseAnswers.set(ConsignorPage(index), consignors.head.key).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual ConsignorPage(index).navigate(NormalMode, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, consignorRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[ConsignorView]
        implicit val msgs: Messages = messages(application)
        val radioOptions = RadioOptions(consignors.map(c => c.key.toString -> c.displayName).toMap)

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(
          boundForm,
          NormalMode,
          lrn,
          index,
          radioOptions
        )(request, implicitly).toString
      }
    }



    "must redirect to Journey Recovery when no consignors have been provided" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, consignorRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, consignorRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, consignorRoute)
            .withFormUrlEncodedBody(("value", consignors.head.key.toString))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
