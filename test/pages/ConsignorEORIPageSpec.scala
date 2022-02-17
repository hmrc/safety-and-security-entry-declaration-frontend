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


class ConsignorEORIPageSpec extends SpecBase with PageBehaviours {

  "ConsignorEORIPage" - {

    beRetrievable[String](ConsignorEORIPage(index))

    beSettable[String](ConsignorEORIPage(index))

    beRemovable[String](ConsignorEORIPage(index))

    "must navigate in Normal Mode" - {

      "to `Do you know the consignee?`" in {

        ConsignorEORIPage(index).navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.ConsigneeKnownController.onPageLoad(NormalMode,emptyUserAnswers.lrn,index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsignorEORIPage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}