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

import controllers.goods.{routes => goodsRoutes}
import models.{Index, UserAnswers}
import pages.goods.{AddPackagePage, CheckPackageItemPage}
import pages.{AddItemPage, CheckAnswersPage, Waypoints}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.goods.AllPackageItemsQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object PackageSummary {

  def rows(answers: UserAnswers, itemIndex: Index, waypoints: Waypoints, sourcePage: AddItemPage)
          (implicit messages: Messages): Seq[ListItem] =
    answers.get(AllPackageItemsQuery(itemIndex)).getOrElse(List.empty).zipWithIndex.map {
      case (packageItem, index) =>
        ListItem(
          name = HtmlFormat.escape(packageItem.kind.code + " - " + packageItem.kind.name).toString,
          changeUrl = CheckPackageItemPage(itemIndex, Index(index)).changeLink(waypoints, answers.lrn, sourcePage).url,
          removeUrl = goodsRoutes.RemovePackageController
            .onPageLoad(waypoints, answers.lrn, itemIndex, Index(index))
            .url
        )
    }

  def checkAnswersRow(answers: UserAnswers,  itemIndex: Index, waypoints: Waypoints, sourcePage: CheckAnswersPage)
                     (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllPackageItemsQuery(itemIndex)).map {
      packages =>
        val value = packages.map(c => s"${c.kind.name} (${c.kind.code})").mkString("<br>")

        SummaryListRowViewModel(
          key = "kindOfPackage.checkYourAnswersLabel",
          value = ValueViewModel(HtmlContent(value)),
          actions = Seq(
            ActionItemViewModel(
              "site.change",
              AddPackagePage(itemIndex).changeLink(waypoints, answers.lrn, sourcePage).url
            ).withVisuallyHiddenText(messages("kindOfPackage.change.hidden"))
          )
        )
    }
}
