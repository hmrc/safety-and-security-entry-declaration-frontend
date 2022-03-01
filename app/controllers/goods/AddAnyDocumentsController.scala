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

import controllers.actions._
import forms.goods.AddAnyDocumentsFormProvider
import models.{Index, LocalReferenceNumber, Mode}
import pages.goods.AddAnyDocumentsPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.goods.AddAnyDocumentsView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddAnyDocumentsController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: AddAnyDocumentsFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AddAnyDocumentsView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val preparedForm = request.userAnswers.get(AddAnyDocumentsPage(index)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, mode, lrn, index))
    }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode, lrn, index))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddAnyDocumentsPage(index), value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(AddAnyDocumentsPage(index).navigate(mode, updatedAnswers))
        )
    }
}
