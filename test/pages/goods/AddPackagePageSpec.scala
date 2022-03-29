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

package pages.goods

import base.SpecBase
import controllers.goods.{routes => goodsRoutes}
import controllers.routes
import models.{CheckMode, Index, KindOfPackage, NormalMode, ProvideGrossWeight}
import pages.behaviours.PageBehaviours
import pages.predec.ProvideGrossWeightPage

class AddPackagePageSpec extends SpecBase with PageBehaviours {

  "AddPackagePage" - {

    "must navigate in Normal Mode" - {
      "to Add Any Documents" in {
        val answers =
          emptyUserAnswers
            .set(ProvideGrossWeightPage, ProvideGrossWeight.Overall).success.value

        AddPackagePage(index)
          .navigate(NormalMode, answers, index, addAnother = false)
          .mustEqual(
            goodsRoutes.AddAnyDocumentsController.onPageLoad(NormalMode, answers.lrn, index)
          )
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddPackagePage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
