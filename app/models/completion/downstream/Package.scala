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

package models.completion.downstream

import play.api.libs.json._

import models.KindOfPackage

/**
 * Represents each package entry in the XML payload, i.e. "PACGS2" in the goods items
 */
case class Package(
  kindPackage: KindOfPackage,
  numPackages: Option[Int],
  numPieces: Option[Int],
  mark: Option[String]
)

object Package {
  implicit val format: OFormat[Package] = Json.format[Package]
}
