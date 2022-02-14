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
import models.{CheckMode, Index, KindOfPackage, NormalMode}
import pages.behaviours.PageBehaviours

class AddPackagePageSpec extends SpecBase with PageBehaviours {

  "AddPackagePage" - {

    "must navigate in Normal Mode" - {

      "to Kind of Package for the next index when the answer is yes" in {

        val answers =
          emptyUserAnswers
            .set(KindOfPackagePage(Index(0), Index(0)), KindOfPackage.standardKindsOfPackages.head).success.value
            .set(NumberOfPackagesPage(Index(0), Index(0)), 1).success.value
            .set(MarkOrNumberPage(Index(0), Index(0)), "mark or number").success.value

        AddPackagePage(Index(0)).navigate(NormalMode, answers, Index(0), addAnother = true)
          .mustEqual(routes.KindOfPackageController.onPageLoad(NormalMode, answers.lrn, Index(0), Index(1)))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddPackagePage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
