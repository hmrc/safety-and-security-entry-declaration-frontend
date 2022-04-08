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

import controllers.routedetails.routes
import models.{Index, UserAnswers}
import pages.routedetails.{AddCountryEnRoutePage, CountryEnRoutePage}
import pages.{AddItemPage, CheckAnswersPage, Waypoints}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.routedetails.AllCountriesEnRouteQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object AddCountryEnRouteSummary {

  def rows(answers: UserAnswers, waypoints: Waypoints, sourcePage: AddItemPage)
          (implicit messages: Messages): Seq[ListItem] =
    answers.get(AllCountriesEnRouteQuery).getOrElse(List.empty).zipWithIndex.map {
      case (country, index) =>
        ListItem(
          name = HtmlFormat.escape(country.name).toString,
          changeUrl = CountryEnRoutePage(Index(index)).changeLink(waypoints, answers.lrn, sourcePage).url,
          removeUrl = routes.RemoveCountryEnRouteController.onPageLoad(waypoints, answers.lrn, Index(index)).url
        )
    }

  def checkAnswersRow(answers: UserAnswers, waypoints: Waypoints, sourcePage: CheckAnswersPage)
                     (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllCountriesEnRouteQuery).map { countries =>

      val value = countries.map(_.name).mkString("<br/>")

      SummaryListRowViewModel(
        key = "countryEnRoute.checkYourAnswersLabel",
        value = ValueViewModel(HtmlContent(value)),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            AddCountryEnRoutePage.changeLink(waypoints, answers.lrn, sourcePage).url
          ).withVisuallyHiddenText(messages("countryEnRoute.change.hidden"))
        )
      )
    }
}
