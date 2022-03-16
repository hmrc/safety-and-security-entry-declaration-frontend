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

package controllers.transport

import base.SpecBase
import controllers.{routes => baseRoutes}
import forms.transport.OverallDocumentFormProvider
import models._
import models.Document._
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{never, times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.mockito.MockitoSugar
import pages.transport.OverallDocumentPage
import play.api.inject.bind
import play.api.libs.json.{JsPath, JsSuccess, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.transport.OverallDocumentView

import scala.concurrent.Future

class OverallDocumentControllerSpec extends SpecBase with MockitoSugar {

  private val formProvider = new OverallDocumentFormProvider()
  private val form = formProvider()

  private lazy val overallDocumentRoute = {
    routes.OverallDocumentController.onPageLoad(NormalMode, lrn, index).url
  }
  private val page = OverallDocumentPage(index)

  private val documentType = DocumentType.allDocumentTypes.head
  private val document = Document(documentType, "reference")

  private val userAnswers = {
    emptyUserAnswers.set(OverallDocumentPage(index), document).success.value
  }

  private def userAnswersWithDocs(num: Int): UserAnswers = {
    val docs = Gen.listOfN(num, arbitrary[Document]).sample.value
    val updatedJson = emptyUserAnswers.data.setObject(JsPath \ "overallDocuments", Json.toJson(docs)) match {
      case JsSuccess(jsValue, _) => jsValue
      case _ => throw new IllegalStateException("Failed to set JSON")
    }
    emptyUserAnswers.copy(data = updatedJson)
  }


  "OverallDocument Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, overallDocumentRoute)

        val view = application.injector.instanceOf[OverallDocumentView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(
          form,
          NormalMode,
          lrn,
          index
        )(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, overallDocumentRoute)

        val view = application.injector.instanceOf[OverallDocumentView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(
          form.fill(document),
          NormalMode,
          lrn,
          index
        )(request, messages(application)).toString
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
          FakeRequest(POST, overallDocumentRoute)
            .withFormUrlEncodedBody("type" -> documentType.code, "reference" -> document.reference)

        val result          = route(application, request).value
        val expectedAnswers = emptyUserAnswers.set(page, document).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual page.navigate(NormalMode, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, overallDocumentRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[OverallDocumentView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(
          boundForm,
          NormalMode,
          lrn,
          index
        )(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, overallDocumentRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must return OK and the correct view for a GET if index is below max document limit" in {
      val numDocs = OverallDocumentController.MaxDocuments - 1
      val currentIndex = Index(numDocs)
      val url = routes.OverallDocumentController.onPageLoad(NormalMode, lrn, currentIndex).url
      val answers = userAnswersWithDocs(numDocs)
      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, url)

        val view = application.injector.instanceOf[OverallDocumentView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(
          form,
          NormalMode,
          lrn,
          currentIndex
        )(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if index is above max document limit" in {
      val numDocs = OverallDocumentController.MaxDocuments
      val currentIndex = Index(numDocs)
      val url = routes.OverallDocumentController.onPageLoad(NormalMode, lrn, currentIndex).url
      val answers = userAnswersWithDocs(numDocs)
      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must return OK and navigate to the next page for a POST if index is below max document limit" in {
      val numDocs = OverallDocumentController.MaxDocuments - 1
      val currentIndex = Index(numDocs)
      val url = routes.OverallDocumentController.onPageLoad(NormalMode, lrn, currentIndex).url
      val answers = userAnswersWithDocs(numDocs)
      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
          .build()

      running(application) {
        val request =
          FakeRequest(POST, url)
            .withFormUrlEncodedBody("type" -> documentType.code, "reference" -> document.reference)

        val result = route(application, request).value
        val expectedAnswers = answers.set(OverallDocumentPage(currentIndex), document).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual page.navigate(NormalMode, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must redirect to Journey Recovery for a POST if index is above max document limit" in {
      val numDocs = OverallDocumentController.MaxDocuments
      val currentIndex = Index(numDocs)
      val url = routes.OverallDocumentController.onPageLoad(NormalMode, lrn, currentIndex).url
      val answers = userAnswersWithDocs(numDocs)
      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request =
          FakeRequest(POST, url)
            .withFormUrlEncodedBody("type" -> documentType.code, "reference" -> document.reference)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
        verify(mockSessionRepository, never()).set(any())
      }
    }
  }
}
