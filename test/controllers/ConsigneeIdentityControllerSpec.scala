package controllers

import base.SpecBase
import forms.ConsigneeIdentityFormProvider
import models.{NormalMode, ConsigneeIdentity}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.ConsigneeIdentityPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import views.html.ConsigneeIdentityView

import scala.concurrent.Future

class ConsigneeIdentityControllerSpec extends SpecBase with MockitoSugar {

  lazy val consigneeIdentityRoute = routes.ConsigneeIdentityController.onPageLoad(NormalMode, lrn).url

  val formProvider = new ConsigneeIdentityFormProvider()
  val form = formProvider()

  "ConsigneeIdentity Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, consigneeIdentityRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[ConsigneeIdentityView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, lrn)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(ConsigneeIdentityPage, ConsigneeIdentity.values.head).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, consigneeIdentityRoute)

        val view = application.injector.instanceOf[ConsigneeIdentityView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(ConsigneeIdentity.values.head), NormalMode, lrn)(request, messages(application)).toString
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
          FakeRequest(POST, consigneeIdentityRoute)
            .withFormUrlEncodedBody(("value", ConsigneeIdentity.values.head.toString))

        val result          = route(application, request).value
        val expectedAnswers = emptyUserAnswers.set(ConsigneeIdentityPage, ConsigneeIdentity.values.head).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual ConsigneeIdentityPage.navigate(NormalMode, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, consigneeIdentityRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[ConsigneeIdentityView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode, lrn)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, consigneeIdentityRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, consigneeIdentityRoute)
            .withFormUrlEncodedBody(("value", ConsigneeIdentity.values.head.toString))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}