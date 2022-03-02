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

package pages.consignees

import controllers.consignees.{routes => consigneesRoutes}
import controllers.routes
import models.{CheckMode, Index, Mode, NormalMode, UserAnswers}
import pages.Page
import play.api.mvc.Call
import queries.consignees.DeriveNumberOfConsignees

case object AddConsigneePage extends Page {

  def navigate(mode: Mode, answers: UserAnswers, addAnother: Boolean): Call =
    if (addAnother) {
      answers.get(DeriveNumberOfConsignees) match {
        case Some(size) =>
          consigneesRoutes.ConsigneeIdentityController.onPageLoad(mode, answers.lrn, Index(size))
        case None =>
          routes.JourneyRecoveryController.onPageLoad()
      }
    } else {
      mode match {
        case NormalMode =>
          consigneesRoutes.AddAnyNotifiedPartiesController.onPageLoad(NormalMode, answers.lrn)
        case CheckMode =>
          routes.CheckYourAnswersController.onPageLoad(answers.lrn)
      }
    }
}
