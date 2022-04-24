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

import models.{Index, UserAnswers}
import pages.goods.ConsigneePage
import pages.{CheckAnswersPage, Waypoints}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.consignees.AllConsigneesQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object ConsigneeSummary {

  def row(answers: UserAnswers, itemIndex: Index, waypoints: Waypoints, sourcePage: CheckAnswersPage)
         (implicit messages: Messages): Option[SummaryListRow] =
    for {
      consignees <- answers.get(AllConsigneesQuery)
      key        <- answers.get(ConsigneePage(itemIndex))
      consignee  <- consignees.find(_.key == key)
    } yield {

      val value = ValueViewModel(HtmlFormat.escape(consignee.displayName).toString)

      val actions = if (consignees.size > 1) {
        Seq(
          ActionItemViewModel(
            "site.change",
            ConsigneePage(itemIndex).changeLink(waypoints, answers.lrn, sourcePage).url
          ).withVisuallyHiddenText(messages("consignee.change.hidden"))
        )
      } else {
        Nil
      }

      SummaryListRowViewModel(
        key = "consignee.checkYourAnswersLabel",
        value = value,
        actions = actions
      )
    }
}