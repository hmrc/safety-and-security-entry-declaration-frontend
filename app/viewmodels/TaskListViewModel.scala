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

package viewmodels

import controllers.consignees.{routes => consigneesRoutes}
import controllers.consignors.{routes => consignorRoutes}
import controllers.goods.{routes => goodsRoutes}
import controllers.predec.{routes => predecRoutes}
import controllers.transport.{routes => transportRoutes}
import controllers.routedetails.{routes => routedetailsRoutes}
import models.{Index, NormalMode, UserAnswers}
import pages.Breadcrumbs
import play.api.i18n.Messages
import play.api.mvc.Call
import queries.DeriveNumberOfGoods
import queries.consignees.{DeriveNumberOfConsignees, DeriveNumberOfNotifiedParties}
import queries.consignors.DeriveNumberOfConsignors
import uk.gov.hmrc.govukfrontend.views.viewmodels.tag.Tag

final case class TaskListViewModel(rows: Seq[TaskListRow])(implicit messages: Messages)

object TaskListViewModel {

  def fromAnswers(answers: UserAnswers)(implicit messages: Messages): TaskListViewModel =
    TaskListViewModel(
      Seq(
        predecRow(answers),
        transportRow(answers),
        routeDetailsRow(answers),
        consignorsRow(answers),
        consigneesRow(answers),
        goodsRow(answers)
      )
    )

  private def predecRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow =
    TaskListRow(
      messageKey          = messages("taskList.predec"),
      link                = predecRoutes.CheckPredecController.onPageLoad(answers.lrn),
      id                  = "predec",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.Completed)
    )

  private def transportRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow =
    TaskListRow(
      messageKey = messages("taskList.transport"),
      link = transportRoutes.TransportModeController.onPageLoad(NormalMode, answers.lrn),
      id = "transport-details",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
    )

  private def routeDetailsRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow =
    TaskListRow(
      messageKey = messages("taskList.routeDetails"),
      link = routedetailsRoutes.CountryOfDepartureController.onPageLoad(NormalMode, answers.lrn),
      id = "route-details",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
    )

  private def consignorsRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow = {
    val url = answers.get(DeriveNumberOfConsignors) match {
      case Some(size) if size > 0 => consignorRoutes.AddConsignorController.onPageLoad(NormalMode, answers.lrn)
      case _ => consignorRoutes.ConsignorIdentityController.onPageLoad(NormalMode, answers.lrn, Index(0))
    }

    TaskListRow(
      messageKey          = messages("taskList.consignors"),
      link                = url,
      id                  = "consignors",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
    )
  }

  private def consigneesRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow = {
    val url = answers.get(DeriveNumberOfConsignees) match {
      case Some(size) if size > 0 => consigneesRoutes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(Breadcrumbs.empty, answers.lrn)
      case _ => answers.get(DeriveNumberOfNotifiedParties) match {
          case Some(size) if size > 0 => consigneesRoutes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(Breadcrumbs.empty, answers.lrn)
          case _ => consigneesRoutes.AnyConsigneesKnownController.onPageLoad(Breadcrumbs.empty,answers.lrn)
        }
    }

    TaskListRow(
      messageKey          = messages("taskList.consignees"),
      link                = url,
      id                  = "consignees",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
    )
  }

  private def goodsRow(answers: UserAnswers)(implicit messages: Messages) : TaskListRow = {
    val url = answers.get(DeriveNumberOfGoods()) match {
      case Some(size) if size > 0 => goodsRoutes.AddGoodsController.onPageLoad(NormalMode, answers.lrn)
      case _ => goodsRoutes.CommodityCodeKnownController.onPageLoad(NormalMode, answers.lrn, Index(0))
    }

    TaskListRow(
      messageKey = messages("taskList.goods"),
      link = url,
      id = "goods-details",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
    )
  }
}

final case class TaskListRow(messageKey: String, link: Call, id: String, completionStatusTag: Tag)(
  implicit messages: Messages
)
