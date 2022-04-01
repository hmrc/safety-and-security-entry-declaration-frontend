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

import models.UserAnswers
import pages.{Breadcrumbs, CheckAnswersPage}
import pages.consignees.AddAnyNotifiedPartiesPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object AddAnyNotifiedPartiesSummary  {

  def row(answers: UserAnswers, breadcrumbs: Breadcrumbs, sourcePage: CheckAnswersPage)
         (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AddAnyNotifiedPartiesPage).map {
      answer =>

        val changeLinkBreadcrumbs = breadcrumbs.push(sourcePage.breadcrumb)
        val value = if (answer) "site.yes" else "site.no"

        SummaryListRowViewModel(
          key     = "addAnyNotifiedParties.checkYourAnswersLabel",
          value   = ValueViewModel(value),
          actions = Seq(
            ActionItemViewModel(
              "site.change",
              AddAnyNotifiedPartiesPage.route(changeLinkBreadcrumbs, answers.lrn).url
            ).withVisuallyHiddenText(messages("addAnyNotifiedParties.change.hidden"))
          )
        )
    }
}
