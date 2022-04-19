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

import controllers.goods.{routes => goodsRoutes}
import models.{Index, NormalMode, UserAnswers}
import pages.goods.{AddDocumentPage, DocumentPage}
import pages.{AddItemPage, CheckAnswersPage, Waypoints}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.AllDocumentsQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._


object DocumentSummary {

  def rows(answers: UserAnswers, itemIndex: Index, waypoints: Waypoints, sourcePage: AddItemPage)
          (implicit messages: Messages): List[ListItem] =
    answers.get(AllDocumentsQuery(itemIndex)).getOrElse(List.empty).zipWithIndex.map {
      case (document, index) =>
        ListItem(
          name = HtmlFormat.escape(document.documentType.name).toString,
          changeUrl = DocumentPage(itemIndex, Index(index)).changeLink(waypoints, answers.lrn, sourcePage).url,
          removeUrl = goodsRoutes.RemoveDocumentController
            .onPageLoad(waypoints, answers.lrn, itemIndex, Index(index))
            .url
        )
    }

  def checkAnswersRow(answers: UserAnswers,  itemIndex: Index, waypoints: Waypoints, sourcePage: CheckAnswersPage)
                     (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllDocumentsQuery(itemIndex)).map {
      documents =>

        val value =
          documents
            .map(c => s"${c.documentType.name} - ${HtmlFormat.escape(c.reference).toString}")
            .mkString("<br>")

        SummaryListRowViewModel(
          key = "addAnyDocuments.checkYourAnswersLabel",
          value = ValueViewModel(HtmlContent(value)),
          actions = Seq(
            ActionItemViewModel(
              "site.change",
              AddDocumentPage(itemIndex).changeLink(waypoints, answers.lrn, sourcePage).url
            ).withVisuallyHiddenText(messages("addAnyDocuments.change.hidden"))
          )
        )
    }
}
