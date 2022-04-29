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
import models.{LocalReferenceNumber, MovementReferenceNumber}
import models.completion.Party
import models.completion.downstream.{AmendmentTimePlace, Declaration, SubmissionTimePlace}

class DeclarationFormatsSpec
  extends SpecBase
  with DeclarationFormats
  with XmlImplicits
  with ScalaCheckPropertyChecks {

  "The declaration format" - {
    "should work symmetrically" - {
      "for any declaration" in {
        forAll(arbitrary[Declaration]) { s =>
          s.toXml.parseXml[Declaration] must be(s)
        }
      }

      "when reference is" - {
        "an lrn (submission)" in {
          val gen = for {
            sub <- submissionGen
            lrn <- arbitrary[LocalReferenceNumber]
          } yield sub.copy(header = sub.header.copy(ref = lrn))

          forAll(gen) { dec => dec.toXml.parseXml[Declaration] must be(dec) }
        }

        "an mrn (amendment)" in {
          val gen = for {
            amend <- amendmentGen
            mrn <- arbitrary[MovementReferenceNumber]
          } yield amend.copy(header = amend.header.copy(ref = mrn))

          forAll(gen) { dec => dec.toXml.parseXml[Declaration] must be(dec) }
        }
      }

      "when declaration place + datetime is" - {
        "for a submission" in {
          val gen = for {
            sub <- submissionGen
            timePlace <- arbitrary[SubmissionTimePlace]
          } yield sub.copy(header = sub.header.copy(timePlace = timePlace))

          forAll(gen) { dec => dec.toXml.parseXml[Declaration] must be(dec) }
        }

        "for an amendment" in {
          val gen = for {
            amend <- amendmentGen
            timePlace <- arbitrary[AmendmentTimePlace]
          } yield amend.copy(header = amend.header.copy(timePlace = timePlace))

          forAll(gen) { dec => dec.toXml.parseXml[Declaration] must be(dec) }
        }
      }

      "when seals are" - {
        "present" in {
          val gen = for {
            len <- Gen.choose(1, 10)
            seals <- Gen.listOfN(len, stringsWithMaxLength(27))
            submission <- arbitrary[Declaration]
          } yield submission.copy(seals = seals)

          forAll(gen) { item => item.toXml.parseXml[Declaration] must be(item) }
        }
        "absent" in {
          val gen = arbitrary[Declaration] map { _.copy(seals = Nil) }
          forAll(gen) { item => item.toXml.parseXml[Declaration] must be(item) }
        }
      }

      "when consignor is" - {
        "present" in {
          val gen = for {
            consignor <- arbitrary[Party]
            submission <- arbitrary[Declaration]
          } yield submission.copy(consignor = Some(consignor))

          forAll(gen) { item => item.toXml.parseXml[Declaration] must be(item) }
        }

        "absent" in {
          val gen = arbitrary[Declaration] map { _.copy(consignor = None) }
          forAll(gen) { item => item.toXml.parseXml[Declaration] must be(item) }
        }
      }

      "when carrier is" - {
        "present" in {
          val gen = for {
            carrier <- arbitrary[Party]
            submission <- arbitrary[Declaration]
          } yield submission.copy(carrier = Some(carrier))

          forAll(gen) { item => item.toXml.parseXml[Declaration] must be(item) }
        }

        "absent (assumed the same as declaring person)" in {
          val gen = arbitrary[Declaration] map { _.copy(carrier = None) }
          forAll(gen) { item => item.toXml.parseXml[Declaration] must be(item) }
        }
      }
    }

    "should produce a submission payload which can be validated against the CC315A schema" in {
      forAll(submissionGen) { dec =>
        val payload = dec.toXml.toString
        XmlValidator.SubmissionValidator.validate(payload)
      }
    }

    "should produce an amendment payload which can be validated against the CC313A schema" in {
      forAll(amendmentGen) { dec =>
        val payload = dec.toXml.toString
        XmlValidator.AmendmentValidator.validate(payload)
      }
    }
  }
}
