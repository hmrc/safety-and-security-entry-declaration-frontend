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

import controllers.actions._
import forms.routedetails.AddCountryEnRouteFormProvider
import models.LocalReferenceNumber
import pages.Waypoints
import pages.routedetails.AddCountryEnRoutePage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.routedetails.AddCountryEnRouteSummary
import views.html.routedetails.AddCountryEnRouteView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddCountryEnRouteController @Inject() (
  formProvider: AddCountryEnRouteFormProvider,
  cc: CommonControllerComponents,
  view: AddCountryEnRouteView
)(implicit ec: ExecutionContext) extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    cc.authAndGetData(lrn) { implicit request =>

      val countries = AddCountryEnRouteSummary.rows(request.userAnswers, waypoints, AddCountryEnRoutePage)

      Ok(view(form, waypoints, lrn, countries))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    cc.authAndGetData(lrn).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => {
            val countries = AddCountryEnRouteSummary.rows(request.userAnswers, waypoints, AddCountryEnRoutePage)

            Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, countries)))
          },
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddCountryEnRoutePage, value))
              _ <- cc.sessionRepository.set(updatedAnswers)
            } yield Redirect(AddCountryEnRoutePage.navigate(waypoints, updatedAnswers))
        )
    }
}
