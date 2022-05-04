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

package extractors;

import base.SpecBase
import models.completion.answers.{GoodsItem, Parties, Predec, RouteDetails}
import models.{Index, UserAnswers}
import org.scalacheck.Arbitrary.arbitrary
import cats.implicits._
import pages.goods.{AnyShippingContainersPage}
import play.api.libs.json.Json

class GoodsItemsExtractorSpec extends SpecBase {
  import GoodsItemsExtractorSpec._

  private val predec = arbitrary[Predec].sample.value
  private val routeDetails = arbitrary[RouteDetails].sample.value
  private val parties = arbitrary[Parties].sample.value

  private val dummyAnswers: UserAnswers = {
    emptyUserAnswers.copy(data = Json.obj("goodsItems" -> Json.arr("1", "2", "3")))
  }

  "The goods items extractor" - {
    "should correctly extract list of goods items" in {
      val actual = {
        new StubExtractor(predec, routeDetails, parties)(dummyAnswers).extract().invalidValue.toList
      }

      // We've stubbed the underlying GoodsItemExtractor with a simple failure, and we expect to
      // have invoked that for each index for which we have goods items
      val expected = (0 to 2).map {
        i => ValidationError.MissingField(AnyShippingContainersPage(Index(i)))
      }


      actual must contain theSameElementsInOrderAs(expected)
    }
    "should provide an error when attempting to retrieve goods items where none exist" in{
      val dummyAnswers = emptyUserAnswers
      val actual = {
        new StubExtractor(predec, routeDetails, parties)(dummyAnswers).extract().invalidValue.toList(0)
      }
      val expected = ValidationError.MissingQueryResult

      actual must be(expected)
    }
  }
}

object GoodsItemsExtractorSpec {
  class StubExtractor(
    predec: Predec,
    routeDetails: RouteDetails,
    parties: Parties
  )(override protected implicit val answers: UserAnswers)
    extends GoodsItemsExtractor(predec, routeDetails, parties)(answers) {

    override protected def extractOne(index: Index): ValidationResult[GoodsItem] = {
      ValidationError.MissingField(AnyShippingContainersPage(index)).invalidNec
    }
  }
}
