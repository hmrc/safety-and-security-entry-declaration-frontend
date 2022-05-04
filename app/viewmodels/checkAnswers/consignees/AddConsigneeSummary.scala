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
import models.{Index, TraderWithEori, TraderWithoutEori, UserAnswers}
import pages.consignees.{AddConsigneePage, CheckConsigneePage}
import pages.{AddItemPage, Waypoints, CheckAnswersPage}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.consignees.AllConsigneesQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object AddConsigneeSummary  {

  def rows(answers: UserAnswers, waypoints: Waypoints, sourcePage: AddItemPage): Seq[ListItem] =
    answers.get(AllConsigneesQuery).getOrElse(List.empty).zipWithIndex.map {
      case (consignee, index) =>
        val name = consignee match {
          case t: TraderWithEori    => HtmlFormat.escape(t.eori.toString).toString
          case t: TraderWithoutEori => HtmlFormat.escape(t.name).toString
        }

        ListItem(
          name      = name,
          changeUrl = CheckConsigneePage(Index(index)).changeLink(waypoints, answers.lrn, sourcePage).url,
          removeUrl = consigneesRoutes.RemoveConsigneeController.onPageLoad(waypoints, answers.lrn, Index(index)).url
        )
    }

  def checkAnswersRow(answers: UserAnswers, waypoints: Waypoints, sourcePage: CheckAnswersPage)
                     (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllConsigneesQuery).map {
      consignees =>

        val value = consignees.map {
          case c: TraderWithEori => c.eori.toString
          case c: TraderWithoutEori => c.name
        }.map(HtmlFormat.escape).mkString("<br>")

        SummaryListRowViewModel(
          key = "consignees.checkYourAnswersLabel",
          value = ValueViewModel(HtmlContent(value)),
          actions = Seq(
            ActionItemViewModel(
              "site.change",
              AddConsigneePage.changeLink(waypoints, answers.lrn, sourcePage).url
            ).withVisuallyHiddenText(messages("consignees.change.hidden"))
          )
        )
    }
}
