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
import forms.goods.AddDocumentFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.AddDocumentPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.goods.DocumentSummary
import views.html.goods.AddDocumentView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddDocumentController @Inject() (
  formProvider: AddDocumentFormProvider,
  cc: CommonControllerComponents,
  view: AddDocumentView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn) { implicit request =>

      val documents = DocumentSummary.rows(request.userAnswers, index, waypoints, AddDocumentPage(index))

      Ok(view(form, waypoints, lrn, index, documents))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => {
            val documents = DocumentSummary.rows(request.userAnswers, index, waypoints, AddDocumentPage(index))

            Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, index, documents)))
          },
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddDocumentPage(index), value))
              _ <- cc.sessionRepository.set(updatedAnswers)
            } yield Redirect(AddDocumentPage(index).navigate(waypoints, updatedAnswers))
        )
    }
}
