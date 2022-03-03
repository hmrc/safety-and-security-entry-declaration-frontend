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
import forms.goods.AddPackageFormProvider
import javax.inject.Inject
import models.{Index, LocalReferenceNumber, Mode}
import pages.goods.AddPackagePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.goods.PackageSummary
import views.html.goods.AddPackageView

class AddPackageController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: AddPackageFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AddPackageView
) extends FrontendBaseController
  with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val rows = PackageSummary.rows(request.userAnswers, itemIndex)

      Ok(view(form, mode, lrn, itemIndex, rows))
    }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => {
            val rows = PackageSummary.rows(request.userAnswers, itemIndex)

            BadRequest(view(formWithErrors, mode, lrn, itemIndex, rows))
          },
          value =>
            Redirect(
              AddPackagePage(itemIndex).navigate(mode, request.userAnswers, itemIndex, value)
            )
        )
    }
}
