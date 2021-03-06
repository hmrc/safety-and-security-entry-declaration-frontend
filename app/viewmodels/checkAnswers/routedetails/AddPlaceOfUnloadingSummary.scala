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
import pages.routedetails.{AddPlaceOfLoadingPage, PlaceOfLoadingPage}
import pages.{AddItemPage, CheckAnswersPage, Waypoints}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.routedetails.AllPlacesOfUnloadingQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object AddPlaceOfUnloadingSummary {

  def rows(answers: UserAnswers, waypoints: Waypoints, sourcePage: AddItemPage)
          (implicit messages: Messages): Seq[ListItem] =
    answers.get(AllPlacesOfUnloadingQuery).getOrElse(List.empty).zipWithIndex.map {
      case (placeOfUnloading, index) =>
        ListItem(
          name = HtmlFormat.escape(placeOfUnloading.place).toString,
          changeUrl = PlaceOfLoadingPage(Index(index)).changeLink(waypoints, answers.lrn, sourcePage).url,
          removeUrl = routes.RemovePlaceOfUnloadingController.onPageLoad(waypoints, answers.lrn, Index(index)).url
        )
    }

  def checkAnswersRow(answers: UserAnswers, waypoints: Waypoints, sourcePage: CheckAnswersPage)
                     (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllPlacesOfUnloadingQuery).map { places =>

      val value =
        places
          .map(_.place)
          .map(HtmlFormat.escape)
          .mkString("<br/>")

      SummaryListRowViewModel(
        key = "placeOfUnloading.checkYourAnswersLabel",
        value = ValueViewModel(HtmlContent(value)),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            AddPlaceOfLoadingPage.changeLink(waypoints, answers.lrn, sourcePage).url
          ).withVisuallyHiddenText(messages("placeOfUnloading.change.hidden"))
        )
      )
    }
}
