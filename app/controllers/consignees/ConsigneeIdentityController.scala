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

package controllers.consignees

import controllers.ByKeyExtractor
import controllers.actions._
import forms.consignees.ConsigneeIdentityFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.consignees.ConsigneeIdentityPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.consignees.{AllConsigneesQuery, ConsigneeKeyQuery}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.consignees.ConsigneeIdentityView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ConsigneeIdentityController @Inject() (
  formProvider: ConsigneeIdentityFormProvider,
  cc: CommonControllerComponents,
  view: ConsigneeIdentityView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport
  with ByKeyExtractor {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn) { implicit request =>

      val preparedForm = request.userAnswers.get(ConsigneeIdentityPage(index)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, waypoints, lrn, index))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn).async {
      implicit request =>
        getItemKey(index, AllConsigneesQuery) {
          consigneeKey =>

            form
              .bindFromRequest()
              .fold(
                formWithErrors => Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, index))),
                value =>
                  for {
                    answers <- Future.fromTry(request.userAnswers.set(ConsigneeIdentityPage(index), value))
                    updatedAnswers <- Future.fromTry(answers.set(ConsigneeKeyQuery(index), consigneeKey))
                    _ <- cc.sessionRepository.set(updatedAnswers)
                  } yield Redirect(ConsigneeIdentityPage(index).navigate(waypoints, updatedAnswers))
              )
        }
    }
}
