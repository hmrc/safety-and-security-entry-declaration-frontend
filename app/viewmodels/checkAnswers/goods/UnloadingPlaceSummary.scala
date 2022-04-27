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
import pages.goods.UnloadingPlacePage
import pages.{CheckAnswersPage, Waypoints}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.routedetails.AllPlacesOfUnloadingQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object UnloadingPlaceSummary  {

  def row(answers: UserAnswers, itemIndex: Index, waypoints: Waypoints, sourcePage: CheckAnswersPage)
         (implicit messages: Messages): Option[SummaryListRow] =
    for {
      placesOfUnloading <- answers.get(AllPlacesOfUnloadingQuery)
      key               <- answers.get(UnloadingPlacePage(itemIndex))
      placeOfUnloading  <- placesOfUnloading.find(_.key == key)
    } yield {

      val value = ValueViewModel(HtmlFormat.escape(placeOfUnloading.place).toString)

      val actions = if (placesOfUnloading.size > 1) {
        Seq(
          ActionItemViewModel(
            "site.change",
            UnloadingPlacePage(itemIndex).changeLink(waypoints, answers.lrn, sourcePage).url
          ).withVisuallyHiddenText(messages("unloadingPlace.change.hidden"))
        )
      } else {
        Nil
      }

      SummaryListRowViewModel(
        key = "unloadingPlace.checkYourAnswersLabel",
        value = value,
        actions = actions
      )
  }

}
