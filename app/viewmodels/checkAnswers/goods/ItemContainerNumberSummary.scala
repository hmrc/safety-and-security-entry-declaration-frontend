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
import models.{Index, NormalMode, UserAnswers}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.AllContainersQuery
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem
import controllers.transport.{routes => transportRoutes}
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object ItemContainerNumberSummary  {

  def rows(answers: UserAnswers, itemIndex: Index)(implicit messages: Messages): List[ListItem] =
    answers.get(AllContainersQuery(itemIndex)).getOrElse(List.empty).zipWithIndex.map {
      case (container, index) =>
        ListItem(
          name = HtmlFormat.escape(container.itemContainerNumber).toString,
          changeUrl = goodsRoutes.ItemContainerNumberController
            .onPageLoad(NormalMode, answers.lrn, itemIndex, Index(index))
            .url,
          removeUrl = goodsRoutes.RemoveItemContainerNumberController
            .onPageLoad(NormalMode, answers.lrn, itemIndex, Index(index))
            .url
        )
    }


  def checkAnswersRow(answers: UserAnswers,  itemIndex: Index)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AllContainersQuery(itemIndex)).map {
      containers =>
        val value = containers.map(c => c.itemContainerNumber).mkString("<br>")

        SummaryListRowViewModel(
          key = "shippingContainers.checkYourAnswersLabel",
          value = ValueViewModel(HtmlContent(value)),
          actions = Seq(
            ActionItemViewModel(
              "site.change",
              transportRoutes.AddOverallDocumentController.onPageLoad(NormalMode, answers.lrn).url
            ).withVisuallyHiddenText(messages("shippingContainers.change.hidden"))
          )
        )
    }
}
