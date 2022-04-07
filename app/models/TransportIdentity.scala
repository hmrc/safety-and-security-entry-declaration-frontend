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

import play.api.libs.json._

sealed trait TransportIdentity

object TransportIdentity {
  case class AirIdentity(flightNumber: String) extends TransportIdentity

  object AirIdentity {
    implicit val format = Json.format[AirIdentity]
  }

  case class MaritimeIdentity(imo: String, conveyanceRefNum: String) extends TransportIdentity

  object MaritimeIdentity {
    implicit val format = Json.format[MaritimeIdentity]
  }

  case class RailIdentity(wagonNumber: String) extends TransportIdentity

  object RailIdentity {
    implicit val format = Json.format[RailIdentity]
  }

  case class RoadIdentity(
    vehicleRegistrationNumber: String,
    trailerNumber: String,
    ferryCompany: Option[String]
  ) extends TransportIdentity

  object RoadIdentity {
    implicit val format = Json.format[RoadIdentity]
  }

  case class RoroAccompaniedIdentity(
    vehicleRegistrationNumber: String,
    trailerNumber: String,
    ferryCompany: Option[String]
  ) extends TransportIdentity

  object RoroAccompaniedIdentity {
    implicit val format = Json.format[RoroAccompaniedIdentity]
  }

  case class RoroUnaccompaniedIdentity(
    trailerNumber: String,
    imo: String,
    ferryCompany: Option[String]
  ) extends TransportIdentity

  object RoroUnaccompaniedIdentity {
    implicit val format = Json.format[RoroUnaccompaniedIdentity]
  }
}
