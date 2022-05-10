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

import config.IndexLimits.maxGoods
import controllers.AnswerExtractor
import controllers.actions._
import forms.goods.NotifiedPartyFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.NotifiedPartyPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.consignees.AllNotifiedPartiesQuery
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.RadioOptions
import views.html.goods.NotifiedPartyView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class NotifiedPartyController @Inject() (
  formProvider: NotifiedPartyFormProvider,
  cc: CommonControllerComponents,
  view: NotifiedPartyView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
    with I18nSupport
    with AnswerExtractor {

  protected val controllerComponents: MessagesControllerComponents = cc
  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods)) {
      implicit request =>
        getAnswer(AllNotifiedPartiesQuery) {
          notifiedParties =>

            val form = formProvider(notifiedParties.map(_.key))
            val radioOptions = RadioOptions(notifiedParties.map(n => n.key.toString -> n.displayName).toMap)

            val preparedForm = request.userAnswers.get(NotifiedPartyPage(itemIndex)) match {
              case None => form
              case Some(value) => form.fill(value)
            }

            Ok(view(preparedForm, waypoints, lrn, itemIndex, radioOptions))
        }
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods)).async {
      implicit request =>
        getAnswerAsync(AllNotifiedPartiesQuery) {
          notifiedParties =>

            val form = formProvider(notifiedParties.map(_.key))

            form
            .bindFromRequest()
            .fold(
              formWithErrors => {
                val radioOptions = RadioOptions(notifiedParties.map(n => n.key.toString -> n.displayName).toMap)
                Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, itemIndex, radioOptions)))
              },
              value =>
                for {
                  updatedAnswers <- Future.fromTry(request.userAnswers.set(NotifiedPartyPage(itemIndex), value))
                  _ <- cc.sessionRepository.set(updatedAnswers)
                } yield Redirect(NotifiedPartyPage(itemIndex).navigate(waypoints, updatedAnswers))
            )
        }
    }
}