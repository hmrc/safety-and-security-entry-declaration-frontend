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

package controllers

import controllers.actions._
import forms.ContainerNumberFormProvider
import javax.inject.Inject
import models.{Index, LocalReferenceNumber, Mode}
import pages.ContainerNumberPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.ContainerNumberView
import scala.concurrent.{ExecutionContext, Future}

class ContainerNumberController @Inject()(
                                        override val messagesApi: MessagesApi,
                                        sessionRepository: SessionRepository,
                                        identify: IdentifierAction,
                                        getData: DataRetrievalActionProvider,
                                        requireData: DataRequiredAction,
                                        formProvider: ContainerNumberFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: ContainerNumberView
                                    )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber, itemIndex: Index, packageIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
    implicit request =>

      val preparedForm = request.userAnswers.get(ContainerNumberPage(itemIndex, packageIndex)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, mode, lrn, itemIndex, packageIndex))
  }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber, itemIndex: Index, packageIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, mode, lrn, itemIndex, packageIndex))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(ContainerNumberPage(itemIndex, packageIndex), value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(ContainerNumberPage(itemIndex, packageIndex ).navigate(mode, updatedAnswers))
      )
  }
}
