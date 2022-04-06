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
import models.{LocalReferenceNumber, UserAnswers}
import pages.{Waypoints, CheckAnswersPage, Page, TaskListPage}
import play.api.mvc.Call

object CheckConsigneesAndNotifiedPartiesPage extends CheckAnswersPage {
  override val urlFragment: String = "check-consignees-notified-parties"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(waypoints, lrn)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    TaskListPage
}
