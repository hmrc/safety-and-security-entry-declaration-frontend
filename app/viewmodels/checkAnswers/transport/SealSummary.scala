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
import queries.AllSealsQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._


object SealSummary  {

  def rows(answers: UserAnswers): List[ListItem] =
    answers.get(AllSealsQuery).getOrElse(Nil).zipWithIndex.map {
      case (seal, index) =>
        ListItem(
          name = HtmlFormat.escape(seal).toString,
          changeUrl = transportRoutes.SealController
            .onPageLoad(NormalMode, answers.lrn, Index(index))
            .url,
          removeUrl = transportRoutes.RemoveSealController
            .onPageLoad(NormalMode, answers.lrn)
            .url
        )
    }

  def checkAnswersRow(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllSealsQuery).map {
      seals =>
        val value = seals.mkString("<br>")

        SummaryListRowViewModel(
          key = "addAnySeals.checkYourAnswersLabel",
          value = ValueViewModel(HtmlContent(value)),
          actions = Seq(
            ActionItemViewModel(
              "site.change",
              transportRoutes.AddSealController.onPageLoad(NormalMode, answers.lrn).url
            ).withVisuallyHiddenText(messages("addAnySeals.change.hidden"))
          )
        )
    }
}
