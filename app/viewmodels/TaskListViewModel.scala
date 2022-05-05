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
import controllers.routedetails.{routes => routedetailsRoutes}
import controllers.transport.{routes => transportRoutes}
import extractors.{PredecExtractor, RouteDetailsExtractor, TransportExtractor}
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.EmptyWaypoints
import pages.predec.{DeclarationPlacePage, LocalReferenceNumberPage}
import pages.routedetails.CountryOfDeparturePage
import pages.transport.TransportModePage
import play.api.i18n.Messages
import play.api.mvc.Call
import queries.consignees.{DeriveNumberOfConsignees, DeriveNumberOfNotifiedParties}
import queries.consignors.DeriveNumberOfConsignors
import queries.goods.DeriveNumberOfGoods
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

  private def predecRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow = {
    val isPredeclarationComplete = new PredecExtractor()(answers).extract().isValid

    TaskListRow(
      messageKey = messages("taskList.predec"),
      link =
        if (isPredeclarationComplete)
          predecRoutes.CheckPredecController.onPageLoad(EmptyWaypoints, answers.lrn)
        else
          predecRoutes.LocalReferenceNumberController.onPageLoad(),
      id = "predec",
      completionStatusTag = {
        if (isPredeclarationComplete) {
          CompletionStatus.tag(CompletionStatus.Completed)
        } else {
          answers.get(DeclarationPlacePage)
            .fold(CompletionStatus.tag(CompletionStatus.NotStarted))(_=>CompletionStatus.tag(CompletionStatus.InProgress))
        }
      }
    )
  }

  private def transportRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow = {
    val isTransportComplete = new TransportExtractor()(answers).extract().isValid

    TaskListRow(
      messageKey = messages("taskList.transport"),
      link =
        if (isTransportComplete)
          transportRoutes.CheckTransportController.onPageLoad(EmptyWaypoints, answers.lrn)
        else
          transportRoutes.TransportModeController.onPageLoad(EmptyWaypoints, answers.lrn),
      id = "transport-details",
      completionStatusTag = {
        if (isTransportComplete) {
          CompletionStatus.tag(CompletionStatus.Completed)
        } else {
          answers.get(TransportModePage)
            .fold(CompletionStatus.tag(CompletionStatus.NotStarted))(_=>CompletionStatus.tag(CompletionStatus.InProgress))
        }
      }
    )
  }

  private def routeDetailsRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow = {
    val isRouteComplete = new RouteDetailsExtractor()(answers).extract().isValid

    TaskListRow(
      messageKey = messages("taskList.routeDetails"),
      link =
        if (isRouteComplete)
          routedetailsRoutes.CheckRouteDetailsController.onPageLoad(EmptyWaypoints,answers.lrn)
        else
          routedetailsRoutes.CountryOfDepartureController.onPageLoad(EmptyWaypoints, answers.lrn),
      id = "route-details",
      completionStatusTag = {
        if (isRouteComplete) {
          CompletionStatus.tag(CompletionStatus.Completed)
        } else {
          answers.get(CountryOfDeparturePage)
            .fold(CompletionStatus.tag(CompletionStatus.NotStarted))(_ => CompletionStatus.tag(CompletionStatus.InProgress))
        }
      }
    )
  }

  private def consignorsRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow = {
    val url = answers.get(DeriveNumberOfConsignors) match {
      case Some(size) if size > 0 => consignorRoutes.AddConsignorController.onPageLoad(EmptyWaypoints, answers.lrn)
      case _ => consignorRoutes.ConsignorIdentityController.onPageLoad(EmptyWaypoints, answers.lrn, Index(0))
    }

    TaskListRow(
      messageKey = messages("taskList.consignors"),
      link = url,
      id = "consignors",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
    )
  }

  private def consigneesRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow = {
    val url = answers.get(DeriveNumberOfConsignees) match {
      case Some(size) if size > 0 =>
        consigneesRoutes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(EmptyWaypoints, answers.lrn)
      case _ =>
        answers.get(DeriveNumberOfNotifiedParties) match {
          case Some(size) if size > 0 =>
            consigneesRoutes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(EmptyWaypoints, answers.lrn)
          case _ => consigneesRoutes.AnyConsigneesKnownController.onPageLoad(EmptyWaypoints, answers.lrn)
        }
    }

    TaskListRow(
      messageKey = messages("taskList.consignees"),
      link = url,
      id = "consignees",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
    )
  }

  private def goodsRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow = {
    val url = answers.get(DeriveNumberOfGoods) match {
      case Some(size) if size > 0 => goodsRoutes.AddGoodsController.onPageLoad(EmptyWaypoints, answers.lrn)
      case _ => goodsRoutes.InitialiseGoodsItemController.initialise(EmptyWaypoints, answers.lrn, Index(0))
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
