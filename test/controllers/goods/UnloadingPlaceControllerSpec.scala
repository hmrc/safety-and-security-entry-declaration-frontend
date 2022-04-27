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
import forms.goods.UnloadingPlaceFormProvider
import models.{Index, PlaceOfUnloading}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import pages.goods.UnloadingPlacePage
import pages.routedetails.PlaceOfUnloadingPage
import pages.{EmptyWaypoints, Waypoints}
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import viewmodels.RadioOptions
import views.html.goods.UnloadingPlaceView

import scala.concurrent.Future

class UnloadingPlaceControllerSpec extends SpecBase with MockitoSugar {

  lazy val unloadingPlaceRoute = routes.UnloadingPlaceController.onPageLoad(waypoints, lrn, index).url
  val waypoints: Waypoints = EmptyWaypoints

  val placeOfUnloading1 = arbitrary[PlaceOfUnloading].sample.value
  val placeOfUnloading2 = arbitrary[PlaceOfUnloading].sample.value
  val placesOfUnloading = List(placeOfUnloading1, placeOfUnloading2)

  val baseAnswers =
    emptyUserAnswers
      .set(PlaceOfUnloadingPage(Index(0)), placeOfUnloading1).success.value
      .set(PlaceOfUnloadingPage(Index(1)), placeOfUnloading2).success.value

  val formProvider = new UnloadingPlaceFormProvider()
  val form = formProvider(placesOfUnloading.map(_.key))

  "UnloadingPlace Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, unloadingPlaceRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[UnloadingPlaceView]
        implicit val msgs: Messages = messages(application)
        val radioOptions = RadioOptions(placesOfUnloading.map(l => l.key.toString -> l.place).toMap)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, index, radioOptions)(request, msgs).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = baseAnswers.set(UnloadingPlacePage(index), placeOfUnloading1.key).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, unloadingPlaceRoute)

        val view = application.injector.instanceOf[UnloadingPlaceView]
        implicit val msgs: Messages = messages(application)
        val radioOptions = RadioOptions(placesOfUnloading.map(l => l.key.toString -> l.place).toMap)

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(
          form.fill(placeOfUnloading1.key),
          waypoints,
          lrn,
          index,
          radioOptions
        )(request, msgs).toString
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
          FakeRequest(POST, unloadingPlaceRoute)
            .withFormUrlEncodedBody(("value", placeOfUnloading1.key.toString))

        val result          = route(application, request).value
        val expectedAnswers = baseAnswers.set(UnloadingPlacePage(index), placeOfUnloading1.key).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual UnloadingPlacePage(index).navigate(waypoints, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, unloadingPlaceRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[UnloadingPlaceView]
        implicit val msgs: Messages = messages(application)
        val radioOptions = RadioOptions(placesOfUnloading.map(l => l.key.toString -> l.place).toMap)

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn, index, radioOptions)(request, msgs).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, unloadingPlaceRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, unloadingPlaceRoute)
            .withFormUrlEncodedBody(("value", placeOfUnloading1.key.toString))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
