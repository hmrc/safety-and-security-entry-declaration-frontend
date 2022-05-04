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
import pages.consignees.ConsigneeEORIPage
import pages.goods.{CheckGoodsItemPage, ConsigneePage}
import play.api.i18n.Messages
import play.api.test.Helpers.{stubMessages, stubMessagesApi}
import queries.consignees.ConsigneeKeyQuery

class ConsigneeSummarySpec extends SpecBase {

  ".row" - {

    val consignee1 = TraderWithEori(1, GbEori(arbitrary[GbEori].sample.value.toString))
    val consignee2 = TraderWithEori(2, GbEori(arbitrary[GbEori].sample.value.toString))
    val waypoints = EmptyWaypoints
    val sourcePage = CheckGoodsItemPage(index)
    implicit val msgs: Messages = stubMessages(stubMessagesApi())

    "must include a change link when there is more than one loading place" in {

      val answers =
        emptyUserAnswers
          .set(ConsigneeKeyQuery(Index(0)), consignee1.key).success.value
          .set(ConsigneeEORIPage(Index(0)), consignee1.eori).success.value
          .set(ConsigneeKeyQuery(Index(1)), consignee2.key).success.value
          .set(ConsigneeEORIPage(Index(1)), consignee2.eori).success.value
          .set(ConsigneePage(index), consignee1.key).success.value

      val result = ConsigneeSummary.row(answers, index, waypoints, sourcePage).value

      result.actions.value.items.size mustEqual 1
    }

    "must not include a change link when there is only one loading place" in {

      val answers =
        emptyUserAnswers
          .set(ConsigneeKeyQuery(Index(0)), consignee1.key).success.value
          .set(ConsigneeEORIPage(Index(0)), consignee1.eori).success.value
          .set(ConsigneePage(index), consignee1.key).success.value

      val result = ConsigneeSummary.row(answers, index, waypoints, sourcePage).value

      result.actions must not be defined
    }
  }
}
