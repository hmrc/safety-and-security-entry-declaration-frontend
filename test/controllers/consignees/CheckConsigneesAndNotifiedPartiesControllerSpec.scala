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

package controllers.consignees

import base.SpecBase
import controllers.{routes => baseRoutes}
import pages.{Breadcrumbs, EmptyBreadcrumbs}
import pages.consignees.CheckConsigneesAndNotifiedPartiesPage
import play.api.test.FakeRequest
import play.api.test.Helpers._
import viewmodels.govuk.SummaryListFluency
import views.html.consignees.CheckConsigneesAndNotifiedPartiesView

class CheckConsigneesAndNotifiedPartiesControllerSpec extends SpecBase with SummaryListFluency {

  private val breadcrumbs = EmptyBreadcrumbs

  "Check Consignees and Notified Parties Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(breadcrumbs, lrn).url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[CheckConsigneesAndNotifiedPartiesView]
        val list = SummaryListViewModel(Seq.empty)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(breadcrumbs, list, lrn)(request, messages(application)).toString
      }
    }

    "must redirect to the next page for a POST" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(POST, routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(breadcrumbs, lrn).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value
          .mustEqual(CheckConsigneesAndNotifiedPartiesPage.navigate(breadcrumbs, emptyUserAnswers).url)
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(breadcrumbs, lrn).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
