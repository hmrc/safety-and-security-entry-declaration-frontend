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

package pages.predec

import base.SpecBase
import controllers.predec.{routes => predecRoutes}
import models.ProvideGrossWeight
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours

class ProvideGrossWeightPageSpec extends SpecBase with PageBehaviours {

  "GrossWeightPage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Check Predec when the answer is Per Item" in {

        val answers = emptyUserAnswers.set(ProvideGrossWeightPage, ProvideGrossWeight.PerItem).success.value

        ProvideGrossWeightPage
          .navigate(waypoints, answers)
          .mustEqual(predecRoutes.CheckPredecController.onPageLoad(waypoints, answers.lrn))
      }

      "to Total Gross Weight when the answer is Overall" in {

        val answers = emptyUserAnswers.set(ProvideGrossWeightPage, ProvideGrossWeight.Overall).success.value

        ProvideGrossWeightPage
          .navigate(waypoints, answers)
          .mustEqual(predecRoutes.TotalGrossWeightController.onPageLoad(waypoints, answers.lrn))
      }
    }

    "must not remove Total Gross Weight when the answer is Overall" in {

      val answers = emptyUserAnswers.set(TotalGrossWeightPage, BigDecimal(1)).success.value

      val result = answers.set(ProvideGrossWeightPage, ProvideGrossWeight.Overall).success.value

      result.get(TotalGrossWeightPage).value mustEqual BigDecimal(1)
    }

    "must remove Total Gross Weight when the answer is PerItem" in {

      val answers = emptyUserAnswers.set(TotalGrossWeightPage, BigDecimal(1)).success.value

      val result = answers.set(ProvideGrossWeightPage, ProvideGrossWeight.PerItem).success.value

      result.get(TotalGrossWeightPage) must not be defined
    }
  }
}
