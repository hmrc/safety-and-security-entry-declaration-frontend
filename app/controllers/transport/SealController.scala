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

package controllers.transport

import config.IndexLimits.maxSeals
import controllers.actions._
import controllers.{routes => baseRoutes}
import forms.transport.SealFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.transport.SealPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.transport.SealView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SealController @Inject() (
  formProvider: SealFormProvider,
  cc: CommonControllerComponents,
  view: SealView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(index, maxSeals)) { implicit request =>

      val preparedForm = request.userAnswers.get(SealPage(index)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, waypoints, lrn, index))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(index, maxSeals)).async { implicit request =>
      val page = SealPage(index)

      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, index))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(page, value))
              _ <- cc.sessionRepository.set(updatedAnswers)
            } yield Redirect(page.navigate(waypoints, updatedAnswers))
        )
    }
}
