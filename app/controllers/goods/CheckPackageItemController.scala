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

import com.google.inject.Inject
import config.IndexLimits.maxGoods
import controllers.actions.CommonControllerComponents
import models.{Index, LocalReferenceNumber}
import pages.Waypoints
import pages.goods.CheckPackageItemPage
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.goods._
import viewmodels.govuk.summarylist._
import views.html.goods.CheckPackageItemView

class CheckPackageItemController @Inject() (
  cc: CommonControllerComponents,
  view: CheckPackageItemView
) extends FrontendBaseController
  with I18nSupport {

  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(
    waypoints: Waypoints,
    lrn: LocalReferenceNumber,
    itemIndex: Index,
    packageIndex: Index
  ): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods)) { implicit request =>

      val thisPage = CheckPackageItemPage(itemIndex, packageIndex)

      val list = SummaryListViewModel(
        rows = Seq(
          KindOfPackageSummary.row(request.userAnswers, itemIndex, packageIndex, waypoints, thisPage),
          NumberOfPackagesSummary.row(request.userAnswers, itemIndex, packageIndex, waypoints, thisPage),
          NumberOfPiecesSummary.row(request.userAnswers, itemIndex, packageIndex, waypoints, thisPage),
          AddMarkOrNumberSummary.row(request.userAnswers, itemIndex, packageIndex, waypoints, thisPage),
          MarkOrNumberSummary.row(request.userAnswers, itemIndex, packageIndex, waypoints, thisPage)
        ).flatten
      )

      Ok(view(waypoints, list, lrn, itemIndex, packageIndex))
    }

  def onSubmit(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index, packageIndex: Index): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods)) {
      implicit request =>
        Redirect(CheckPackageItemPage(itemIndex, packageIndex).navigate(waypoints, request.userAnswers))
    }
}
