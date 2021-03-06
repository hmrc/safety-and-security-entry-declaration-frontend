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
import forms.goods.RemoveDocumentFormProvider
import models.{Document, DocumentType, Index}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{never, times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.goods.{DocumentPage, GoodsItemGrossWeightPage, RemoveDocumentPage}
import pages.{EmptyWaypoints, Waypoints, goods}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.goods.RemoveDocumentView

import scala.concurrent.Future

class RemoveDocumentControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new RemoveDocumentFormProvider()
  val form = formProvider()
  val waypoints: Waypoints = EmptyWaypoints

  private val baseAnswers = emptyUserAnswers.set(GoodsItemGrossWeightPage(index),BigDecimal(5)).success.value
    .set(DocumentPage(index,index),Document(DocumentType("test","test"),"test")).success.value

  lazy val removeDocumentRoute =
    routes.RemoveDocumentController.onPageLoad(waypoints, lrn, index, index).url

  "RemoveDocument Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, removeDocumentRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[RemoveDocumentView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints, lrn, index, index)(
          request,
          messages(application)
        ).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(goods.RemoveDocumentPage(index, index), true).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, removeDocumentRoute)

        val view = application.injector.instanceOf[RemoveDocumentView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(true), waypoints, lrn, index, index)(
          request,
          messages(application)
        ).toString
      }
    }

    "must remove the document and redirect to the next page when the answer is yes" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removeDocumentRoute)
            .withFormUrlEncodedBody(("value", "true"))

        val result = route(application, request).value
        val expectedAnswers = baseAnswers.remove(queries.goods.DocumentQuery(index,index)).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemoveDocumentPage(index,index)
          .navigate(waypoints, expectedAnswers)
          .url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must redirect to the next page without removing the document when the answer is no" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, removeDocumentRoute)
            .withFormUrlEncodedBody(("value", "false"))

        val result = route(application, request).value
        val expectedAnswers = baseAnswers

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual RemoveDocumentPage(index,index)
          .navigate(waypoints, expectedAnswers)
          .url
        verify(mockSessionRepository, never()).set(any())
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, removeDocumentRoute)
            .withFormUrlEncodedBody(("value", ""))

        val boundForm = form.bind(Map("value" -> ""))

        val view = application.injector.instanceOf[RemoveDocumentView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints, lrn, index, index)(
          request,
          messages(application)
        ).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, removeDocumentRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, removeDocumentRoute)
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
          FakeRequest(GET, routes.RemoveDocumentController.onPageLoad(waypoints, lrn, Index(maxGoods), index).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if the item index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, routes.RemoveDocumentController.onPageLoad(waypoints, lrn, Index(maxGoods), index).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a GET if the document index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(GET, routes.RemoveDocumentController.onPageLoad(waypoints, lrn, index, Index(maxDocuments)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if the document index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, routes.RemoveDocumentController.onPageLoad(waypoints, lrn, index, Index(maxDocuments)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
