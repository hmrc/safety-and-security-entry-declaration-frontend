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
import pages.{Breadcrumb, Breadcrumbs, CheckAnswersBreadcrumbPage, DataPage}
import play.api.libs.json.JsPath
import play.api.mvc.Call

final case class CheckNotifiedPartyPage(index: Index) extends CheckAnswersBreadcrumbPage {

  override val urlFragment: String = s"check-notified-party-${index.display}"

  override def path: JsPath = JsPath

  override def route(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Call =
    consigneesRoutes.CheckNotifiedPartyController.onPageLoad(breadcrumbs, lrn, index)

  override def nextPageNormalMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    AddNotifiedPartyPage
}

object CheckNotifiedPartyPage {

  def breadcrumbFromString(s: String): Option[Breadcrumb] = {

    val pattern = """check-notified-party-(\d{1,3})""".r.anchored

    s match {
      case pattern(indexDisplay) =>
        Some(CheckNotifiedPartyPage(Index(indexDisplay.toInt - 1)).breadcrumb)

      case _ =>
        None
    }
  }
}
