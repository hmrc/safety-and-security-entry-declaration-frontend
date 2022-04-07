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
import forms.routedetails.RemoveCountryEnRouteFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.routedetails.{CountryEnRoutePage, RemoveCountryEnRoutePage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.routedetails.RemoveCountryEnRouteView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RemoveCountryEnRouteController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: RemoveCountryEnRouteFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: RemoveCountryEnRouteView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  val form = formProvider()

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>
      Ok(view(form, waypoints, lrn, index))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, index))),
          value =>
            if (value) {
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.remove(CountryEnRoutePage(index)))
                _              <- sessionRepository.set(updatedAnswers)
              } yield Redirect(RemoveCountryEnRoutePage(index).navigate(waypoints, updatedAnswers))
            } else {
              Future.successful(
                Redirect(RemoveCountryEnRoutePage(index).navigate(waypoints, request.userAnswers))
              )
            }
        )
    }
}
