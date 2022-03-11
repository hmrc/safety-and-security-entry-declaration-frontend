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

package extractors

import cats.implicits._
import org.scalacheck.Arbitrary.arbitrary

import base.SpecBase
import extractors.ValidationError._
import models.{ConsignorIdentity => ConsignorIdentityAnswer, _}
import models.completion.answers.ConsignorIdentity
import pages.consignors._

class ConsignorIdentityExtractorSpec extends SpecBase {
  private val baseAnswers: UserAnswers = emptyUserAnswers
  private val itemOne: Index = Index(0)
  private val itemTwo: Index = Index(1)
  private val itemThree: Index = Index(2)

  private val name = "Test Name"
  private val gb = Country("GB", "United Kingdom")
  private val addr = Address("123 Test Address", "City", "POSTCODE", gb)

  "The consignor identity extractor" - {
    "should extract consignor EORI" in {
      implicit val answers = {
        baseAnswers
          .set(ConsignorIdentityPage(itemOne), ConsignorIdentityAnswer.GBEORI).success.value
          .set(ConsignorEORIPage(itemOne), eori).success.value
      }

      val expected = ConsignorIdentity.ByEori(eori)
      val actual = new ConsignorIdentityExtractor(itemOne).extract().value

      actual must be(expected)
    }

    "should extract consignor name / address" in {
      implicit val answers = {
        baseAnswers
          .set(ConsignorIdentityPage(itemOne), ConsignorIdentityAnswer.NameAddress).success.value
          .set(ConsignorNamePage(itemOne), name).success.value
          .set(ConsignorAddressPage(itemOne), addr).success.value
      }

      val expected = ConsignorIdentity.ByAddress(name, addr)
      val actual = new ConsignorIdentityExtractor(itemOne).extract().value

      actual must be(expected)
    }

    "should extract separate consignor identities for different item numbers" in {
      val eori1 = arbitrary[GbEori].sample.value
      val eori2 = arbitrary[GbEori].sample.value

      implicit val answers = {
        baseAnswers
          .set(ConsignorIdentityPage(itemOne), ConsignorIdentityAnswer.GBEORI).success.value
          .set(ConsignorEORIPage(itemOne), eori1).success.value
          .set(ConsignorIdentityPage(itemTwo), ConsignorIdentityAnswer.GBEORI).success.value
          .set(ConsignorEORIPage(itemTwo), eori2).success.value
          .set(ConsignorIdentityPage(itemThree), ConsignorIdentityAnswer.NameAddress).success.value
          .set(ConsignorNamePage(itemThree), name).success.value
          .set(ConsignorAddressPage(itemThree), addr).success.value
      }

      Seq(
        itemOne -> ConsignorIdentity.ByEori(eori1),
        itemTwo -> ConsignorIdentity.ByEori(eori2),
        itemThree -> ConsignorIdentity.ByAddress(name, addr)
      ) foreach { case (itemNum, expected) =>
        val actual = new ConsignorIdentityExtractor(itemNum).extract().value
        actual must be(expected)
      }
    }

    "should fail if missing the answer to how we're identifying consignor" in {
      implicit val answers = baseAnswers.remove(ConsignorIdentityPage(itemOne)).success.value

      val expected = Seq(MissingField(ConsignorIdentityPage(itemOne)))
      val actual = new ConsignorIdentityExtractor(itemOne).extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }

    "should fail if missing consignor EORI" in {
      implicit val answers = {
        baseAnswers
          .set(ConsignorIdentityPage(itemOne), ConsignorIdentityAnswer.GBEORI).success.value
          .remove(ConsignorEORIPage(itemOne)).success.value
      }

      val expected = Seq(MissingField(ConsignorEORIPage(itemOne)))
      val actual = new ConsignorIdentityExtractor(itemOne).extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }

    "should fail if missing consignor name and/or address" in {
      implicit val answers = {
        baseAnswers
          .set(ConsignorIdentityPage(itemOne), ConsignorIdentityAnswer.NameAddress).success.value
          .remove(ConsignorNamePage(itemOne)).success.value
          .remove(ConsignorAddressPage(itemOne)).success.value
      }

      val expected = Seq(
        MissingField(ConsignorNamePage(itemOne)),
        MissingField(ConsignorAddressPage(itemOne))
      )
      val actual = new ConsignorIdentityExtractor(itemOne).extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }
  }
}

