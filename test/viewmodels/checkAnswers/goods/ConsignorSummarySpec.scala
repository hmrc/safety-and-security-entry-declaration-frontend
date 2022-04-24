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

package viewmodels.checkAnswers.goods

import base.SpecBase
import models.{GbEori, Index, TraderWithEori}
import org.scalacheck.Arbitrary.arbitrary
import pages.EmptyWaypoints
import pages.consignors.ConsignorEORIPage
import pages.goods.{CheckGoodsItemPage, ConsignorPage}
import play.api.i18n.Messages
import play.api.test.Helpers.{stubMessages, stubMessagesApi}
import queries.consignors.ConsignorKeyQuery

class ConsignorSummarySpec extends SpecBase {

  ".row" - {

    val consignor1 = TraderWithEori(1, arbitrary[GbEori].sample.value.toString)
    val consignor2 = TraderWithEori(2, arbitrary[GbEori].sample.value.toString)
    val waypoints = EmptyWaypoints
    val sourcePage = CheckGoodsItemPage(index)
    implicit val msgs: Messages = stubMessages(stubMessagesApi())

    "must include a change link when there is more than one loading place" in {

      val answers =
        emptyUserAnswers
          .set(ConsignorKeyQuery(Index(0)), consignor1.key).success.value
          .set(ConsignorEORIPage(Index(0)), GbEori(consignor1.eori)).success.value
          .set(ConsignorKeyQuery(Index(1)), consignor2.key).success.value
          .set(ConsignorEORIPage(Index(1)), GbEori(consignor2.eori)).success.value
          .set(ConsignorPage(index), consignor1.key).success.value

      val result = ConsignorSummary.row(answers, index, waypoints, sourcePage).value

      result.actions.value.items.size mustEqual 1
    }

    "must not include a change link when there is only one loading place" in {

      val answers =
        emptyUserAnswers
          .set(ConsignorKeyQuery(Index(0)), consignor1.key).success.value
          .set(ConsignorEORIPage(Index(0)), GbEori(consignor1.eori)).success.value
          .set(ConsignorPage(index), consignor1.key).success.value

      val result = ConsignorSummary.row(answers, index, waypoints, sourcePage).value

      result.actions must not be defined
    }
  }
}
