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

import controllers.consignees.{routes => consigneeRoutes}
import models.{Index, LocalReferenceNumber, UserAnswers}
import pages.{Breadcrumbs, DataPage}
import play.api.libs.json.JsPath
import play.api.mvc.Call
import queries.consignees.{DeriveNumberOfConsignees, DeriveNumberOfNotifiedParties}

case object AnyConsigneesKnownPage extends DataPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "anyConsigneesKnown"

  override def route(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Call =
    consigneeRoutes.AnyConsigneesKnownController.onPageLoad(breadcrumbs, lrn)

  override protected def nextPageNormalMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    answers.get(AnyConsigneesKnownPage).map {
      case true => ConsigneeIdentityPage(Index(0))
      case false => NotifiedPartyIdentityPage(Index(0))
    }.orRecover

  override protected def nextPageCheckMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    answers.get(this).map {
      case true =>
        answers.get(DeriveNumberOfConsignees).map {
          case n if n > 0 => breadcrumbs.current.orRecover
          case _ => ConsigneeIdentityPage(Index(0))
        }.getOrElse(ConsigneeIdentityPage(Index(0)))

      case false =>
        answers.get(DeriveNumberOfNotifiedParties).map {
          case n if n > 0 => breadcrumbs.current.orRecover
          case _ => NotifiedPartyIdentityPage(Index(0))
        }.getOrElse(NotifiedPartyIdentityPage(Index(0)))
    }.orRecover
}
