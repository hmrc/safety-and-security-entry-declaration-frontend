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

package pages.goods

import controllers.goods.routes
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{CheckAnswersPage, Page, Waypoint, Waypoints}
import play.api.mvc.Call

final case class CheckPackageItemPage(itemIndex: Index, packageIndex: Index) extends CheckAnswersPage {

  override val urlFragment: String = s"check-package-${itemIndex.position}-${packageIndex.position}"

  override def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    routes.CheckPackageItemController.onPageLoad(waypoints, lrn, itemIndex, packageIndex)

  override def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    AddPackagePage(itemIndex)
}

object CheckPackageItemPage {

  def waypointFromString(s: String): Option[Waypoint] = {

    val pattern = """check-package-(\d{1,3})-(\d{1,3})""".r.anchored

    s match {
      case pattern(itemIndexDisplay, packageIndexDisplay) =>
        Some(CheckPackageItemPage(Index(itemIndexDisplay.toInt - 1), Index(packageIndexDisplay.toInt - 1)).waypoint)

      case _ =>
        None
    }
  }
}
