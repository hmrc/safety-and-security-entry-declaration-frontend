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

package controllers.transport

import config.IndexLimits.maxDocuments
import controllers.AnswerExtractor
import controllers.actions._
import forms.transport.AddOverallDocumentFormProvider
import models.LocalReferenceNumber
import pages.Waypoints
import pages.transport.AddOverallDocumentPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.transport.DeriveNumberOfOverallDocuments
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.transport.OverallDocumentSummary
import views.html.transport.AddOverallDocumentView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddOverallDocumentController @Inject()(
  formProvider: AddOverallDocumentFormProvider,
  cc: CommonControllerComponents,
  view: AddOverallDocumentView
)(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with AnswerExtractor {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] = cc.authAndGetData(lrn) {
    implicit request =>
      val documents = OverallDocumentSummary.rows(request.userAnswers, waypoints, AddOverallDocumentPage)

      Ok(view(form, waypoints, lrn, documents))
  }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    cc.authAndGetData(lrn).async {
      implicit request =>
        getAnswerAsync(DeriveNumberOfOverallDocuments) {
          numberOfOverallDocs =>

            form.bindFromRequest().fold(
              formWithErrors => {
                val documents = OverallDocumentSummary.rows(request.userAnswers, waypoints, AddOverallDocumentPage)
                Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, documents)))
              },
              value => {
                val protectedAnswer = if (numberOfOverallDocs >= maxDocuments) false else value

                for {
                  updatedAnswers <- Future.fromTry(request.userAnswers.set(AddOverallDocumentPage, protectedAnswer))
                  _ <- cc.sessionRepository.set(updatedAnswers)
                } yield Redirect(AddOverallDocumentPage.navigate(waypoints, updatedAnswers))
              }
            )
          }
    }
}
