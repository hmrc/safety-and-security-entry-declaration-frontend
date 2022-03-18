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
import controllers.{routes => baseRoutes}
import forms.transport.SealFormProvider
import javax.inject.Inject
import models.{Index, LocalReferenceNumber, Mode}
import pages.transport.SealPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.transport.SealView

import scala.concurrent.{ExecutionContext, Future}

class SealController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: SealFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: SealView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      if (index.position >= SealController.MaxDocuments) {
        Redirect(baseRoutes.JourneyRecoveryController.onPageLoad())
      } else {
        val preparedForm = request.userAnswers.get(SealPage(index)) match {
          case None => form
          case Some(value) => form.fill(value)
        }

        Ok(view(preparedForm, mode, lrn, index))
      }
    }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async { implicit request =>
      val page = SealPage(index)

      if (index.position >= SealController.MaxDocuments) {
        Future.successful(Redirect(baseRoutes.JourneyRecoveryController.onPageLoad()))
      } else {
        form
          .bindFromRequest()
          .fold(
            formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode, lrn, index))),
            value =>
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.set(page, value))
                _ <- sessionRepository.set(updatedAnswers)
              } yield Redirect(page.navigate(mode, updatedAnswers))
          )
      }
    }
}

object SealController {
  val MaxDocuments = 99
}
