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

import base.SpecBase
import models.LocalReferenceNumber
import models.completion.downstream.{AmendmentTimePlace, Header, SubmissionTimePlace, TransportDetails}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class HeaderFormatsSpec
  extends SpecBase
  with ScalaCheckPropertyChecks
  with HeaderFormats
  with XmlImplicits {

  private val headerGen: Gen[Header] = {
    for {
      lrn <- arbitrary[LocalReferenceNumber]
      transportDetails <- arbitrary[TransportDetails]
      itemCount <- Gen.choose(1, 3)
      packageCount <- Gen.choose(1, 3)
      grossMass <- Gen.option(Gen.choose(BigDecimal(0.001), BigDecimal(99999999.999)))
      conveyanceReferenceNumber <- Gen.alphaNumStr
      timePlace <- arbitrary[SubmissionTimePlace]
    } yield {
      Header(
        lrn,
        transportDetails,
        itemCount,
        packageCount,
        grossMass,
        conveyanceReferenceNumber,
        timePlace
      )
    }
  }

  "The header format" - {
    "should work symmetrically" - {
      "for any header with a SubmissionTimePlace" in {
        forAll(headerGen) { h =>
          h.toXml.parseXml[Header] must be(h)
        }
      }

      "for any header with a AmendmentTimePlace" in {
        val header = for {
          header <- headerGen
          atp <- arbitrary[AmendmentTimePlace]
        } yield header.copy(timePlace = atp)

        forAll(header) { h =>
          h.toXml.parseXml[Header] must be(h)
        }
      }

      "when gross mass" - {
        "is specified" in {
          val gen = for {
            header <- headerGen
            mass <- Gen.choose(1, 100000)
          } yield {
            header.copy(grossMass = Some(mass))
          }

          forAll(gen) { h =>
            h.toXml.parseXml[Header] must be(h)
          }
        }

        "is missing" in {
          val gen = headerGen map { _.copy(grossMass = None) }

          forAll(gen) { h =>
            h.toXml.parseXml[Header] must be(h)
          }
        }
      }
    }
  }
}
