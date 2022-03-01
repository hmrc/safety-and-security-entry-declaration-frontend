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

package controllers

import base.SpecBase
import models.LodgingPersonType
import pages.preDeclaration.LodgingPersonTypePage
import play.api.test.FakeRequest
import play.api.test.Helpers._
import viewmodels.TaskListViewModel
import viewmodels.govuk.SummaryListFluency
import views.html.TaskListView


class TaskListControllerSpec extends SpecBase with SummaryListFluency {

  private val answers =
    emptyUserAnswers.set(LodgingPersonTypePage, LodgingPersonType.Representative).success.value

  "Check Your Answers Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.TaskListController.onPageLoad(lrn).url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[TaskListView]
        val taskList = TaskListViewModel.fromAnswers(answers)(messages(application))

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(lrn, taskList)(
          request,
          messages(application)
        ).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, routes.TaskListController.onPageLoad(lrn).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
