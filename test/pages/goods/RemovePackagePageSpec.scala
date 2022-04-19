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
import models.{CheckMode, KindOfPackage}
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours

class RemovePackagePageSpec extends SpecBase with PageBehaviours {

  "RemovePackagePage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Package when there is at least one package in user answers" in {

        val answers =
          emptyUserAnswers
            .set(KindOfPackagePage(index, index), KindOfPackage.standardKindsOfPackages.head).success.value
            .set(NumberOfPackagesPage(index, index), 1).success.value
            .set(MarkOrNumberPage(index, index), "Mark or number").success.value

        RemovePackagePage(index, index)
          .navigate(waypoints, answers)
          .mustEqual(goodsRoutes.AddPackageController.onPageLoad(waypoints, answers.lrn, index))
      }

      "to Kind of Package for index 0 when there are no packages in user answers" in {

        RemovePackagePage(index, index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(
            goodsRoutes.KindOfPackageController
              .onPageLoad(waypoints, emptyUserAnswers.lrn, index, index)
          )
      }
    }
  }
}
