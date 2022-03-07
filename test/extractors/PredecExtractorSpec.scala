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
import models.{ProvideGrossWeight, TransportMode, UserAnswers}
import models.completion.answers.Predec
import pages.TransportModePage
import pages.predec._

class PredecExtractorSpec extends SpecBase {
  private val location = "test-declaration-location"
  private val crn = "TEST-CRN"
  private val totalMass = 1000
  private val transportMode = TransportMode.Road

  private val validAnswers = {
    arbitrary[UserAnswers].sample.value
      .set(DeclarationPlacePage, location).success.value
      .set(OverallCrnKnownPage, true).success.value
      .set(OverallCrnPage, crn).success.value
      .set(ProvideGrossWeightPage, ProvideGrossWeight.Overall).success.value
      .set(TotalGrossWeightPage, BigDecimal.exact(totalMass)).success.value
      .set(TransportModePage, transportMode).success.value
  }

  "The predeclaration extractor" - {
    "should correctly extract a valid predeclaration" in {
      implicit val answers = validAnswers

      val expected = Predec(
        lrn = validAnswers.lrn,
        location = location,
        crn = Some(crn),
        totalMass = Some(totalMass),
        transport = transportMode
      )
      val actual = new PredecExtractor().extract().value

      actual must be(expected)
    }

    "should correctly extract when not answering optional questions" in {
      implicit val answers = {
        validAnswers
          .set(OverallCrnKnownPage, false).success.value
          .remove(OverallCrnPage).success.value
          .set(ProvideGrossWeightPage, ProvideGrossWeight.PerItem).success.value
          .remove(TotalGrossWeightPage).success.value
      }

      val expected = Predec(
        lrn = validAnswers.lrn,
        location = location,
        crn = None,
        totalMass = None,
        transport = transportMode
      )
      val actual = new PredecExtractor().extract().value

      actual must be(expected)
    }

    "should fail for any missing required fields" in {
      implicit val answers = {
        validAnswers
          .remove(DeclarationPlacePage).success.value
          .remove(OverallCrnKnownPage).success.value
          .remove(OverallCrnPage).success.value
          .remove(ProvideGrossWeightPage).success.value
          .remove(TotalGrossWeightPage).success.value
          .remove(TransportModePage).success.value
      }

      val expected = List(
        MissingField(DeclarationPlacePage),
        MissingField(OverallCrnKnownPage),
        MissingField(ProvideGrossWeightPage),
        MissingField(TransportModePage)
      )
      val actual = new PredecExtractor().extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }

    "should fail if CRN known but not provided" in {
      implicit val answers = validAnswers.remove(OverallCrnPage).success.value

      val expected = List(MissingField(OverallCrnPage))
      val actual = new PredecExtractor().extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }

    "should fail if gross weight not fully provided" in {
      implicit val answers = validAnswers.remove(TotalGrossWeightPage).success.value

      val expected = List(MissingField(TotalGrossWeightPage))
      val actual = new PredecExtractor().extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }

  }
}
