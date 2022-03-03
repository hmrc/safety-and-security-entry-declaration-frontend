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
import controllers.preDeclaration.{routes => preDecRoutes}
import controllers.routedetails.{routes => routedetailsRoutes}
import controllers.routes
import models.{LodgingPersonType, NormalMode, UserAnswers}
import pages.preDeclaration.LodgingPersonTypePage
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.tag.Tag

final case class TaskListViewModel(rows: Seq[TaskListRow])(implicit messages: Messages)

object TaskListViewModel {

  def fromAnswers(answers: UserAnswers)(implicit messages: Messages): TaskListViewModel =
    TaskListViewModel(
      Seq(
        Some(routeDetailsRow(answers)),
        carrierDetailsRow(answers),
        Some(consigneesRow(answers))
      ).flatten
    )

  private def routeDetailsRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow =
    TaskListRow(
      messageKey = messages("taskList.routeDetails"),
      link = routedetailsRoutes.CountryOfDepartureController.onPageLoad(NormalMode, answers.lrn),
      id = "route-details",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
    )

  private def carrierDetailsRow(
    answers: UserAnswers
  )(implicit messages: Messages): Option[TaskListRow] =
    answers.get(LodgingPersonTypePage) match {
      case Some(LodgingPersonType.Representative) =>
        Some(
          TaskListRow(
            messageKey = messages("taskList.carrier"),
            link = preDecRoutes.IdentifyCarrierController.onPageLoad(NormalMode, answers.lrn),
            id = "carrier-details",
            completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
          )
        )

      case Some(LodgingPersonType.Carrier) =>
        None

      case None => ???
    }

  private def consigneesRow(answers: UserAnswers)(implicit messages: Messages): TaskListRow =
    TaskListRow(
      messageKey          = messages("taskList.consignees"),
      link                = consigneesRoutes.ConsigneeKnownController.onPageLoad(NormalMode, answers.lrn),
      id                  = "consignees",
      completionStatusTag = CompletionStatus.tag(CompletionStatus.NotStarted)
    )
}

final case class TaskListRow(messageKey: String, link: Call, id: String, completionStatusTag: Tag)(
  implicit messages: Messages
)
