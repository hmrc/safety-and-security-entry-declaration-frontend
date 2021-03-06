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

import base.SpecBase
import cats.implicits._
import extractors.ValidationError._
import models.completion.Party
import models.completion.answers.Predec
import models.{Address, GbEori, LodgingPersonType, ProvideGrossWeight, TraderIdentity, UserAnswers}
import org.scalacheck.Arbitrary.arbitrary
import pages.predec._

class PredecExtractorSpec extends SpecBase {
  private val location = "test-declaration-location"
  private val totalMass = 1000
  private val carrierEORI = arbitrary[GbEori].sample.value
  private val name = "Name"
  private val address = arbitrary[Address].sample.value

  private val validAnswers = {
    arbitrary[UserAnswers].sample.value
      .set(DeclarationPlacePage, location).success.value
      .set(ProvideGrossWeightPage, ProvideGrossWeight.Overall).success.value
      .set(TotalGrossWeightPage, BigDecimal.exact(totalMass)).success.value
      .set(LodgingPersonTypePage, LodgingPersonType.Representative).success.value
      .set(CarrierIdentityPage, TraderIdentity.GBEORI).success.value
      .set(CarrierEORIPage, carrierEORI).success.value
  }

  "The predeclaration extractor" - {
    "should correctly extract a valid predeclaration" in {
      implicit val answers = validAnswers

      val expected = Predec(
        lrn = validAnswers.lrn,
        location = location,
        carrier = Some(Party.ByEori(carrierEORI)),
        totalMass = Some(totalMass)
      )
      val actual = new PredecExtractor().extract().value

      actual must be(expected)
    }

    "should correctly extract when not answering optional questions" in {
      implicit val answers = {
        validAnswers
          .set(ProvideGrossWeightPage, ProvideGrossWeight.PerItem).success.value
          .remove(TotalGrossWeightPage).success.value
          .set(LodgingPersonTypePage, LodgingPersonType.Carrier).success.value
          .remove(CarrierEORIPage).success.value
      }

      val expected = Predec(
        lrn = validAnswers.lrn,
        location = location,
        carrier = None,
        totalMass = None
      )
      val actual = new PredecExtractor().extract().value

      actual must be(expected)
    }

    "should fail for any missing required fields" in {
      implicit val answers = {
        validAnswers
          .remove(DeclarationPlacePage).success.value
          .remove(ProvideGrossWeightPage).success.value
          .remove(TotalGrossWeightPage).success.value
          .remove(LodgingPersonTypePage).success.value
      }

      val expected = List(
        MissingField(DeclarationPlacePage),
        MissingField(ProvideGrossWeightPage),
        MissingField(LodgingPersonTypePage)
      )
      val actual = new PredecExtractor().extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }

    "should fail if gross weight not fully provided" in {
      implicit val answers = validAnswers.remove(TotalGrossWeightPage).success.value

      val expected = List(MissingField(TotalGrossWeightPage))
      val actual = new PredecExtractor().extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }

    "should fail if carrier identity is not provided" in {
      implicit val answers = validAnswers.remove(CarrierIdentityPage).success.value

      val expected = List(MissingField(CarrierIdentityPage))
      val actual = new PredecExtractor().extract().invalidValue.toList

      actual must contain theSameElementsAs expected
    }

    "should fail if carrier is identified by EORI and EORI is not provided" in  {
      implicit val answers = validAnswers.remove(CarrierEORIPage).success.value

      val expected = List(MissingField(CarrierEORIPage))
      val actual = new PredecExtractor().extract().invalidValue.toList

      actual must contain theSameElementsAs expected
    }

    "should fail if carrier is identified by Name and Address and Name is not provided" in {
      implicit val answers =
        validAnswers
          .set(CarrierIdentityPage, TraderIdentity.NameAddress).success.value
          .set(CarrierAddressPage, address).success.value
          .remove(CarrierEORIPage).success.value

      val expected = List(MissingField(CarrierNamePage))
      val actual = new PredecExtractor().extract().invalidValue.toList

      actual must contain theSameElementsAs expected
    }

    "should fail if carrier is identified by Name and Address and Address is not provided" in {
      implicit val answers =
        validAnswers
          .set(CarrierIdentityPage, TraderIdentity.NameAddress).success.value
          .set(CarrierNamePage, name).success.value
          .remove(CarrierEORIPage).success.value

      val expected = List(MissingField(CarrierAddressPage))
      val actual = new PredecExtractor().extract().invalidValue.toList

      actual must contain theSameElementsAs expected
    }
  }
}
