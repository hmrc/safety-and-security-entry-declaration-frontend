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
import forms.AddCountryEnRouteFormProvider
import models.requests.DataRequest
import models.{LocalReferenceNumber, Mode}
import pages.AddCountryEnRoutePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import queries.DeriveNumberOfCountriesEnRoute
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.CountryEnRouteSummary
import views.html.AddCountryEnRouteView

import javax.inject.Inject

class AddCountryEnRouteController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalActionProvider,
                                         requireData: DataRequiredAction,
                                         formProvider: AddCountryEnRouteFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: AddCountryEnRouteView
                                 ) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber): Action[AnyContent] = (identify andThen getData(lrn) andThen requireData) {
    implicit request =>

      val countries = CountryEnRouteSummary.rows(request.userAnswers)

      Ok(view(form, mode, lrn, countries))
  }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber): Action[AnyContent] = (identify andThen getData(lrn) andThen requireData) {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {
          val countries = CountryEnRouteSummary.rows(request.userAnswers)

          BadRequest(view(formWithErrors, mode, lrn, countries))
        },

        value =>
          Redirect(AddCountryEnRoutePage.navigate(mode, request.userAnswers, value))
      )
  }
}
