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
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import base.SpecBase
import models.{Country, PaymentMethod, TransportMode}
import models.completion.downstream.TransportDetails

class TransportFormatsSpec
  extends SpecBase
  with ScalaCheckPropertyChecks
  with TransportFormats
  with XmlImplicits {

  private val transportDetailsGen: Gen[TransportDetails] = {
    for {
      mode <- arbitrary[TransportMode]
      identifier <- Gen.alphaNumStr
      nationality <- Gen.option(arbitrary[Country])
      paymentMethod <- Gen.option(arbitrary[PaymentMethod])
    } yield TransportDetails(mode, identifier, nationality, paymentMethod)
  }

  "The transport mode format" - {
    "should work symmetrically" in {
      forAll(arbitrary[TransportMode]) { tm =>
        tm.toXmlString.parseXmlString[TransportMode] must be(tm)
      }
    }
  }

  "The payment method format" - {
    "should work symmetrically" in {
      forAll(arbitrary[PaymentMethod]) { pm =>
        pm.toXmlString.parseXmlString[PaymentMethod] must be(pm)
      }
    }
  }

  "The transport details format" - {
    "should work symmetrically" - {
      "for any transport details" in {
        forAll(transportDetailsGen) { details =>
          details.toXml.parseXml[TransportDetails] must be(details)
        }
      }

      "when carrier payment method" - {
        "is specified" in {
          val gen = for {
            details <- transportDetailsGen
            pm <- arbitrary[PaymentMethod]
          } yield details.copy(paymentMethod = Some(pm))

          forAll(gen) { details =>
            details.toXml.parseXml[TransportDetails] must be(details)
          }
        }
        "is missing" in {
          val gen = transportDetailsGen map { _.copy(paymentMethod = None) }

          forAll(gen) { details =>
            details.toXml.parseXml[TransportDetails] must be(details)
          }
        }
      }
      "when transport nationality" - {
        "is specified" in {
          val gen = for {
            details <- transportDetailsGen
            nationality <- arbitrary[Country]
          } yield details.copy(nationality = Some(nationality))

          forAll(gen) { details =>
            details.toXml.parseXml[TransportDetails] must be(details)
          }
        }
        "is missing" in {
          val gen = transportDetailsGen map { _.copy(nationality = None) }

          forAll(gen) { details =>
            details.toXml.parseXml[TransportDetails] must be(details)
          }
        }
      }
    }
  }
}
