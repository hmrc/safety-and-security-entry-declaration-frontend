package controllers

import base.SpecBase
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.EnrolmentRequiredView

class EnrolmentRequiredControllerSpec extends SpecBase {

  "EnrolmentRequired Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.EnrolmentRequiredController.onPageLoad(lrn).url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[EnrolmentRequiredView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(lrn)(request, messages(application)).toString
      }
    }
  }
}
