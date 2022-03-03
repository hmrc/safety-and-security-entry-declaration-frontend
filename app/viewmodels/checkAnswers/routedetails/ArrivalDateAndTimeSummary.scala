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

package viewmodels.checkAnswers.routedetails

import controllers.routedetails.{routes => routeDetailRoutes}
import models.{CheckMode, UserAnswers}
import pages.routedetails.ArrivalDateAndTimePage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow

import java.time.format.DateTimeFormatter
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object ArrivalDateAndTimeSummary {

  private def dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  private def timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

  def row(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(ArrivalDateAndTimePage).map { answer =>

      val value = HtmlFormat
        .escape(answer.date.format(dateFormatter))
        .toString + "<br/>" + HtmlFormat.escape(answer.time.format(timeFormatter)).toString

      SummaryListRowViewModel(
        key = "arrivalDateAndTime.checkYourAnswersLabel",
        value = ValueViewModel(HtmlContent(value)),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            routeDetailRoutes.ArrivalDateAndTimeController.onPageLoad(CheckMode, answers.lrn).url
          ).withVisuallyHiddenText(messages("arrivalDateAndTime.change.hidden"))
        )
      )
    }
}
