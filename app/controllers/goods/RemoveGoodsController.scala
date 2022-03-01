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
import forms.goods.RemoveGoodsFormProvider
import models.{Index, LocalReferenceNumber, Mode}
import pages.goods.RemoveGoodsPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.GoodItemQuery
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.goods.RemoveGoodsView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RemoveGoodsController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: RemoveGoodsFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: RemoveGoodsView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  val form = formProvider()

  def onPageLoad(
    mode: Mode,
    lrn: LocalReferenceNumber,
    goodItemIndex: Index
  ): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val preparedForm =
        request.userAnswers.get(RemoveGoodsPage(goodItemIndex)) match {
          case None => form
          case Some(value) => form.fill(value)
        }

      Ok(view(preparedForm, mode, lrn, goodItemIndex))
    }

  def onSubmit(
    mode: Mode,
    lrn: LocalReferenceNumber,
    goodItemIndex: Index
  ): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors =>
            Future
              .successful(BadRequest(view(formWithErrors, mode, lrn, goodItemIndex))),
          value =>
            if (value) {
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.remove(GoodItemQuery(goodItemIndex)))
                _ <- sessionRepository.set(updatedAnswers)
              } yield Redirect(RemoveGoodsPage(goodItemIndex).navigate(mode, updatedAnswers))
            } else {
              Future.successful(
                Redirect(RemoveGoodsPage(goodItemIndex).navigate(mode, request.userAnswers))
              )
            }
        )
    }
}
