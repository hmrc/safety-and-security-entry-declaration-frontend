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
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import models.completion.{CustomsOffice, Itinerary}
import models.completion.downstream.{GoodsItem, Header, Submission}
import org.scalacheck.Gen

class SubmissionFormatsSpec
  extends SpecBase
  with SubmissionFormats
  with XmlImplicits
  with ScalaCheckPropertyChecks {

  "The submission format" - {
    "should work symmetrically" - {
      "for any submission" in {
        forAll(arbitrary[Submission]) { s =>
          s.toXml.parseXml[Submission] must be(s)
        }
      }

      "when seals are" - {
        "present" in {
          val gen = for {
            len <- Gen.choose(1, 10)
            seals <- Gen.listOfN(len, stringsWithMaxLength(27))
            submission <- arbitrary[Submission]
          } yield submission.copy(seals = seals)

          forAll(gen) { item => item.toXml.parseXml[Submission] must be(item) }
        }
        "absent" in {
          val gen = arbitrary[Submission] map { _.copy(seals = Nil) }
          forAll(gen) { item => item.toXml.parseXml[Submission] must be(item) }
        }
      }
    }
  }
}