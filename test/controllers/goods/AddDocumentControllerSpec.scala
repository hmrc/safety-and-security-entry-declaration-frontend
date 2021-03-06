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
import config.IndexLimits.{maxDocuments, maxGoods}
import controllers.{routes => baseRoutes}
import forms.goods.AddDocumentFormProvider
import models.{Document, Index}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.mockito.MockitoSugar
import pages.goods.{AddDocumentPage, DocumentPage}
import pages.{EmptyWaypoints, Waypoints, goods}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.goods.AddDocumentView

import scala.concurrent.Future

class AddDocumentControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new AddDocumentFormProvider()
  val form = formProvider()
  val waypoints: Waypoints = EmptyWaypoints

  lazy val addDocumentRoute = routes.AddDocumentController.onPageLoad(waypoints, lrn, index).url

  "AddDocument Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, addDocumentRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AddDocumentView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, index, List.empty)(
          request,
          messages(application)
        ).toString
      }
    }

    "must save the answer and redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val document = arbitrary[Document].sample.value
      val answers =emptyUserAnswers.set(DocumentPage(index, index), document).success.value

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, addDocumentRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = answers.set(AddDocumentPage(index), true).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual goods.AddDocumentPage(index)
          .navigate(waypoints, expectedAnswers)
          .url

        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must save `false` and redirect when the maximum number of documents has been added, even when the answer is `true`" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val documents = Gen.listOfN(maxDocuments, arbitrary[Document]).sample.value

      val answers =
        documents
          .zipWithIndex
          .foldLeft(emptyUserAnswers) {
            case (accumulatedAnswers, (doc, index)) =>
              accumulatedAnswers.set(DocumentPage(Index(0), Index(index)), doc).success.value
          }

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, addDocumentRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = answers.set(AddDocumentPage(index), false).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual goods.AddDocumentPage(index)
          .navigate(waypoints, expectedAnswers)
          .url

        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val document = arbitrary[Document].sample.value
      val answers =emptyUserAnswers.set(DocumentPage(index, index), document).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request =
          FakeRequest(POST, addDocumentRoute)
            .withFormUrlEncodedBody(("value", ""))

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, addDocumentRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, addDocumentRoute)
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
          FakeRequest(GET, routes.AddDocumentController.onPageLoad(waypoints, lrn, Index(maxGoods)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if the item index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, routes.AddDocumentController.onPageLoad(waypoints, lrn, Index(maxGoods)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
