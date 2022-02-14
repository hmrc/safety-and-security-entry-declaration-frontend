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


class KindOfPackagePageSpec extends SpecBase with PageBehaviours {

  "KindOfPackagePage" - {

    beRetrievable[KindOfPackage](KindOfPackagePage)

    beSettable[KindOfPackage](KindOfPackagePage)

    beRemovable[KindOfPackage](KindOfPackagePage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        KindOfPackagePage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        KindOfPackagePage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
