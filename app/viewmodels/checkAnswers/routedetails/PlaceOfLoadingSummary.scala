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

import controllers.routedetails.{routes => routedetailsRoutes}
import models.{Index, NormalMode, UserAnswers}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.routedetails.AllPlacesOfLoadingQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object PlaceOfLoadingSummary {

  def checkAnswersRow(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllPlacesOfLoadingQuery).map { places =>

      val value = places.map(_.place).mkString("<br/>")

      SummaryListRowViewModel(
        key = "placeOfLoading.checkYourAnswersLabel",
        value = ValueViewModel(HtmlContent(value)),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            routedetailsRoutes.AddPlaceOfLoadingController.onPageLoad(NormalMode, answers.lrn).url
          ).withVisuallyHiddenText(messages("placeOfLoading.change.hidden"))
        )
      )
    }

  def rows(answers: UserAnswers)(implicit messages: Messages): Seq[ListItem] =
    answers.get(AllPlacesOfLoadingQuery).getOrElse(List.empty).zipWithIndex.map {
      case (placeOfLoading, index) =>
        ListItem(
          name = HtmlFormat.escape(placeOfLoading.place).toString,
          changeUrl = routedetailsRoutes.PlaceOfLoadingController.onPageLoad(NormalMode, answers.lrn, Index(index)).url,
          removeUrl = routedetailsRoutes.RemovePlaceOfLoadingController
            .onPageLoad(NormalMode, answers.lrn, Index(index))
            .url
        )
    }
}
