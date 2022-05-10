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

import config.IndexLimits.{maxGoods, maxPackages}
import controllers.actions._
import forms.goods.NumberOfPackagesFormProvider
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.NumberOfPackagesPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.goods.NumberOfPackagesView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class NumberOfPackagesController @Inject() (
  formProvider: NumberOfPackagesFormProvider,
  cc: CommonControllerComponents,
  view: NumberOfPackagesView
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  private val form = formProvider()
  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(
    waypoints: Waypoints,
    lrn: LocalReferenceNumber,
    itemIndex: Index,
    packageIndex: Index
  ): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods) andThen cc.limitIndex(packageIndex, maxPackages)) {
      implicit request =>

        val preparedForm =
          request.userAnswers.get(NumberOfPackagesPage(itemIndex, packageIndex)) match {
            case None => form
            case Some(value) => form.fill(value)
          }

        Ok(view(preparedForm, waypoints, lrn, itemIndex, packageIndex))
      }

  def onSubmit(
    waypoints: Waypoints,
    lrn: LocalReferenceNumber,
    itemIndex: Index,
    packageIndex: Index
  ): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods) andThen cc.limitIndex(packageIndex, maxPackages)).async {
      implicit request =>

        form
          .bindFromRequest()
          .fold(
            formWithErrors => Future.successful(BadRequest(view(formWithErrors, waypoints, lrn, itemIndex, packageIndex))),
            value =>
              for {
                updatedAnswers <- Future.fromTry(
                  request.userAnswers.set(NumberOfPackagesPage(itemIndex, packageIndex), value)
                )
                _ <- cc.sessionRepository.set(updatedAnswers)
              } yield Redirect(
                NumberOfPackagesPage(itemIndex, packageIndex).navigate(waypoints, updatedAnswers)
              )
          )
      }
}
