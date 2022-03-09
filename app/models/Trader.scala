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

trait Trader extends WithId

object Trader {

  implicit val reads: Reads[Trader] =
    TraderWithEori.format.widen[Trader] orElse
      TraderWithoutEori.format.widen[Trader]

  implicit val writes: Writes[Trader] = Writes {
    case t: TraderWithEori    => Json.toJson(t)(TraderWithEori.format)
    case t: TraderWithoutEori => Json.toJson(t)(TraderWithoutEori.format)
  }
}

final case class TraderWithEori(id: Int, eori: String) extends Trader

object TraderWithEori {

  implicit val format: OFormat[TraderWithEori] = Json.format[TraderWithEori]
}

final case class TraderWithoutEori(id: Int, name: String, address: Address) extends Trader

object TraderWithoutEori {

  implicit val format: OFormat[TraderWithoutEori] = Json.format[TraderWithoutEori]
}
