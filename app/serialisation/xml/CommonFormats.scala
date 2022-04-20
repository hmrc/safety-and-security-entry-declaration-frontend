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

import models.{Country, GbEori, LocalReferenceNumber}

/**
 * XML formats for miscellaneous common or shared data types, e.g. enums, reference numbers, etc.
 */
trait CommonFormats {
  implicit val gbEoriFmt: StringFormat[GbEori] = new StringFormat[GbEori] {
    override def encode(eori: GbEori): String = s"GB${eori.value}"
    override def decode(s: String): GbEori = {
      if (s.toUpperCase.startsWith("GB")) {
        GbEori(s.drop(2))
      } else {
        throw new XmlDecodingException(s"Expected GB EORI did not start with GB: '$s'")
      }
    }
  }

  implicit val bigDecimalFmt: StringFormat[BigDecimal] = StringFormat.simple(
    _.toString, BigDecimal(_)
  )

  implicit val lrnFmt: StringFormat[LocalReferenceNumber] = {
    StringFormat.simple(_.value, LocalReferenceNumber.apply)
  }

  implicit val countryFmt: StringFormat[Country] = new StringFormat[Country] {
    override def encode(c: Country): String = c.code
    override def decode(s: String): Country = {
      Country.allCountries.find { _.code == s }.getOrElse {
        throw new XmlDecodingException(s"Bad country code: $s")
      }
    }
  }
}

object CommonFormats extends CommonFormats
