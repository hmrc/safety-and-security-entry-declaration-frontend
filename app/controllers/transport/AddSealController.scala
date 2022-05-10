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

import config.IndexLimits.maxSeals
import controllers.AnswerExtractor
import controllers.actions._
import forms.transport.AddSealFormProvider
import models.LocalReferenceNumber
import pages.Waypoints
import pages.transport.AddSealPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.transport.DeriveNumberOfSeals
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.transport.SealSummary
import views.html.transport.AddSealView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddSealController @Inject()(
  formProvider: AddSealFormProvider,
  cc: CommonControllerComponents,
  view: AddSealView
)(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with AnswerExtractor {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    cc.authAndGetData(lrn) {
      implicit request =>
        val documents = SealSummary.rows(request.userAnswers, waypoints, AddSealPage)

        Ok(view(form, waypoints, lrn, documents))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    cc.authAndGetData(lrn).async {
      implicit request =>
        getAnswerAsync(DeriveNumberOfSeals) {
          numberOfSeals =>

            form.bindFromRequest().fold(
              formWithErrors => {
                val documents = SealSummary.rows(request.userAnswers, waypoints, AddSealPage)
                Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, documents)))
              },
              value => {
                val protectedAnswer = if (numberOfSeals >= maxSeals) false else value

                for {
                  updatedAnswers <- Future.fromTry(request.userAnswers.set(AddSealPage, protectedAnswer))
                  _ <- cc.sessionRepository.set(updatedAnswers)
                } yield Redirect(AddSealPage.navigate(waypoints, updatedAnswers))
              }
            )
          }
    }
}
