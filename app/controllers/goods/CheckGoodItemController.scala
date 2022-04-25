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

package controllers.goods

import com.google.inject.Inject
import controllers.actions.{DataRequiredAction, DataRetrievalActionProvider, IdentifierAction}
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.CheckGoodsItemPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.goods._
import viewmodels.govuk.summarylist._
import views.html.goods.CheckGoodItemView

class CheckGoodItemController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  val controllerComponents: MessagesControllerComponents,
  view: CheckGoodItemView
) extends FrontendBaseController
  with I18nSupport {

  def onPageLoad(
    waypoints: Waypoints,
    lrn: LocalReferenceNumber,
    itemIndex: Index
  ): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val thisPage = CheckGoodsItemPage(itemIndex)

      val list = SummaryListViewModel(
        rows = Seq(
          CommodityCodeKnownSummary.row(request.userAnswers, itemIndex, waypoints, thisPage),
          CommodityCodeSummary.row(request.userAnswers, itemIndex, waypoints, thisPage),
          GoodsDescriptionSummary.row(request.userAnswers, itemIndex, waypoints, thisPage),
          ConsignorSummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          ConsigneeKnownSummary.row(request.userAnswers, itemIndex, waypoints, thisPage),
          ConsigneeSummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          NotifiedPartySummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          LoadingPlaceSummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          UnloadingPlaceSummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          AnyShippingContainersSummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          ItemContainerNumberSummary.checkAnswersRow(request.userAnswers,itemIndex, waypoints, thisPage),
          GoodsItemGrossWeightSummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          PackageSummary.checkAnswersRow(request.userAnswers, itemIndex, waypoints, thisPage),
          AddAnyDocumentsSummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          DocumentSummary.checkAnswersRow(request.userAnswers,itemIndex, waypoints, thisPage),
          DangerousGoodSummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          DangerousGoodCodeSummary.row(request.userAnswers,itemIndex, waypoints, thisPage),
          PaymentMethodSummary.row(request.userAnswers,itemIndex, waypoints, thisPage)
        ).flatten
      )

      Ok(view(waypoints, list, lrn, itemIndex))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
      implicit request =>
        Redirect(CheckGoodsItemPage(index).navigate(waypoints, request.userAnswers))
    }
}
