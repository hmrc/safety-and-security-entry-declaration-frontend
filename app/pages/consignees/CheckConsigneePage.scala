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
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{Waypoint, Waypoints, CheckAnswersPage, Page}
import play.api.mvc.Call

case class CheckConsigneePage(index: Index) extends CheckAnswersPage {

  override val urlFragment: String = s"check-consignee-${index.display}"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.CheckConsigneeController.onPageLoad(waypoints, lrn, index)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    AddConsigneePage
}

object CheckConsigneePage {

  def waypointFromString(s: String): Option[Waypoint] = {

    val pattern = """check-consignee-(\d{1,3})""".r.anchored

    s match {
      case pattern(indexDisplay) =>
        Some(CheckConsigneePage(Index(indexDisplay.toInt - 1)).waypoint)

      case _ =>
        None
    }
  }
}
