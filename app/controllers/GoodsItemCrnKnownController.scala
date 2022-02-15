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
import forms.GoodsItemCrnKnownFormProvider
import javax.inject.Inject
import models.{LocalReferenceNumber, Mode}
import pages.GoodsItemCrnKnownPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.GoodsItemCrnKnownView

import scala.concurrent.{ExecutionContext, Future}

class GoodsItemCrnKnownController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalActionProvider,
                                         requireData: DataRequiredAction,
                                         formProvider: GoodsItemCrnKnownFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: GoodsItemCrnKnownView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber): Action[AnyContent] = (identify andThen getData(lrn) andThen requireData) {
    implicit request =>

      val preparedForm = request.userAnswers.get(GoodsItemCrnKnownPage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, mode, lrn))
  }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber): Action[AnyContent] = (identify andThen getData(lrn) andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, mode, lrn))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(GoodsItemCrnKnownPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(GoodsItemCrnKnownPage.navigate(mode, updatedAnswers))
      )
  }
}
