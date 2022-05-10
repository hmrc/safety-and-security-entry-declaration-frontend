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
import config.IndexLimits.{maxGoods, maxPackages}
import controllers.{routes => baseRoutes}
import forms.goods.AddPackageFormProvider
import models.{Index, KindOfPackage}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.goods.{AddPackagePage, KindOfPackagePage, MarkOrNumberPage, NumberOfPackagesPage}
import pages.{EmptyWaypoints, Waypoints}
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import viewmodels.checkAnswers.goods.PackageSummary
import views.html.goods.AddPackageView

import scala.concurrent.Future

class AddPackageControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new AddPackageFormProvider()
  val form = formProvider()
  val waypoints: Waypoints = EmptyWaypoints

  lazy val addPackageRoute = routes.AddPackageController.onPageLoad(waypoints, lrn, index).url

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
        val list = PackageSummary.rows(baseAnswers, index, waypoints, AddPackagePage(index))

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, index, list)(
          request,
          implicitly
        ).toString
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
          FakeRequest(POST, addPackageRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = baseAnswers.set(AddPackagePage(index), true).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual AddPackagePage(index)
          .navigate(waypoints, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must save `false` and redirect when the max number of packages has been added, even if the answer is `true" in {

      val answers =
        (0 until maxPackages)
          .foldLeft(emptyUserAnswers) {
            (accumulatedAnswers, index) =>
              accumulatedAnswers
                .set(KindOfPackagePage(Index(0), Index(index)), KindOfPackage.standardKindsOfPackages.head).success.value
                .set(NumberOfPackagesPage(Index(0), Index(index)), 1).success.value
                .set(MarkOrNumberPage(Index(0), Index(index)), "mark").success.value
          }

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, addPackageRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = answers.set(AddPackagePage(index), false).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual AddPackagePage(index)
          .navigate(waypoints, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addPackageRoute)
            .withFormUrlEncodedBody(("value", ""))

        val result = route(application, request).value

        implicit val msgs: Messages = messages(application)
        val list = PackageSummary.rows(emptyUserAnswers, index, waypoints, AddPackagePage(index))

      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addPackageRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
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
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a GET if the item index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(GET, routes.AddPackageController.onPageLoad(waypoints, lrn, Index(maxGoods)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if the item index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, routes.AddPackageController.onPageLoad(waypoints, lrn, Index(maxGoods)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
