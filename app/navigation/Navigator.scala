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

package navigation

import javax.inject.{Inject, Singleton}

import play.api.mvc.Call
import controllers.routes
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case LocalReferenceNumberPage => ua => routes.LodgingPersonTypeController.onPageLoad(NormalMode, ua.lrn)
    case LodgingPersonTypePage => ua => routes.GrossWeightController.onPageLoad(NormalMode, ua.lrn)
    case GrossWeightPage => ua => ua.get(GrossWeightPage) match {
      case Some(GrossWeight.PerItem) => routes.TransportModeController.onPageLoad(NormalMode, ua.lrn)
      case Some(GrossWeight.Overall) => routes.TotalGrossWeightController.onPageLoad(NormalMode, ua.lrn)
      case _ => routes.JourneyRecoveryController.onPageLoad()
    }
    case TotalGrossWeightPage => ua => routes.TransportModeController.onPageLoad(NormalMode, ua.lrn)
    case TransportModePage => ua => routes.IdentifyCarrierController.onPageLoad(NormalMode, ua.lrn)
    case IdentifyCarrierPage => ua => routes.CarriersEORIController.onPageLoad(NormalMode, ua.lrn)
    case _                        => _ => routes.IndexController.onPageLoad
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    case _ => ua => routes.CheckYourAnswersController.onPageLoad(ua.lrn)
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
