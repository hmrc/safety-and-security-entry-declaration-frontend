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
import forms.goods.AddMarkOrNumberFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.AddMarkOrNumberPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.goods.AddMarkOrNumberView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddMarkOrNumberController @Inject() (
  formProvider: AddMarkOrNumberFormProvider,
  cc: CommonControllerComponents,
  view: AddMarkOrNumberView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(
    waypoints: Waypoints,
    lrn: LocalReferenceNumber,
    itemIndex: Index,
    packageIndex: Index
  ): Action[AnyContent] =
    cc.authAndGetData(lrn) { implicit request =>

      val preparedForm =
        request.userAnswers.get(AddMarkOrNumberPage(itemIndex, packageIndex)) match {
          case None => form
          case Some(value) => form.fill(value)
        }

      Ok(view(preparedForm, waypoints, lrn, itemIndex, packageIndex))
    }

  def onSubmit(
    waypoints: Waypoints,
    lrn: LocalReferenceNumber,
    itemIndex: Index,
    packageIndex: Index
  ): Action[AnyContent] =
    cc.authAndGetData(lrn).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, itemIndex, packageIndex))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(
                request.userAnswers.set(AddMarkOrNumberPage(itemIndex, packageIndex), value)
              )
              _ <- cc.sessionRepository.set(updatedAnswers)
            } yield Redirect(
              AddMarkOrNumberPage(itemIndex, packageIndex).navigate(waypoints, updatedAnswers)
            )
        )
    }
}
