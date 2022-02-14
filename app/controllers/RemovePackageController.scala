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
import forms.RemovePackageFormProvider

import javax.inject.Inject
import models.{Index, LocalReferenceNumber, Mode}
import pages.RemovePackagePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.PackageQuery
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.RemovePackageView

import scala.concurrent.{ExecutionContext, Future}

class RemovePackageController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalActionProvider,
                                         requireData: DataRequiredAction,
                                         formProvider: RemovePackageFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: RemovePackageView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber, itemIndex: Index, packageIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
      implicit request =>

        Ok(view(form, mode, lrn, itemIndex, packageIndex))
    }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber, itemIndex: Index, packageIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async {
      implicit request =>

        form.bindFromRequest().fold(
          formWithErrors =>
            Future.successful(BadRequest(view(formWithErrors, mode, lrn, itemIndex, packageIndex))),

          value =>
            if (value) {
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.remove(PackageQuery(itemIndex, packageIndex)))
                _              <- sessionRepository.set(updatedAnswers)
              } yield Redirect(RemovePackagePage(itemIndex, packageIndex).navigate(mode, updatedAnswers))
            } else {
              Future.successful(Redirect(RemovePackagePage(itemIndex, packageIndex).navigate(mode, request.userAnswers)))
            }
        )
    }
}
