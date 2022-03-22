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
import java.time.temporal.ChronoUnit

import base.SpecBase
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import models.completion.downstream.CustomsOffice
import models.{CustomsOffice => JourneyCustomsOffice}

class CustomsOfficeFormatsSpec
  extends SpecBase
  with CustomsOfficeFormats
  with ScalaCheckPropertyChecks
  with XmlImplicits {

  // The customs office writes datetime with minute precision
  private val instantGen = arbitrary[Instant](arbitraryRecentInstant) map {
    _.truncatedTo(ChronoUnit.MINUTES)
  }

  "The customs office format" - {
    "should work symmetrically" in {
      val sampleData = for {
        office <- arbitrary[JourneyCustomsOffice]
        datetime <- instantGen
      } yield CustomsOffice(office.code, datetime)

      forAll(sampleData) { c =>
        c.toXml.parseXml[CustomsOffice] must be(c)
      }
    }
  }
}
