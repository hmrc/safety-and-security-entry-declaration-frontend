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
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.AddPackagePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.goods.PackageSummary
import views.html.goods.AddPackageView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddPackageController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: AddPackageFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AddPackageView
)(implicit ec: ExecutionContext) extends FrontendBaseController
  with I18nSupport {

  val form = formProvider()

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val rows = PackageSummary.rows(request.userAnswers, itemIndex, waypoints, AddPackagePage(itemIndex))

      Ok(view(form, waypoints, lrn, itemIndex, rows))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => {
            val rows = PackageSummary.rows(request.userAnswers, itemIndex, waypoints, AddPackagePage(itemIndex))

            Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, itemIndex, rows)))
          },
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddPackagePage(itemIndex), value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(AddPackagePage(itemIndex).navigate(waypoints, updatedAnswers))
        )
    }
}
