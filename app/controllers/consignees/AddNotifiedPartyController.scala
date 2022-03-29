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
import forms.consignees.AddNotifiedPartyFormProvider
import models.LocalReferenceNumber
import pages.Breadcrumbs
import pages.consignees.AddNotifiedPartyPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.consignees.AddNotifiedPartySummary
import views.html.consignees.AddNotifiedPartyView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddNotifiedPartyController @Inject()(
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: AddNotifiedPartyFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AddNotifiedPartyView
)(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  private val form = formProvider()

  def onPageLoad(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
      implicit request =>

        val notifiedParties = AddNotifiedPartySummary.rows(request.userAnswers, breadcrumbs)

        Ok(view(form, breadcrumbs, lrn, notifiedParties))
    }

  def onSubmit(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async {
      implicit request =>

        form.bindFromRequest().fold(
          formWithErrors => {
            val notifiedParties = AddNotifiedPartySummary.rows(request.userAnswers, breadcrumbs)

            Future.successful(BadRequest(view(formWithErrors, breadcrumbs, lrn, notifiedParties)))
          },
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddNotifiedPartyPage, value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(AddNotifiedPartyPage.navigate(breadcrumbs, updatedAnswers))
        )
    }
}
