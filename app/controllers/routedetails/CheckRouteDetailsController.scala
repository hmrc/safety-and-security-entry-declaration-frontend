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

package controllers.routedetails

import com.google.inject.Inject
import controllers.actions.{DataRequiredAction, DataRetrievalActionProvider, IdentifierAction}
import models.LocalReferenceNumber
import pages.Waypoints
import pages.routedetails.CheckRouteDetailsPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.routedetails._
import viewmodels.govuk.summarylist._
import views.html.routedetails.CheckRouteDetailsView

class CheckRouteDetailsController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  val controllerComponents: MessagesControllerComponents,
  view: CheckRouteDetailsView
) extends FrontendBaseController
  with I18nSupport {

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val list = SummaryListViewModel(
        rows = Seq(
          CountryOfDepartureSummary.row(request.userAnswers, waypoints, CheckRouteDetailsPage),
          AddPlaceOfLoadingSummary.checkAnswersRow(request.userAnswers, waypoints, CheckRouteDetailsPage),
          GoodsPassThroughOtherCountriesSummary.row(request.userAnswers, waypoints, CheckRouteDetailsPage),
          AddCountryEnRouteSummary.checkAnswersRow(request.userAnswers, waypoints, CheckRouteDetailsPage),
          CustomsOfficeOfFirstEntrySummary.row(request.userAnswers, waypoints, CheckRouteDetailsPage),
          ArrivalDateAndTimeSummary.row(request.userAnswers, waypoints, CheckRouteDetailsPage),
          AddPlaceOfUnloadingSummary.checkAnswersRow(request.userAnswers, waypoints, CheckRouteDetailsPage)
        ).flatten
      )

      Ok(view(waypoints, list, lrn))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
      implicit request =>
        Redirect(CheckRouteDetailsPage.navigate(waypoints, request.userAnswers))
    }
}
