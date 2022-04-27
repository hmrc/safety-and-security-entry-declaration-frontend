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
import forms.goods.LoadingPlaceFormProvider
import models.{Index, PlaceOfLoading}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.goods.LoadingPlacePage
import pages.routedetails.PlaceOfLoadingPage
import pages.{EmptyWaypoints, Waypoints}
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import viewmodels.RadioOptions
import views.html.goods.LoadingPlaceView

import scala.concurrent.Future

class LoadingPlaceControllerSpec extends SpecBase with MockitoSugar {

  val waypoints: Waypoints = EmptyWaypoints
  lazy val loadingPlaceRoute = routes.LoadingPlaceController.onPageLoad(waypoints, lrn, index).url

  val placeOfLoading1 = arbitrary[PlaceOfLoading].sample.value
  val placeOfLoading2 = arbitrary[PlaceOfLoading].sample.value
  val placesOfLoading = List(placeOfLoading1, placeOfLoading2)

  val baseAnswers =
    emptyUserAnswers
      .set(PlaceOfLoadingPage(Index(0)), placeOfLoading1).success.value
      .set(PlaceOfLoadingPage(Index(1)), placeOfLoading2).success.value

  val formProvider = new LoadingPlaceFormProvider()
  val form = formProvider(placesOfLoading.map(_.key))

  "LoadingPlace Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, loadingPlaceRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[LoadingPlaceView]
        implicit val msgs: Messages = messages(application)
        val radioOptions = RadioOptions(placesOfLoading.map(l => l.key.toString -> l.place).toMap)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, index, radioOptions)(request, implicitly).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = baseAnswers.set(LoadingPlacePage(index), placeOfLoading1.key).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, loadingPlaceRoute)

        val view = application.injector.instanceOf[LoadingPlaceView]
        implicit val msgs: Messages = messages(application)
        val radioOptions = RadioOptions(placesOfLoading.map(l => l.key.toString -> l.place).toMap)

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(
          form.fill(placeOfLoading1.key),
          waypoints,
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
          FakeRequest(POST, loadingPlaceRoute)
            .withFormUrlEncodedBody(("value", placeOfLoading1.key.toString))

        val result = route(application, request).value
        val expectedAnswers = baseAnswers.set(LoadingPlacePage(index), placeOfLoading1.key).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual LoadingPlacePage(index).navigate(waypoints, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, loadingPlaceRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[LoadingPlaceView]
        implicit val msgs: Messages = messages(application)
        val radioOptions = RadioOptions(placesOfLoading.map(l => l.key.toString -> l.place).toMap)


        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(
          boundForm,
          waypoints,
          lrn,
          index,
          radioOptions
        )(request, implicitly).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, loadingPlaceRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, loadingPlaceRoute)
            .withFormUrlEncodedBody(("value", placeOfLoading1.key.toString))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
