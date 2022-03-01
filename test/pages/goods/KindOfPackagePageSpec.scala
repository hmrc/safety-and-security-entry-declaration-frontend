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
import models.{CheckMode, KindOfPackage, NormalMode}
import org.scalacheck.Gen
import pages.behaviours.PageBehaviours

class KindOfPackagePageSpec extends SpecBase with PageBehaviours {

  "KindOfPackagePage" - {

    beRetrievable[KindOfPackage](KindOfPackagePage(index, index))

    beSettable[KindOfPackage](KindOfPackagePage(index, index))

    beRemovable[KindOfPackage](KindOfPackagePage(index, index))

    "must navigate in Normal Mode" - {

      "to Add Mark or Number when the answer is a bulk kind of package" in {

        val packaging = Gen.oneOf(KindOfPackage.bulkKindsOfPackage).sample.value
        val answers = emptyUserAnswers.set(KindOfPackagePage(index, index), packaging).success.value

        KindOfPackagePage(index, index)
          .navigate(NormalMode, answers)
          .mustEqual(
            goodsRoutes.AddMarkOrNumberController.onPageLoad(NormalMode, answers.lrn, index, index)
          )
      }

      "to Number of Packages when the answer is a standard kind of package" in {

        val packaging = Gen.oneOf(KindOfPackage.standardKindsOfPackages).sample.value
        val answers = emptyUserAnswers.set(KindOfPackagePage(index, index), packaging).success.value

        KindOfPackagePage(index, index)
          .navigate(NormalMode, answers)
          .mustEqual(
            goodsRoutes.NumberOfPackagesController.onPageLoad(NormalMode, answers.lrn, index, index)
          )
      }

      "to Number of Pieces when the answer is an unpacked kind of package" in {

        val packaging = Gen.oneOf(KindOfPackage.unpackedKindsOfPackage).sample.value
        val answers = emptyUserAnswers.set(KindOfPackagePage(index, index), packaging).success.value

        KindOfPackagePage(index, index)
          .navigate(NormalMode, answers)
          .mustEqual(
            goodsRoutes.NumberOfPiecesController.onPageLoad(NormalMode, answers.lrn, index, index)
          )
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        KindOfPackagePage(index, index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
