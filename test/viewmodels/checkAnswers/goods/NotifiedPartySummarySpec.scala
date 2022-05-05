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
import pages.consignees.NotifiedPartyEORIPage
import pages.goods.{CheckGoodsItemPage, NotifiedPartyPage}
import play.api.i18n.Messages
import play.api.test.Helpers.{stubMessages, stubMessagesApi}
import queries.consignees.NotifiedPartyKeyQuery

class NotifiedPartySummarySpec extends SpecBase {

  ".row" - {

    val consignee1 = TraderWithEori(1, arbitrary[GbEori].sample.value)
    val consignee2 = TraderWithEori(2, arbitrary[GbEori].sample.value)
    val waypoints = EmptyWaypoints
    val sourcePage = CheckGoodsItemPage(index)
    implicit val msgs: Messages = stubMessages(stubMessagesApi())

    "must include a change link when there is more than one loading place" in {

      val answers =
        emptyUserAnswers
          .set(NotifiedPartyKeyQuery(Index(0)), consignee1.key).success.value
          .set(NotifiedPartyEORIPage(Index(0)), consignee1.eori).success.value
          .set(NotifiedPartyKeyQuery(Index(1)), consignee2.key).success.value
          .set(NotifiedPartyEORIPage(Index(1)), consignee2.eori).success.value
          .set(NotifiedPartyPage(index), consignee1.key).success.value

      val result = NotifiedPartySummary.row(answers, index, waypoints, sourcePage).value

      result.actions.value.items.size mustEqual 1
    }

    "must not include a change link when there is only one loading place" in {

      val answers =
        emptyUserAnswers
          .set(NotifiedPartyKeyQuery(Index(0)), consignee1.key).success.value
          .set(NotifiedPartyEORIPage(Index(0)), consignee1.eori).success.value
          .set(NotifiedPartyPage(index), consignee1.key).success.value

      val result = NotifiedPartySummary.row(answers, index, waypoints, sourcePage).value

      result.actions must not be defined
    }
  }
}
