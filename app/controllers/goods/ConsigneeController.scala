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
import forms.goods.ConsigneeFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.ConsigneePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.consignees.AllConsigneesQuery
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.RadioOptions
import views.html.goods.ConsigneeView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ConsigneeController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: ConsigneeFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: ConsigneeView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
    with I18nSupport
    with AnswerExtractor {

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
      implicit request =>
        getAnswer(AllConsigneesQuery) {
          consignees =>

            val form = formProvider(consignees.map(_.key))
            val radioOptions = RadioOptions(consignees.map(c => c.key.toString -> c.displayName).toMap)

            val preparedForm = request.userAnswers.get(ConsigneePage(itemIndex)) match {
              case None => form
              case Some(value) => form.fill(value)
            }

            Ok(view(preparedForm, waypoints, lrn, itemIndex, radioOptions))
        }
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async {
      implicit request =>
        getAnswerAsync(AllConsigneesQuery) {
          consignees =>

            val form = formProvider(consignees.map(_.key))

            form
              .bindFromRequest()
              .fold(
                formWithErrors => {
                  val radioOptions = RadioOptions(consignees.map(c => c.key.toString -> c.displayName).toMap)
                  Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, itemIndex, radioOptions)))
                },
                value =>
                  for {
                    updatedAnswers <- Future.fromTry(request.userAnswers.set(ConsigneePage(itemIndex), value))
                    _ <- sessionRepository.set(updatedAnswers)
                  } yield Redirect(ConsigneePage(itemIndex).navigate(waypoints, updatedAnswers))
              )
          }
    }
}