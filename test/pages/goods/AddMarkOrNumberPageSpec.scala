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
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours

class AddMarkOrNumberPageSpec extends SpecBase with PageBehaviours {

  "AddMarkOrNumberPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Mark or Number if the answer is yes" in {

        val answers = emptyUserAnswers.set(AddMarkOrNumberPage(index, index), true).success.value

        AddMarkOrNumberPage(index, index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.MarkOrNumberController.onPageLoad(waypoints, answers.lrn, index, index))
      }

      "to Check Package Item if the answer is no" in {

        val answers = emptyUserAnswers.set(AddMarkOrNumberPage(index, index), false).success.value

        AddMarkOrNumberPage(index, index).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.CheckPackageItemController.onPageLoad(waypoints, answers.lrn, index, index))
      }
    }
  }
}
