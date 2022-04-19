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
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours

class MarkOrNumberPageSpec extends SpecBase with PageBehaviours {

  "MarkOrNumberPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Check Package Item" in {

        MarkOrNumberPage(index, index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(
            goodsRoutes.CheckPackageItemController
              .onPageLoad(waypoints, emptyUserAnswers.lrn, index, index)
          )
      }
    }
  }
}
