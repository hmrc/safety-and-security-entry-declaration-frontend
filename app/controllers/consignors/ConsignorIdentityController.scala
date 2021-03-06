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

package controllers.consignors

import controllers.ByKeyExtractor
import controllers.actions._
import forms.consignors.ConsignorIdentityFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.consignors.ConsignorIdentityPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.consignors.{AllConsignorsQuery, ConsignorKeyQuery}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.consignors.ConsignorIdentityView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ConsignorIdentityController @Inject() (
  formProvider: ConsignorIdentityFormProvider,
  cc: CommonControllerComponents,
  view: ConsignorIdentityView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport
  with ByKeyExtractor {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn) { implicit request =>

      val preparedForm = request.userAnswers.get(ConsignorIdentityPage(index)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, waypoints, lrn, index))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, index: Index): Action[AnyContent] =
    cc.authAndGetData(lrn).async {
      implicit request =>
        getItemKey(index, AllConsignorsQuery) {
          consignorKey =>

            form
              .bindFromRequest()
              .fold(
                formWithErrors => Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, index))),
                value =>
                  for {
                    answers <- Future.fromTry(request.userAnswers.set(ConsignorIdentityPage(index), value))
                    updatedAnswers <- Future.fromTry(answers.set(ConsignorKeyQuery(index), consignorKey))
                    _ <- cc.sessionRepository.set(updatedAnswers)
                  } yield Redirect(ConsignorIdentityPage(index).navigate(waypoints, updatedAnswers))
              )
        }
    }
}
