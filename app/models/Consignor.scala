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

package models

import play.api.libs.json.{Json, OFormat, Reads, Writes}

trait Consignor

object Consignor {

  implicit val reads: Reads[Consignor] =
    ConsignorWithEori.format.widen[Consignor] orElse
      ConsignorWithoutEori.format.widen[Consignor]

  implicit val writes: Writes[Consignor] = Writes {
    case t: ConsignorWithEori    => Json.toJson(t)(ConsignorWithEori.format)
    case t: ConsignorWithoutEori => Json.toJson(t)(ConsignorWithoutEori.format)
  }
}

final case class ConsignorWithEori(consignorEORI: String) extends Consignor

object ConsignorWithEori {

  implicit val format: OFormat[ConsignorWithEori] = Json.format[ConsignorWithEori]
}

final case class ConsignorWithoutEori(consignorName: String, consignorAddress: Address) extends Consignor

object ConsignorWithoutEori {

  implicit val format: OFormat[ConsignorWithoutEori] = Json.format[ConsignorWithoutEori]
}
