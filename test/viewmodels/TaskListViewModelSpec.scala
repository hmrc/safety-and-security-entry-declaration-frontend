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

package viewmodels

import base.SpecBase
import models.LodgingPersonType
import pages.LodgingPersonTypePage
import play.api.test.Helpers.stubMessages

class TaskListViewModelSpec extends SpecBase {

  "when the user is the Carrier" - {

    "must not include a row for carrier details" in {

      val answers =
        emptyUserAnswers.set(LodgingPersonTypePage, LodgingPersonType.Carrier).success.value

      val taskListViewModel = TaskListViewModel.fromAnswers(answers)(stubMessages())
      taskListViewModel.rows.size mustEqual 1
    }
  }

  "when the user is a Representative" - {

    "must include a row for carrier details" in {

      val answers =
        emptyUserAnswers.set(LodgingPersonTypePage, LodgingPersonType.Representative).success.value

      val taskListViewModel = TaskListViewModel.fromAnswers(answers)(stubMessages())
      taskListViewModel.rows.size mustEqual 2
    }
  }
}
