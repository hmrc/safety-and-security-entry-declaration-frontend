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

package controllers.consignees

import com.google.inject.Inject
import controllers.actions.{DataRequiredAction, DataRetrievalActionProvider, IdentifierAction}
import models.LocalReferenceNumber
import pages.Breadcrumbs
import pages.consignees.CheckConsigneesAndNotifiedPartiesPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.consignees._
import viewmodels.govuk.summarylist._
import views.html.consignees.CheckConsigneesAndNotifiedPartiesView

class CheckConsigneesAndNotifiedPartiesController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  val controllerComponents: MessagesControllerComponents,
  view: CheckConsigneesAndNotifiedPartiesView
) extends FrontendBaseController with I18nSupport {

  def onPageLoad(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val changeLinkBreadcrumbs = breadcrumbs.push(CheckConsigneesAndNotifiedPartiesPage.breadcrumb)

      val list = SummaryListViewModel(
        rows = Seq(
          AnyConsigneesKnownSummary.row(request.userAnswers, changeLinkBreadcrumbs),
          AddConsigneeSummary.checkAnswersRow(request.userAnswers, changeLinkBreadcrumbs),
          AddAnyNotifiedPartiesSummary.row(request.userAnswers, changeLinkBreadcrumbs),
          AddNotifiedPartySummary.checkAnswersRow(request.userAnswers, changeLinkBreadcrumbs)
        ).flatten
      )

      Ok(view(breadcrumbs, list, lrn))
    }


  def onSubmit(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
      implicit request =>

        Redirect(CheckConsigneesAndNotifiedPartiesPage.navigate(breadcrumbs, request.userAnswers))
    }
}

