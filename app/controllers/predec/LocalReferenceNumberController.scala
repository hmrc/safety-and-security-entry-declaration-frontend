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

package controllers.predec

import controllers.actions._
import forms.predec.LocalReferenceNumberFormProvider
import models.UserAnswers
import pages.EmptyWaypoints
import pages.predec.LocalReferenceNumberPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.predec.LocalReferenceNumberView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class LocalReferenceNumberController @Inject() (
  formProvider: LocalReferenceNumberFormProvider,
  cc: CommonControllerComponents,
  view: LocalReferenceNumberView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(): Action[AnyContent] =
    cc.identify { implicit request =>
      Ok(view(form))
    }

  def onSubmit(): Action[AnyContent] =
    cc.identify.async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors))),
          value => {
            val updatedAnswers = UserAnswers(request.eori, value)

            cc.sessionRepository.set(updatedAnswers).map { _ =>
              Redirect(LocalReferenceNumberPage.navigate(EmptyWaypoints, updatedAnswers))
            }
          }
        )
    }
}
