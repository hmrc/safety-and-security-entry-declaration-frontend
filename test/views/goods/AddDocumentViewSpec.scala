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
import config.IndexLimits.maxDocuments
import forms.goods.AddDocumentFormProvider
import models.{Document, Index}
import org.jsoup.Jsoup
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import pages.EmptyWaypoints
import pages.goods.{AddDocumentPage, DocumentPage}
import play.api.i18n.Messages
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers.running
import viewmodels.checkAnswers.goods.DocumentSummary
import views.html.goods.AddDocumentView

class AddDocumentViewSpec extends SpecBase {

  "Add Document View" - {

    "must show yes/no radios when fewer than the maximum number of documents have been added" in {

      val documents = Gen.listOfN(maxDocuments - 1, arbitrary[Document]).sample.value

      val answers =
        documents
          .zipWithIndex
          .foldLeft(emptyUserAnswers) {
            case (accumulatedAnswers, (doc, index)) =>
              accumulatedAnswers.set(DocumentPage(Index(0), Index(index)), doc).success.value
          }

      val application = applicationBuilder(Some(answers)).build()

      running(application) {

        val view = application.injector.instanceOf[AddDocumentView]
        val formProvider = application.injector.instanceOf[AddDocumentFormProvider]

        implicit val msgs: Messages = messages(application)
        implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        val form = formProvider()
        val rows = DocumentSummary.rows(answers, Index(0), EmptyWaypoints, AddDocumentPage(Index(0)))

        val result = view.apply(form, EmptyWaypoints, lrn, Index(0), rows)
        val document = Jsoup.parse(result.toString)

        val radio = document.getElementById("value")
        radio.attr("type") mustEqual "radio"

        document.select(".maxReached") mustBe empty
      }
    }

    "must show guidance when the maximum number of documents have been added" in {

      val documents = Gen.listOfN(maxDocuments, arbitrary[Document]).sample.value

      val answers =
        documents
          .zipWithIndex
          .foldLeft(emptyUserAnswers) {
            case (accumulatedAnswers, (doc, index)) =>
              accumulatedAnswers.set(DocumentPage(Index(0), Index(index)), doc).success.value
          }

      val application = applicationBuilder(Some(answers)).build()

      running(application) {

        implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
        implicit val msgs: Messages = messages(application)

        val view = application.injector.instanceOf[AddDocumentView]
        val formProvider = application.injector.instanceOf[AddDocumentFormProvider]
        val form = formProvider()
        val rows = DocumentSummary.rows(answers, Index(0), EmptyWaypoints, AddDocumentPage(Index(0)))

        val result = view.apply(form, EmptyWaypoints, lrn, Index(0), rows)
        val document = Jsoup.parse(result.toString)

        val input = document.getElementById("value")
        input.attr("type") mustEqual "hidden"

        document.getElementById("maxReached").text mustEqual msgs("addDocument.maximumReached")
      }
    }
  }
}
