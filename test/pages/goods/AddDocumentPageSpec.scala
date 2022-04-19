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
import models.{Document, Index}
import org.scalacheck.Arbitrary.arbitrary
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours

class AddDocumentPageSpec extends SpecBase with PageBehaviours {

  "AddDocumentPage" - {

    val document = arbitrary[Document].sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Document for the next index when the answer is yes" in {

        val answers =
          emptyUserAnswers
            .set(DocumentPage(index, Index(0)), document).success.value
            .set(AddDocumentPage(index), true).success.value

        AddDocumentPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.DocumentController.onPageLoad(waypoints, answers.lrn, index, Index(1)))
      }

      "to Dangerous Goods when the answer is no" in {

        val answers =
          emptyUserAnswers
            .set(DocumentPage(index, Index(0)), document).success.value
            .set(AddDocumentPage(index), false).success.value

        AddDocumentPage(index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.DangerousGoodController.onPageLoad(waypoints, answers.lrn, index))
      }
    }
  }
}
