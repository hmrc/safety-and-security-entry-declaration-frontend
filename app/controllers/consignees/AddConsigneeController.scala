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
import forms.consignees.AddConsigneeFormProvider

import javax.inject.Inject
import models.{LocalReferenceNumber, Mode}
import pages.Breadcrumbs
import pages.consignees.AddConsigneePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.consignees.AddConsigneeSummary
import views.html.consignees.AddConsigneeView

import scala.concurrent.{ExecutionContext, Future}

class AddConsigneeController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: AddConsigneeFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AddConsigneeView
)(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  private val form = formProvider()

  def onPageLoad(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
      implicit request =>

        val consignees = AddConsigneeSummary.rows(request.userAnswers, breadcrumbs)

        Ok(view(form, breadcrumbs, lrn, consignees))
    }

  def onSubmit(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async {
      implicit request =>

        form.bindFromRequest().fold(
          formWithErrors => {
            val consignees = AddConsigneeSummary.rows(request.userAnswers, breadcrumbs)

            Future.successful(BadRequest(view(formWithErrors, breadcrumbs, lrn, consignees)))
          },
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddConsigneePage, value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(AddConsigneePage.navigate(breadcrumbs, updatedAnswers))
        )
  }
}
