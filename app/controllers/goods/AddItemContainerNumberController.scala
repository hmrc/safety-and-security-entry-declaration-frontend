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
import forms.goods.AddItemContainerNumberFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.AddItemContainerNumberPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.goods.ItemContainerNumberSummary
import views.html.goods.AddItemContainerNumberView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddItemContainerNumberController @Inject()(
  formProvider: AddItemContainerNumberFormProvider,
  cc: CommonControllerComponents,
  view: AddItemContainerNumberView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn) { implicit request =>

      val containers = ItemContainerNumberSummary.rows(request.userAnswers, index, waypoints, AddItemContainerNumberPage(index))

      Ok(view(form, waypoints, lrn, index, containers))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => {
            val containers = ItemContainerNumberSummary.rows(request.userAnswers, index, waypoints, AddItemContainerNumberPage(index))

            Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, index, containers)))
          },
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddItemContainerNumberPage(index), value))
              _ <- cc.sessionRepository.set(updatedAnswers)
            } yield Redirect(AddItemContainerNumberPage(index).navigate(waypoints, updatedAnswers))
        )
    }
}
