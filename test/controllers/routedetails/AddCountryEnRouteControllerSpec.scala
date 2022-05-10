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

package controllers.routedetails

import base.SpecBase
import config.IndexLimits.maxCountries
import controllers.{routes => baseRoutes}
import forms.routedetails.AddCountryEnRouteFormProvider
import models.{Country, Index}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.routedetails.{AddCountryEnRoutePage, CountryEnRoutePage}
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import viewmodels.checkAnswers.routedetails.AddCountryEnRouteSummary
import views.html.routedetails.AddCountryEnRouteView

import scala.concurrent.Future

class AddCountryEnRouteControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new AddCountryEnRouteFormProvider()
  val form = formProvider()
  val waypoints = EmptyWaypoints

  lazy val addCountryEnRouteRoute =
    routes.AddCountryEnRouteController.onPageLoad(waypoints, lrn).url

  "AddCountryEnRoute Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addCountryEnRouteRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AddCountryEnRouteView]

        implicit val msgs: Messages = messages(application)
        val list = AddCountryEnRouteSummary.rows(emptyUserAnswers, waypoints, AddCountryEnRoutePage)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, list)(
          request,
          implicitly
        ).toString
      }
    }

    "must save the answer and redirect to the next page when valid data is submitted" in {

      val answers =
        emptyUserAnswers.set(CountryEnRoutePage(index), arbitrary[Country].sample.value).success.value

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, addCountryEnRouteRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = answers.set(AddCountryEnRoutePage, true).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual AddCountryEnRoutePage.navigate(waypoints, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must save `false` and redirect when the max number of countries have been added, even when the answer is false" in {

      val countries = Gen.listOfN(maxCountries, arbitrary[Country].sample.value).sample.value

      val answers =
        countries
          .zipWithIndex
          .foldLeft(emptyUserAnswers) {
            case (accumulatedAnswers, (country, index)) =>
              accumulatedAnswers.set(CountryEnRoutePage(Index(index)), country).success.value
          }

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, addCountryEnRouteRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = answers.set(AddCountryEnRoutePage, false).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual AddCountryEnRoutePage.navigate(waypoints, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val answers =
        emptyUserAnswers.set(CountryEnRoutePage(index), arbitrary[Country].sample.value).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addCountryEnRouteRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[AddCountryEnRouteView]

        val result = route(application, request).value

        implicit val msgs: Messages = messages(application)
        val list = AddCountryEnRouteSummary.rows(answers, waypoints, AddCountryEnRoutePage)

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn, list)(
          request,
          implicitly
        ).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addCountryEnRouteRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, addCountryEnRouteRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
