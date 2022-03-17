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
import forms.goods.AddItemContainerNumberFormProvider
import models.{Index, LocalReferenceNumber, Mode}
import pages.goods.AddItemContainerNumberPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.goods.ItemContainerNumberSummary
import views.html.goods.AddItemContainerNumberView

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class AddItemContainerNumberController @Inject()(
      override val messagesApi: MessagesApi,
      sessionRepository: SessionRepository,
      identify: IdentifierAction,
      getData: DataRetrievalActionProvider,
      requireData: DataRequiredAction,
      formProvider: AddItemContainerNumberFormProvider,
      val controllerComponents: MessagesControllerComponents,
      view: AddItemContainerNumberView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val containers = ItemContainerNumberSummary.rows(request.userAnswers, index)

      Ok(view(form, mode, lrn, index, containers))
    }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => {
            val containers = ItemContainerNumberSummary.rows(request.userAnswers, index)

            BadRequest(view(formWithErrors, mode, lrn, index, containers))
          },
          value => Redirect(AddItemContainerNumberPage(index).navigate(mode, request.userAnswers, index, value))
        )
    }
}
