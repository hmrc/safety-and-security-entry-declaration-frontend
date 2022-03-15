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
import forms.goods.LoadingPlaceFormProvider

import javax.inject.Inject
import models.{Index, LocalReferenceNumber, Mode}
import pages.goods.LoadingPlacePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.routedetails.AllPlacesOfLoadingQuery
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.RadioOptions
import views.html.goods.LoadingPlaceView

import scala.concurrent.{ExecutionContext, Future}

class LoadingPlaceController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: LoadingPlaceFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: LoadingPlaceView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
    with I18nSupport
    with AnswerExtractor {

  def onPageLoad(mode: Mode, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) {
      implicit request =>
        getAnswer(AllPlacesOfLoadingQuery) {
          placesOfLoading =>

            val form = formProvider(placesOfLoading.map(_.key))
            val radioOptions = RadioOptions(placesOfLoading.map(l => l.key.toString -> l.place).toMap)

            val preparedForm = request.userAnswers.get(LoadingPlacePage(itemIndex)) match {
              case None => form
              case Some(value) => form.fill(value)
            }

            Ok(view(preparedForm, mode, lrn, itemIndex, radioOptions))
        }
    }

  def onSubmit(mode: Mode, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async {
      implicit request =>
        getAnswerAsync(AllPlacesOfLoadingQuery) {
          placesOfLoading =>

            val form = formProvider(placesOfLoading.map(_.key))

            form
              .bindFromRequest()
              .fold(
                formWithErrors => {
                  val radioOptions = RadioOptions(placesOfLoading.map(l => l.key.toString -> l.place).toMap)
                  Future.successful(BadRequest(view(formWithErrors, mode, lrn, itemIndex, radioOptions)))
                },
                value =>
                  for {
                    updatedAnswers <- Future.fromTry(request.userAnswers.set(LoadingPlacePage(itemIndex), value))
                    _ <- sessionRepository.set(updatedAnswers)
                  } yield Redirect(LoadingPlacePage(itemIndex).navigate(mode, updatedAnswers))
              )
        }
    }
}