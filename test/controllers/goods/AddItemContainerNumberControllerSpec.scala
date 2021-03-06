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
import config.IndexLimits.{maxContainers, maxGoods}
import controllers.{routes => baseRoutes}
import forms.goods.AddItemContainerNumberFormProvider
import models.{Container, Index}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.mockito.MockitoSugar
import pages.goods.{AddItemContainerNumberPage, ItemContainerNumberPage}
import pages.{EmptyWaypoints, Waypoints, goods}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.goods.AddItemContainerNumberView

import scala.concurrent.Future

class AddItemContainerNumberControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new AddItemContainerNumberFormProvider()
  val form = formProvider()
  val waypoints: Waypoints = EmptyWaypoints

  lazy val addContainerRoute = routes.AddItemContainerNumberController.onPageLoad(waypoints, lrn, index).url

  "Add Item Container Number Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addContainerRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AddItemContainerNumberView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, index, List.empty)(
          request,
          messages(application)
        ).toString
      }
    }

    "must save the answer and redirect to the next page when valid data is submitted" in {

      val container = arbitrary[Container].sample.value
      val answers = emptyUserAnswers.set(ItemContainerNumberPage(index, index), container).success.value

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, addContainerRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = answers.set(AddItemContainerNumberPage(index), true).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual goods.AddItemContainerNumberPage(index)
          .navigate(waypoints, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must save `false` and redirect when the max number of containers has been added, even if the answer is `true`" in {

      val containers = Gen.listOfN(maxContainers, arbitrary[Container]).sample.value

      val answers =
        containers
          .zipWithIndex
          .foldLeft(emptyUserAnswers) {
            case (accumulatedAnswers, (container, index)) =>
              accumulatedAnswers.set(ItemContainerNumberPage(Index(0), Index(index)), container).success.value
          }

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, addContainerRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = answers.set(AddItemContainerNumberPage(index), false).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual goods.AddItemContainerNumberPage(index)
          .navigate(waypoints, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val container = arbitrary[Container].sample.value
      val answers = emptyUserAnswers.set(ItemContainerNumberPage(index, index), container).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addContainerRoute)
            .withFormUrlEncodedBody(("value", ""))

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
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

    "must redirect to Journey Recovery for a GET if the item index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(GET, routes.AddItemContainerNumberController.onPageLoad(waypoints, lrn, Index(maxGoods)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if the item index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, routes.AddItemContainerNumberController.onPageLoad(waypoints, lrn, Index(maxGoods)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
