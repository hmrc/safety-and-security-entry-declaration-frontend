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

import controllers.actions._
import forms.transport.AddOverallDocumentFormProvider

import javax.inject.Inject
import models.{LocalReferenceNumber, Mode}
import pages.Waypoints
import pages.transport.AddOverallDocumentPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.transport.OverallDocumentSummary
import views.html.transport.AddOverallDocumentView

import scala.concurrent.{ExecutionContext, Future}

class AddOverallDocumentController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: AddOverallDocumentFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AddOverallDocumentView
)(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  private val form = formProvider()

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] = (identify andThen getData(lrn) andThen requireData) {
    implicit request =>
      val documents = OverallDocumentSummary.rows(request.userAnswers, waypoints, AddOverallDocumentPage)

      Ok(view(form, waypoints, lrn, documents))
  }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async {
      implicit request =>

        form.bindFromRequest().fold(
          formWithErrors => {
            val documents = OverallDocumentSummary.rows(request.userAnswers, waypoints, AddOverallDocumentPage)
            Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, documents)))
          },
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddOverallDocumentPage, value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(AddOverallDocumentPage.navigate(waypoints, updatedAnswers))
        )
    }
}
