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

package viewmodels.checkAnswers.transport

import controllers.transport.{routes => transportRoutes}
import models.{Index, NormalMode, UserAnswers}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.AllOverallDocumentsQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object OverallDocumentSummary {

  def rows(answers: UserAnswers): List[ListItem] =
    answers.get(AllOverallDocumentsQuery).getOrElse(Nil).zipWithIndex.map {
      case (document, index) =>
        ListItem(
          name = HtmlFormat.escape(document.documentType.name).toString,
          changeUrl = transportRoutes.OverallDocumentController
            .onPageLoad(NormalMode, answers.lrn, Index(index))
            .url,
          removeUrl = transportRoutes.RemoveOverallDocumentController
            .onPageLoad(NormalMode, answers.lrn)
            .url
        )
    }

  def checkAnswersRow(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllOverallDocumentsQuery).map {
      documents =>
        val value = documents.map(c=>s"${c.documentType.name}(${c.reference})").mkString("<br>")

        SummaryListRowViewModel(
          key = "addOverallDocument.checkYourAnswersLabel",
          value = ValueViewModel(HtmlContent(value)),
          actions = Seq(
            ActionItemViewModel(
              "site.change",
              transportRoutes.AddOverallDocumentController.onPageLoad(NormalMode, answers.lrn).url
            ).withVisuallyHiddenText(messages("addOverallDocument.change.hidden"))
          )
        )
    }
}
