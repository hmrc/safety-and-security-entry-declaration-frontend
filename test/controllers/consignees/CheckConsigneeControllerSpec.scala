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
import pages.{Waypoints, EmptyWaypoints}
import pages.consignees.CheckConsigneePage
import play.api.test.FakeRequest
import play.api.test.Helpers._
import viewmodels.govuk.SummaryListFluency
import views.html.consignees.CheckConsigneeView

class CheckConsigneeControllerSpec extends SpecBase with SummaryListFluency {

  private val waypoints = EmptyWaypoints

  "Check Consignee Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckConsigneeController.onPageLoad(waypoints, lrn, index).url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[CheckConsigneeView]
        val list = SummaryListViewModel(Seq.empty)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(waypoints, list, lrn, index)(request, messages(application)).toString
      }
    }

    "must redirect to the next page for a POST" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(POST, routes.CheckConsigneeController.onSubmit(waypoints, lrn, index).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual CheckConsigneePage(index).navigate(waypoints, emptyUserAnswers).url
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckConsigneeController.onPageLoad(waypoints, lrn, index).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
