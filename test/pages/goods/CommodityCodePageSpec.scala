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
import models.{CheckMode, GbEori, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.consignors.ConsignorEORIPage
import queries.consignors.ConsignorKeyQuery

class CommodityCodePageSpec extends SpecBase with PageBehaviours {

  "CommodityCodePage" - {

    beRetrievable[String](CommodityCodePage(index))

    beSettable[String](CommodityCodePage(index))

    beRemovable[String](CommodityCodePage(index))

    val consignor1Eori = arbitrary[GbEori].sample.value
    val consignor2Eori = arbitrary[GbEori].sample.value
    val consignor1Key = 1
    val consignor2Key = 2

    "must navigate in Normal Mode" - {

      "to Consignor when there is more than one consignor" in {

        val answers =
          emptyUserAnswers
            .set(ConsignorKeyQuery(Index(0)), consignor1Key).success.value
            .set(ConsignorEORIPage(Index(0)), consignor1Eori).success.value
            .set(ConsignorKeyQuery(Index(1)), consignor2Key).success.value
            .set(ConsignorEORIPage(Index(1)), consignor2Eori).success.value

        CommodityCodePage(Index(0)).navigate(NormalMode, answers)
          .mustEqual(goodsRoutes.ConsignorController.onPageLoad(NormalMode, emptyUserAnswers.lrn, index))
      }

      "to wherever Consignor navigates to when there is only one consignor" in {

        val answers =
          emptyUserAnswers
            .set(ConsignorKeyQuery(Index(0)), consignor1Key).success.value
            .set(ConsignorEORIPage(Index(0)), consignor1Eori).success.value

        CommodityCodePage(Index(0)).navigate(NormalMode, answers)
          .mustEqual(ConsignorPage(Index(0)).navigate(NormalMode, answers))
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        CommodityCodePage(index)
          .navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
