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

import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import base.SpecBase
import models.{Country, LocalReferenceNumber}

class CommonFormatsSpec
  extends SpecBase
  with ScalaCheckPropertyChecks
  with CommonFormats
  with XmlImplicits {

  "The local reference number formats" - {
    "should work symmetrically" in {
      forAll(arbitrary[LocalReferenceNumber]) { lrn =>
        lrn.toXmlString.parseXmlString[LocalReferenceNumber] must be(lrn)
      }
    }
  }

  "The country format" - {
    "should work symmetrically" in {
      forAll(arbitrary[Country]) { c =>
        c.toXmlString.parseXmlString[Country] must be(c)
      }
    }
  }

  "The big decimal format" - {
    "should work symmetrically" in {
      forAll(arbitrary[BigDecimal]) { bd =>
        bd.toXmlString.parseXmlString[BigDecimal] must be(bd)
      }
    }
  }
}
