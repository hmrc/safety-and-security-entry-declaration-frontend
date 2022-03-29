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

import controllers.actions._
import forms.consignees.NotifiedPartyEORIFormProvider

import javax.inject.Inject
import models.{GbEori, Index, LocalReferenceNumber, Mode}
import pages.Breadcrumbs
import pages.consignees.NotifiedPartyEORIPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.consignees.NotifiedPartyEORIView

import scala.concurrent.{ExecutionContext, Future}

class NotifiedPartyEORIController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: NotifiedPartyEORIFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: NotifiedPartyEORIView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  val form = formProvider()

  def onPageLoad(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val preparedForm = request.userAnswers.get(NotifiedPartyEORIPage(index)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, breadcrumbs, lrn, index))
    }

  def onSubmit(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, breadcrumbs, lrn, index))),
          { value: GbEori =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(NotifiedPartyEORIPage(index), value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(NotifiedPartyEORIPage(index).navigate(breadcrumbs, updatedAnswers))
          }
        )
    }
}
