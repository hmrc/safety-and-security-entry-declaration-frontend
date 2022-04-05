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

import java.time.Instant

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import base.SpecBase

class TimeFormatsSpec
  extends SpecBase
  with ScalaCheckPropertyChecks
  with TimeFormats
  with XmlImplicits {

  private implicit val fmt = TimeFormats.HeaderFormat

  "The instant formats" - {
    "should work symmetrically (at relevant precision)" in {
      forAll(minutePrecisionInstants) { instant =>
        instant.toXmlString.parseXmlString must be(instant)
      }
    }
  }

  "The header (HEAHEA) datetime format" - {
    "should encode as a plain 12-digit string with minute precision" in {
      val input = Instant.parse("2020-01-31T01:02:33Z")
      val expected = "202001310102"
      val actual = input.toXmlString

      actual must be(expected)
    }
  }
}
