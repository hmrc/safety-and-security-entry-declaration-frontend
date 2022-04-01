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

import controllers.consignees.{routes => consigneesRoutes}
import models.{CheckMode, Index, TraderWithEori, TraderWithoutEori, UserAnswers}
import pages.{AddItemPage, Breadcrumbs, CheckAnswersPage}
import pages.consignees.AddAnyNotifiedPartiesPage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.consignees.AllNotifiedPartiesQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object AddNotifiedPartySummary  {

  def rows(answers: UserAnswers, breadcrumbs: Breadcrumbs, sourcePage: AddItemPage)(implicit messages: Messages): Seq[ListItem] =
    answers.get(AllNotifiedPartiesQuery).getOrElse(List.empty).zipWithIndex.map {
      case (notifiedParty, index) =>
        val name = notifiedParty match {
          case t: TraderWithEori    => HtmlFormat.escape(t.eori).toString
          case t: TraderWithoutEori => HtmlFormat.escape(t.name).toString
        }

        val changeLinkBreadcrumbs = breadcrumbs.push(sourcePage.breadcrumb(CheckMode))

        ListItem(
          name      = name,
          changeUrl = consigneesRoutes.CheckNotifiedPartyController.onPageLoad(changeLinkBreadcrumbs, answers.lrn, Index(index)).url,
          removeUrl = consigneesRoutes.RemoveNotifiedPartyController.onPageLoad(breadcrumbs, answers.lrn, Index(index)).url
        )
    }

  def checkAnswersRow(answers: UserAnswers, breadcrumbs: Breadcrumbs, sourcePage: CheckAnswersPage)
                     (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllNotifiedPartiesQuery).map {
      notifiedParties =>

        val changeLinkBreadcrumbs = breadcrumbs.push(sourcePage.breadcrumb)

        val value = notifiedParties.map {
          case np: TraderWithEori => np.eori
          case np: TraderWithoutEori => np.name
        }.map(HtmlFormat.escape).mkString("<br>")

        SummaryListRowViewModel(
          key = "notifiedParties.checkYourAnswersLabel",
          value = ValueViewModel(HtmlContent(value)),
          actions = Seq(
            ActionItemViewModel(
              "site.change",
              AddAnyNotifiedPartiesPage.route(changeLinkBreadcrumbs, answers.lrn).url
            ).withVisuallyHiddenText(messages("notifiedParties.change.hidden"))
          )
        )
    }
}
