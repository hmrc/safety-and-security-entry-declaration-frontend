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
import controllers.actions._
import forms.goods.RemoveGoodsFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.RemoveGoodsPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.goods.GoodItemQuery
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.goods.RemoveGoodsView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RemoveGoodsController @Inject() (
  formProvider: RemoveGoodsFormProvider,
  cc: CommonControllerComponents,
  view: RemoveGoodsView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(
    waypoints: Waypoints,
    lrn: LocalReferenceNumber,
    itemIndex: Index
  ): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods)) { implicit request =>

      val preparedForm =
        request.userAnswers.get(RemoveGoodsPage(itemIndex)) match {
          case None => form
          case Some(value) => form.fill(value)
        }

      Ok(view(preparedForm, waypoints, lrn, itemIndex))
    }

  def onSubmit(
    waypoints: Waypoints,
    lrn: LocalReferenceNumber,
    itemIndex: Index
  ): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods)).async { implicit request =>

      form
        .bindFromRequest()
        .fold(
          formWithErrors =>
            Future
              .successful(BadRequest(view(formWithErrors, waypoints, lrn, itemIndex))),
          value =>
            if (value) {
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.remove(GoodItemQuery(itemIndex)))
                _ <- cc.sessionRepository.set(updatedAnswers)
              } yield Redirect(RemoveGoodsPage(itemIndex).navigate(waypoints, updatedAnswers))
            } else {
              Future.successful(
                Redirect(RemoveGoodsPage(itemIndex).navigate(waypoints, request.userAnswers))
              )
            }
        )
    }
}
