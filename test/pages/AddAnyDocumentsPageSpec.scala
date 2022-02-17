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
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class AddAnyDocumentsPageSpec extends SpecBase with PageBehaviours {

  "AddAnyDocumentsPage" - {

    beRetrievable[Boolean](AddAnyDocumentsPage(index))

    beSettable[Boolean](AddAnyDocumentsPage(index))

    beRemovable[Boolean](AddAnyDocumentsPage(index))

    "must navigate in Normal Mode" - {

      "to Document for the first index when the answer is yes" in {

        val answers = emptyUserAnswers.set(AddAnyDocumentsPage(index), true).success.value

        AddAnyDocumentsPage(index).navigate(NormalMode, answers)
          .mustEqual(routes.DocumentController.onPageLoad(NormalMode, answers.lrn, index, index))
      }

      "to Dangerous Goods when the answer is no" in {

        val answers = emptyUserAnswers.set(AddAnyDocumentsPage(index), false).success.value

        AddAnyDocumentsPage(index).navigate(NormalMode, answers)
          .mustEqual(routes.DangerousGoodController.onPageLoad(NormalMode, answers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddAnyDocumentsPage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
