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

package serialisation.xml

import java.time.{Instant, ZoneId, ZoneOffset}
import java.time.format.DateTimeFormatter

/**
 * XML formats for time-related fields
 */
trait TimeFormats {
  implicit def instantFmt(implicit fmt: DateTimeFormatter): StringFormat[Instant] = {
    new StringFormat[Instant] {
      override def encode(instant: Instant): String = fmt.format(instant)
      override def decode(s: String): Instant = Instant.from(fmt.parse(s))
    }
  }
}

object TimeFormats extends TimeFormats {
  // Time format used in HEAHEA's datetime field
  val HeaderFormat = {
    DateTimeFormatter.ofPattern("yyyyMMddHHmm").withZone(ZoneId.from(ZoneOffset.UTC))
  }
}
