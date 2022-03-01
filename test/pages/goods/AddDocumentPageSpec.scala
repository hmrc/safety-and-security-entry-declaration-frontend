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
import models.{CheckMode, Document, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class AddDocumentPageSpec extends SpecBase with PageBehaviours {

  "AddDocumentPage" - {

    "must navigate in Normal Mode" - {

      "to Document for the next index if the answer is yes" in {

        val document = arbitrary[Document].sample.value

        val answers = emptyUserAnswers.set(DocumentPage(index, Index(0)), document).success.value

        AddDocumentPage(index)
          .navigate(NormalMode, answers, index, addAnother = true)
          .mustEqual(goodsRoutes.DocumentController.onPageLoad(NormalMode, answers.lrn, index, Index(1)))
      }

      "to Dangerous Goods when the answer is no" in {

        val document = arbitrary[Document].sample.value

        val answers = emptyUserAnswers.set(DocumentPage(index, Index(0)), document).success.value

        AddDocumentPage(index)
          .navigate(NormalMode, answers, index, addAnother = false)
          .mustEqual(goodsRoutes.DangerousGoodController.onPageLoad(NormalMode, answers.lrn, index))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddDocumentPage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
