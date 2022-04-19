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
import models.{GbEori, Index}
import org.scalacheck.Arbitrary.arbitrary
import pages.EmptyWaypoints
import pages.behaviours.PageBehaviours
import pages.consignors.ConsignorEORIPage
import queries.consignors.ConsignorKeyQuery

class GoodsDescriptionPageSpec extends SpecBase with PageBehaviours {

  "GoodsDescriptionPage" - {

    val consignor1Eori = arbitrary[GbEori].sample.value
    val consignor2Eori = arbitrary[GbEori].sample.value
    val consignor1Key = 1
    val consignor2Key = 2

    "must navigate when there are no waypoints" - {
      
      val waypoints = EmptyWaypoints

      "to Consignor when there is more than one consignor" in {

        val answers =
          emptyUserAnswers
            .set(ConsignorKeyQuery(Index(0)), consignor1Key).success.value
            .set(ConsignorEORIPage(Index(0)), consignor1Eori).success.value
            .set(ConsignorKeyQuery(Index(1)), consignor2Key).success.value
            .set(ConsignorEORIPage(Index(1)), consignor2Eori).success.value

        GoodsDescriptionPage(Index(0)).navigate(waypoints, answers)
          .mustEqual(goodsRoutes.ConsignorController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }

      "to wherever Consignor navigates to when there is only one consignor" in {

        val answers =
          emptyUserAnswers
            .set(ConsignorKeyQuery(Index(0)), consignor1Key).success.value
            .set(ConsignorEORIPage(Index(0)), consignor1Eori).success.value

        GoodsDescriptionPage(Index(0)).navigate(waypoints, answers)
          .mustEqual(ConsignorPage(Index(0)).navigate(waypoints, answers))
      }
    }
  }
}
