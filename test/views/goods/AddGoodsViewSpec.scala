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

package views.goods

import base.SpecBase
import config.IndexLimits.maxGoods
import forms.goods.AddGoodsFormProvider
import models.Index
import org.jsoup.Jsoup
import org.scalacheck.Gen
import pages.EmptyWaypoints
import pages.goods.{AddGoodsPage, CommodityCodePage}
import play.api.i18n.Messages
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers.running
import viewmodels.checkAnswers.goods.GoodsSummary
import views.html.goods.AddGoodsView

class AddGoodsViewSpec extends SpecBase {

  "Add Goods View" - {

    "must show yes/no radios when fewer than the maximum number of goods have been added" in {

      val commodityCodes = Gen.listOfN(maxGoods - 1, Gen.numStr).sample.value

      val answers =
        commodityCodes
          .zipWithIndex
          .foldLeft(emptyUserAnswers) {
            case (accumulatedAnswers, (code, index)) =>
              accumulatedAnswers.set(CommodityCodePage(Index(index)), code).success.value
          }

      val application = applicationBuilder(Some(answers)).build()

      running(application) {

        val view = application.injector.instanceOf[AddGoodsView]
        val formProvider = application.injector.instanceOf[AddGoodsFormProvider]

        implicit val msgs: Messages = messages(application)
        implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        val form = formProvider()
        val rows = GoodsSummary.rows(answers, EmptyWaypoints, AddGoodsPage)

        val result = view.apply(form, EmptyWaypoints, lrn, rows)
        val document = Jsoup.parse(result.toString)

        val radio = document.getElementById("value")
        radio.attr("type") mustEqual "radio"

        document.select(".maxReached") mustBe empty
      }
    }

    "must show guidance when the maximum number of documents have been added" in {

      val commodityCodes = Gen.listOfN(maxGoods, Gen.numStr).sample.value

      val answers =
        commodityCodes
          .zipWithIndex
          .foldLeft(emptyUserAnswers) {
            case (accumulatedAnswers, (code, index)) =>
              accumulatedAnswers.set(CommodityCodePage(Index(index)), code).success.value
          }

      val application = applicationBuilder(Some(answers)).build()

      running(application) {

        implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        implicit val msgs: Messages = messages(application)

        val view = application.injector.instanceOf[AddGoodsView]
        val formProvider = application.injector.instanceOf[AddGoodsFormProvider]
        val form = formProvider()
        val rows = GoodsSummary.rows(answers, EmptyWaypoints, AddGoodsPage)

        val result = view.apply(form, EmptyWaypoints, lrn, rows)
        val document = Jsoup.parse(result.toString)

        val input = document.getElementById("value")
        input.attr("type") mustEqual "hidden"

        document.getElementById("maxReached").text mustEqual msgs("addGoods.maximumReached")
      }
    }
  }
}
