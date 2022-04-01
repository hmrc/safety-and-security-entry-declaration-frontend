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

package viewmodels.checkAnswers.consignees

import models.{GbEori, Index, UserAnswers}
import pages.consignees.NotifiedPartyEORIPage
import pages.{Breadcrumbs, CheckAnswersPage, consignees}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object NotifiedPartyEORISummary {

  def row(answers: UserAnswers, index: Index, breadcrumbs: Breadcrumbs, sourcePage: CheckAnswersPage)
         (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(consignees.NotifiedPartyEORIPage(index)).map { answer: GbEori =>

      val changeLinkBreadcrumbs = breadcrumbs.push(sourcePage.breadcrumb)

      SummaryListRowViewModel(
        key = "notifiedPartyEORI.checkYourAnswersLabel",
        value = ValueViewModel(HtmlFormat.escape(answer.value).toString),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            NotifiedPartyEORIPage(index).route(changeLinkBreadcrumbs, answers.lrn).url
          ).withVisuallyHiddenText(messages("notifiedPartyEORI.change.hidden"))
        )
      )
    }
}
