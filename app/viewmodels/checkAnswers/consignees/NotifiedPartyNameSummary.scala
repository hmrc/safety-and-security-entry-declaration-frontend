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

import models.{Index, UserAnswers}
import pages.consignees.NotifiedPartyNamePage
import pages.{Breadcrumbs, consignees}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object NotifiedPartyNameSummary {

  def row(answers: UserAnswers, index: Index, breadcrumbs: Breadcrumbs)
         (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(consignees.NotifiedPartyNamePage(index)).map { answer =>

      SummaryListRowViewModel(
        key = "notifiedPartyName.checkYourAnswersLabel",
        value = ValueViewModel(HtmlFormat.escape(answer).toString),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            NotifiedPartyNamePage(index).route(breadcrumbs, answers.lrn).url
          ).withVisuallyHiddenText(messages("notifiedPartyName.change.hidden"))
        )
      )
    }
}
