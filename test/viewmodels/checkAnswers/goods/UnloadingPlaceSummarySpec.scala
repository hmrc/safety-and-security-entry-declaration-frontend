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
import models.{Index, PlaceOfUnloading}
import org.scalacheck.Arbitrary.arbitrary
import pages.EmptyWaypoints
import pages.goods.{CheckGoodsItemPage, UnloadingPlacePage}
import pages.routedetails.PlaceOfUnloadingPage
import play.api.i18n.Messages
import play.api.test.Helpers.{stubMessages, stubMessagesApi}

class UnloadingPlaceSummarySpec extends SpecBase {

  ".row" - {

    val placeOfUnloading1 = arbitrary[PlaceOfUnloading].sample.value.copy(key = 1)
    val placeOfUnloading2 = arbitrary[PlaceOfUnloading].sample.value.copy(key = 2)
    val waypoints = EmptyWaypoints
    val sourcePage = CheckGoodsItemPage(index)
    implicit val msgs: Messages = stubMessages(stubMessagesApi())

    "must include a change link when there is more than one loading place" in {

      val answers =
        emptyUserAnswers
          .set(PlaceOfUnloadingPage(Index(0)), placeOfUnloading1).success.value
          .set(PlaceOfUnloadingPage(Index(1)), placeOfUnloading2).success.value
          .set(UnloadingPlacePage(index), placeOfUnloading1.key).success.value

      val result = UnloadingPlaceSummary.row(answers, index, waypoints, sourcePage).value

      result.actions.value.items.size mustEqual 1
    }

    "must not include a change link when there is only one loading place" in {

      val answers =
        emptyUserAnswers
          .set(PlaceOfUnloadingPage(Index(0)), placeOfUnloading1).success.value
          .set(UnloadingPlacePage(index), placeOfUnloading1.key).success.value

      val result = UnloadingPlaceSummary.row(answers, index, waypoints, sourcePage).value

      result.actions must not be defined
    }
  }
}
