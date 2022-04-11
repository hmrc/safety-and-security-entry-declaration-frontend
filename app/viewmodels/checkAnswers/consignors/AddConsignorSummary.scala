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

package viewmodels.checkAnswers.consignors

import controllers.consignors.{routes => consignorRoutes}
import models.{Index, TraderWithEori, TraderWithoutEori, UserAnswers}
import pages.consignors.{AddConsignorPage, CheckConsignorPage}
import pages.{AddItemPage, CheckAnswersPage, Waypoints}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.consignors.AllConsignorsQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object AddConsignorSummary  {

  def rows(answers: UserAnswers, waypoints: Waypoints, sourcePage: AddItemPage)(implicit messages: Messages): Seq[ListItem] =
    answers.get(AllConsignorsQuery).getOrElse(List.empty).zipWithIndex.map {
      case (consignor, index) =>
        val name = consignor match {
          case t: TraderWithEori    => HtmlFormat.escape(t.eori).toString
          case t: TraderWithoutEori => HtmlFormat.escape(t.name).toString
        }

        ListItem(
          name      = name,
          changeUrl = CheckConsignorPage(Index(index)).changeLink(waypoints, answers.lrn, sourcePage).url,
          removeUrl = consignorRoutes.RemoveConsignorController.onPageLoad(waypoints, answers.lrn,Index(index)).url
        )
    }

  def checkAnswersRow(answers: UserAnswers, waypoints: Waypoints, sourcePage: CheckAnswersPage)
                     (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllConsignorsQuery).map {
      consignors =>

        val value = consignors.map {
          case c: TraderWithEori => c.eori
          case c: TraderWithoutEori => c.name
        }.map(HtmlFormat.escape).mkString("<br>")

        SummaryListRowViewModel(
          key = "consignors.checkYourAnswersLabel",
          value = ValueViewModel(HtmlContent(value)),
          actions = Seq(
            ActionItemViewModel(
              "site.change",
              AddConsignorPage.changeLink(waypoints, answers.lrn, sourcePage).url
            ).withVisuallyHiddenText(messages("consignors.change.hidden"))
          )
        )
    }
}
