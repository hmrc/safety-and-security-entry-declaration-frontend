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
import models.ConsigneeIdentity.{GBEORI, NameAddress}
import models.{ConsigneeIdentity, Index, LocalReferenceNumber, UserAnswers}
import pages.{Breadcrumbs, DataPage}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case class ConsigneeIdentityPage(index: Index) extends DataPage[ConsigneeIdentity] {

  override def path: JsPath = JsPath \ "consignees" \ index.position \ toString

  override def toString: String = "identity"

  override def route(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Call =
    consigneeRoutes.ConsigneeIdentityController.onPageLoad(breadcrumbs, lrn, index)

  override protected def nextPageNormalMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    answers.get(this).map {
      case GBEORI => ConsigneeEORIPage(index)
      case NameAddress => ConsigneeNamePage(index)
    }.orRecover

  override protected def nextPageCheckMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    answers.get(this).map {
      case GBEORI =>
        answers.get(ConsigneeEORIPage(index))
          .map(_ => CheckConsigneePage(index))
          .getOrElse(ConsigneeEORIPage(index))

      case NameAddress =>
        answers.get(ConsigneeNamePage(index))
        .map (_ => CheckConsigneePage(index))
        .getOrElse(ConsigneeNamePage(index))
    }.orRecover

  override def cleanup(value: Option[ConsigneeIdentity], userAnswers: UserAnswers): Try[UserAnswers] = {
    value.map {
      case GBEORI =>
        userAnswers
          .remove(ConsigneeNamePage(index))
          .flatMap(_.remove(ConsigneeAddressPage(index)))

      case NameAddress =>
        userAnswers.remove(ConsigneeEORIPage(index))
    }.getOrElse(super.cleanup(value, userAnswers))
  }
}
