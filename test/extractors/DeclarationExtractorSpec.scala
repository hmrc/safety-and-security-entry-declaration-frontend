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
import models.UserAnswers
import models.completion.answers._
import pages.predec.TotalGrossWeightPage

class DeclarationExtractorSpec extends SpecBase {
  import DeclarationExtractorSpec._

  private val predec = arbitrary[Predec].sample.value
  private val transport = arbitrary[Transport].sample.value
  private val routeDetails = arbitrary[RouteDetails].sample.value
  private val parties = arbitrary[Parties].sample.value
  private val items = List.empty[GoodsItem]

  // An arbitrary (mock) error response
  private val failure: ValidationError = ValidationError.MissingField(TotalGrossWeightPage)

  private implicit val answers: UserAnswers = emptyUserAnswers

  "The declaration extractor" - {
    "should combine all sections into a single model if all sections correctly extract" in {
      val extractor = new StubbedExtractor(
        predec.validNec,
        transport.validNec,
        routeDetails.validNec,
        parties.validNec,
        items.validNec
      )

      val expected = Declaration(predec, transport, routeDetails, items)
      val actual = extractor.extract().value

      actual must be(expected)
    }

    "should carry forward errors across all sections pre-goods items if they fail" in {
      val extractor = new StubbedExtractor(
        failure.invalidNec,
        failure.invalidNec,
        failure.invalidNec,
        failure.invalidNec,
        items.validNec
      )

      val expected = List.fill(4)(failure)
      val actual = extractor.extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }

    "should fail without attempting the goods items section if an earlier section fails" in {
      val extractor = new StubbedExtractor(
        failure.invalidNec,
        failure.invalidNec,
        failure.invalidNec,
        failure.invalidNec,
        failure.invalidNec
      )

      val expected = List.fill(4)(failure)
      val actual = extractor.extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }

    "should carry forward errors from the goods items section if it fails" in {
      val extractor = new StubbedExtractor(
        predec.validNec,
        transport.validNec,
        routeDetails.validNec,
        parties.validNec,
        failure.invalidNec
      )

      val expected = List(failure)
      val actual = extractor.extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }
  }
}

object DeclarationExtractorSpec extends Extractors {
  /**
   * Mock out the individual extractors used by the declaration extractor
   */
  class StubbedExtractor(
    override protected val extractedPredec: ValidationResult[Predec],
    override protected val extractedTransport: ValidationResult[Transport],
    override protected val extractedRouteDetails: ValidationResult[RouteDetails],
    override protected val extractedParties: ValidationResult[Parties],
    extractedItems: ValidationResult[List[GoodsItem]]
  )(override protected implicit val answers: UserAnswers) extends DeclarationExtractor {
    override protected def extractItems(
      predec: Predec,
      routeDetails: RouteDetails,
      parties: Parties
    ): ValidationResult[List[GoodsItem]] = extractedItems
  }
}
