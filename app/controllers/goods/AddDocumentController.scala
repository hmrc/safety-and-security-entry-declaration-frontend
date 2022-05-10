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

import config.IndexLimits.{maxDocuments, maxGoods}
import controllers.AnswerExtractor
import controllers.actions._
import forms.goods.AddDocumentFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.AddDocumentPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.goods.DeriveNumberOfDocuments
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
    with I18nSupport
    with AnswerExtractor {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods)) { implicit request =>

      val documents = DocumentSummary.rows(request.userAnswers, itemIndex, waypoints, AddDocumentPage(itemIndex))

      Ok(view(form, waypoints, lrn, itemIndex, documents))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods)).async { implicit request =>
      getAnswerAsync(DeriveNumberOfDocuments(itemIndex)) {
        numberOfDocuments =>

          form
            .bindFromRequest()
            .fold(
              formWithErrors => {
                val documents = DocumentSummary.rows(request.userAnswers, itemIndex, waypoints, AddDocumentPage(itemIndex))

                Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, itemIndex, documents)))
              },
              value => {
                val protectedAnswer = if (numberOfDocuments >= maxDocuments) false else value

                for {
                  updatedAnswers <- Future.fromTry(request.userAnswers.set(AddDocumentPage(itemIndex), protectedAnswer))
                  _ <- cc.sessionRepository.set(updatedAnswers)
                } yield Redirect(AddDocumentPage(itemIndex).navigate(waypoints, updatedAnswers))
              }
            )
        }
      }
}