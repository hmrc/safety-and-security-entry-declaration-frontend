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

package models

import base.SpecBase
import controllers.consignors.{routes => consignorRoutes}

import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.consignors.ConsignorEORIPage
import queries.consignors.ConsignorKeyQuery
import viewmodels.TaskListViewModel

class TaskListViewModelSpec
  extends AnyFreeSpec
  with SpecBase
  with Matchers
  with ScalaCheckPropertyChecks
  with OptionValues {

  "On the task list" - {
    val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()
    "For the consignors section" - {
      "When we already have some" - {
        "we go to the consignors listing page" in {
          val answers =
            emptyUserAnswers
              .set(ConsignorEORIPage(Index(0)), GbEori("123456789000")).success.value
              .set(ConsignorKeyQuery(Index(0)), 1).success.value

          val result = TaskListViewModel.fromAnswers(answers)(messages(application))

          result.rows(3).link mustEqual consignorRoutes.AddConsignorController.onPageLoad(NormalMode,answers.lrn)
        }
      }
      "When have don't have any" - {
        "we go to the first consignor input" in {
          val result = TaskListViewModel.fromAnswers(emptyUserAnswers)(messages(application))

          result.rows(3).link mustEqual consignorRoutes.ConsignorIdentityController.onPageLoad(NormalMode,emptyUserAnswers.lrn,Index(0))
        }
      }
    }
  }
}
