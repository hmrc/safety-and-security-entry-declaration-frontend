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
import forms.goods.AddGoodsFormProvider
import models.LocalReferenceNumber
import pages.Waypoints
import pages.goods.AddGoodsPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.goods.GoodsSummary
import views.html.goods.AddGoodsView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddGoodsController @Inject() (
  override val messagesApi: MessagesApi,
  sessionRepository: SessionRepository,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  formProvider: AddGoodsFormProvider,
  val controllerComponents: MessagesControllerComponents,
  view: AddGoodsView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  val form = formProvider()

  def onPageLoad(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData) { implicit request =>

      val goods = GoodsSummary.rows(request.userAnswers, waypoints, AddGoodsPage)

      Ok(view(form, waypoints, lrn, goods))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber): Action[AnyContent] =
    (identify andThen getData(lrn) andThen requireData).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors => {
            val goods = GoodsSummary.rows(request.userAnswers, waypoints, AddGoodsPage)

            Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, goods)))
          },
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(AddGoodsPage, value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(AddGoodsPage.navigate(waypoints, updatedAnswers))
        )
    }
}
