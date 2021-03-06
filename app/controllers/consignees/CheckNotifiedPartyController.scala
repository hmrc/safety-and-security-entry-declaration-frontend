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
import controllers.actions.CommonControllerComponents
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.consignees.CheckNotifiedPartyPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.consignees._
import viewmodels.govuk.summarylist._
import views.html.consignees.CheckNotifiedPartyView

class CheckNotifiedPartyController @Inject() (
  cc: CommonControllerComponents,
  view: CheckNotifiedPartyView
) extends FrontendBaseController
  with I18nSupport {

  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn) { implicit request =>

      val list = SummaryListViewModel(
        rows = Seq(
          NotifiedPartyIdentitySummary.row(request.userAnswers, index, waypoints, CheckNotifiedPartyPage(index)),
          NotifiedPartyEORISummary.row(request.userAnswers, index, waypoints, CheckNotifiedPartyPage(index)),
          NotifiedPartyNameSummary.row(request.userAnswers, index, waypoints, CheckNotifiedPartyPage(index)),
          NotifiedPartyAddressSummary.row(request.userAnswers, index, waypoints, CheckNotifiedPartyPage(index))
        ).flatten
      )

      Ok(view(waypoints, list, lrn, index))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn) {
      implicit request =>
        Redirect(CheckNotifiedPartyPage(index).navigate(waypoints, request.userAnswers))
    }
}

