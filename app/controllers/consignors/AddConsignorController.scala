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

package controllers.consignors

import controllers.actions._
import forms.consignors.AddConsignorFormProvider
import models.{LocalReferenceNumber, Mode}
import pages.consignors.AddConsignorPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.consignors.AddConsignorSummary
import views.html.consignors.AddConsignorView

import javax.inject.Inject

class AddConsignorController @Inject()(
                                        override val messagesApi: MessagesApi,
                                        identify: IdentifierAction,
                                        getData: DataRetrievalActionProvider,
                                        requireData: DataRequiredAction,
                                        formProvider: AddConsignorFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: AddConsignorView
) extends FrontendBaseController with I18nSupport {

  private val form = formProvider()

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber): Action[AnyContent] = (identify andThen getData(lrn) andThen requireData) {
    implicit request =>

      val consignors = AddConsignorSummary.rows(request.userAnswers)

      Ok(view(form, mode, lrn, consignors))
  }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber): Action[AnyContent] = (identify andThen getData(lrn) andThen requireData) {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {
          val consignors = AddConsignorSummary.rows(request.userAnswers)

          BadRequest(view(formWithErrors, mode, lrn, consignors))
        },
        value => Redirect(AddConsignorPage.navigate(mode, request.userAnswers, value))
      )
  }
}
