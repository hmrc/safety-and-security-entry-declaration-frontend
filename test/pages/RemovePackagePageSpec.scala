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

package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, KindOfPackage, NormalMode}
import pages.behaviours.PageBehaviours

class RemovePackagePageSpec extends SpecBase with PageBehaviours {

  "RemovePackagePage" - {

    "must navigate in Normal Mode" - {

      "to Add Package when there is at least one package in user answers" in {

        val answers =
          emptyUserAnswers
            .set(KindOfPackagePage(index, index), KindOfPackage.standardKindsOfPackages.head).success.value
            .set(NumberOfPackagesPage(index, index), 1).success.value
            .set(MarkOrNumberPage(index, index), "Mark or number").success.value

        RemovePackagePage(index, index).navigate(NormalMode, answers)
          .mustEqual(routes.AddPackageController.onPageLoad(NormalMode, answers.lrn, index))
      }

      "to Kind of Package for index 0 when there are no packages in user answers" in {

        RemovePackagePage(index, index).navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.KindOfPackageController.onPageLoad(NormalMode, emptyUserAnswers.lrn, index, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        RemovePackagePage(index, index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
