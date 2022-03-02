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

package viewmodels.checkAnswers.routeDetails

import controllers.routeDetails.{routes => routeDetailRoutes}
import models.{Index, NormalMode, UserAnswers}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.routeDetails.AllCountriesEnRouteQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object CountryEnRouteSummary {

  def checkAnswersRow(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllCountriesEnRouteQuery).map { countries =>

      val value = countries.map(_.name).mkString("<br/>")

      SummaryListRowViewModel(
        key = "countryEnRoute.checkYourAnswersLabel",
        value = ValueViewModel(HtmlContent(value)),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            routeDetailRoutes.AddCountryEnRouteController.onPageLoad(NormalMode, answers.lrn).url
          ).withVisuallyHiddenText(messages("countryEnRoute.change.hidden"))
        )
      )
    }

  def rows(answers: UserAnswers)(implicit messages: Messages): Seq[ListItem] =
    answers.get(AllCountriesEnRouteQuery).getOrElse(List.empty).zipWithIndex.map {
      case (country, index) =>
        ListItem(
          name = HtmlFormat.escape(country.name).toString,
          changeUrl = routeDetailRoutes.CountryEnRouteController.onPageLoad(NormalMode, answers.lrn, Index(index)).url,
          removeUrl = routeDetailRoutes.RemoveCountryEnRouteController
            .onPageLoad(NormalMode, answers.lrn, Index(index))
            .url
        )
    }
}