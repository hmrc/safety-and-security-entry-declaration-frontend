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

/**
 * Represents a GB EORI number
 *
 * A GB EORI is written as "GB" followed by 12 or 15 numeric characters. Since this models
 * a GB EORI specifically, we only record the numeric characters.
 */
class GbEori(val value: String) extends AnyVal

object GbEori {
  implicit val reads: Reads[GbEori] = Reads.StringReads.map { new GbEori(_) }
  implicit val writes: Writes[GbEori] = Writes.StringWrites.contramap { _.value }
}
