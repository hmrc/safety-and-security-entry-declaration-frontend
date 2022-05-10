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
import config.IndexLimits.maxPackages
import forms.goods.AddPackageFormProvider
import models.{Index, KindOfPackage}
import org.jsoup.Jsoup
import pages.EmptyWaypoints
import pages.goods.{AddPackagePage, KindOfPackagePage, MarkOrNumberPage, NumberOfPackagesPage}
import play.api.i18n.Messages
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers.running
import viewmodels.checkAnswers.goods.PackageSummary
import views.html.goods.AddPackageView

class AddPackageViewSpec extends SpecBase {

  "Add Package View" - {

    "must show yes/no radios when fewer than the maximum number of packages have been added" in {

      val answers =
        (0 until maxPackages - 1)
          .foldLeft(emptyUserAnswers) {
            (accumulatedAnswers, index) =>
              accumulatedAnswers
                .set(KindOfPackagePage(Index(0), Index(index)), KindOfPackage.standardKindsOfPackages.head).success.value
                .set(NumberOfPackagesPage(Index(0), Index(index)), 1).success.value
                .set(MarkOrNumberPage(Index(0), Index(index)), "mark").success.value
          }

      val application = applicationBuilder(Some(answers)).build()

      running(application) {

        val view = application.injector.instanceOf[AddPackageView]
        val formProvider = application.injector.instanceOf[AddPackageFormProvider]

        implicit val msgs: Messages = messages(application)
        implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        val form = formProvider()
        val rows = PackageSummary.rows(answers, Index(0), EmptyWaypoints, AddPackagePage(Index(0)))

        val result = view.apply(form, EmptyWaypoints, lrn, Index(0), rows)
        val document = Jsoup.parse(result.toString)

        val radio = document.getElementById("value")
        radio.attr("type") mustEqual "radio"

        document.select(".maxReached") mustBe empty
      }
    }

    "must show guidance when the maximum number of packages have been added" in {

      val answers =
        (0 until maxPackages)
          .foldLeft(emptyUserAnswers) {
            (accumulatedAnswers, index) =>
              accumulatedAnswers
                .set(KindOfPackagePage(Index(0), Index(index)), KindOfPackage.standardKindsOfPackages.head).success.value
                .set(NumberOfPackagesPage(Index(0), Index(index)), 1).success.value
                .set(MarkOrNumberPage(Index(0), Index(index)), "mark").success.value
          }

      val application = applicationBuilder(Some(answers)).build()

      running(application) {

        implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        implicit val msgs: Messages = messages(application)

        val view = application.injector.instanceOf[AddPackageView]
        val formProvider = application.injector.instanceOf[AddPackageFormProvider]
        val form = formProvider()
        val rows = PackageSummary.rows(answers, Index(0), EmptyWaypoints, AddPackagePage(Index(0)))

        val result = view.apply(form, EmptyWaypoints, lrn, Index(0), rows)
        val document = Jsoup.parse(result.toString)

        val input = document.getElementById("value")
        input.attr("type") mustEqual "hidden"

        document.getElementById("maxReached").text mustEqual msgs("addPackage.maximumReached")
      }
    }
  }
}
