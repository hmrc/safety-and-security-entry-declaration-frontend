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

package pages.consignors

import controllers.consignors.{routes => consignorRoutes}
import controllers.routes
import models.{CheckMode, Index, Mode, NormalMode, UserAnswers}
import pages.Page
import play.api.mvc.Call
import queries.consignors.DeriveNumberOfConsignors

case object AddConsignorPage extends Page {

  def navigate(mode: Mode, answers: UserAnswers, addAnother: Boolean): Call =
    if (addAnother) {
      answers.get(DeriveNumberOfConsignors) match {
        case Some(size) =>
          consignorRoutes.ConsignorIdentityController.onPageLoad(mode, answers.lrn, Index(size))
        case None =>
          routes.JourneyRecoveryController.onPageLoad()
      }
    } else {
      mode match {
        case NormalMode =>
          routes.TaskListController.onPageLoad(answers.lrn)
        case CheckMode =>
          routes.CheckYourAnswersController.onPageLoad(answers.lrn)
      }
    }
}
