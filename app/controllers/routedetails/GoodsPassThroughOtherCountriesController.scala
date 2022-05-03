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
import forms.routedetails.GoodsPassThroughOtherCountriesFormProvider
import models.LocalReferenceNumber
import pages.Waypoints
import pages.routedetails.GoodsPassThroughOtherCountriesPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.routedetails.GoodsPassThroughOtherCountriesView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class GoodsPassThroughOtherCountriesController @Inject() (
  formProvider: GoodsPassThroughOtherCountriesFormProvider,
  cc: CommonControllerComponents,
  view: GoodsPassThroughOtherCountriesView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    cc.authAndGetData(lrn) { implicit request =>

      val preparedForm = request.userAnswers.get(GoodsPassThroughOtherCountriesPage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, waypoints, lrn))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    cc.authAndGetData(lrn).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, waypoints, lrn))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(GoodsPassThroughOtherCountriesPage, value))
              _ <- cc.sessionRepository.set(updatedAnswers)
            } yield Redirect(GoodsPassThroughOtherCountriesPage.navigate(waypoints, updatedAnswers))
        )
    }
}
