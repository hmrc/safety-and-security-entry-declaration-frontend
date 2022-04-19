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

import controllers.AnswerExtractor
import controllers.actions._
import forms.goods.ConsignorFormProvider
import models.{Index, LocalReferenceNumber, Mode}
import pages.Waypoints
import pages.goods.ConsignorPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.consignors.AllConsignorsQuery
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.RadioOptions
import views.html.goods.ConsignorView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ConsignorController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: ConsignorFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: ConsignorView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport
  with AnswerExtractor {

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
      implicit request =>
        getAnswer(AllConsignorsQuery) {
          consignors =>

            val form = formProvider(consignors.map(_.key))
            val radioOptions = RadioOptions(consignors.map(c => c.key.toString -> c.displayName).toMap)

            val preparedForm = request.userAnswers.get(ConsignorPage(itemIndex)) match {
              case None => form
              case Some(value) => form.fill(value)
            }

            Ok(view(preparedForm, waypoints, lrn, itemIndex, radioOptions))
          }
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async {
      implicit request =>
        getAnswerAsync(AllConsignorsQuery) {
          consignors =>

            val form = formProvider(consignors.map(_.key))

            form
              .bindFromRequest()
              .fold(
                formWithErrors => {
                  val radioOptions = RadioOptions(consignors.map(c => c.key.toString -> c.displayName).toMap)
                  Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, itemIndex, radioOptions)))
                },
                value =>
                  for {
                    updatedAnswers <- Future.fromTry(request.userAnswers.set(ConsignorPage(itemIndex), value))
                    _ <- sessionRepository.set(updatedAnswers)
                  } yield Redirect(ConsignorPage(itemIndex).navigate(waypoints, updatedAnswers))
              )
          }
    }
}
