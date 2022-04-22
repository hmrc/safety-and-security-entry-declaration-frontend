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
import extractors.ValidationError.MissingField
import models._
import models.completion._
import models.completion.answers._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import pages.consignees.{AnyConsigneesKnownPage, ConsigneeIdentityPage, NotifiedPartyIdentityPage}
import pages.consignors.ConsignorIdentityPage
import queries.consignees.{AllConsigneesQuery, AllNotifiedPartiesQuery}
import queries.consignors.AllConsignorsQuery

class PartiesExtractorSpec extends SpecBase {

  private val genTraders: Gen[List[Trader]] = {
    Gen.choose(1, 10)
      .flatMap { len => Gen.listOfN(len, arbitrary[Trader]) }
  }

  private val consignors = genTraders.sample.value
  private val consignees = genTraders.sample.value
  private val notifiedParties = genTraders.sample.value

  private val validAnswers = {
    arbitrary[UserAnswers].sample.value
      .set(AllConsignorsQuery, consignors).success.value
      .set(AllConsigneesQuery, consignees).success.value
      .set(AllNotifiedPartiesQuery, notifiedParties).success.value
      .set(AnyConsigneesKnownPage, true).success.value
  }

  private val expectedResult: Parties = Parties(
    tradersToParties(consignors),
    tradersToParties(consignees),
    tradersToParties(notifiedParties)
  )

  "The parties extractor" - {
    "should correctly extract valid parties" in {
      val actual = new PartiesExtractor()(validAnswers).extract().value
      actual must be(expectedResult)
    }

    "consignees" - {
      "should fail to extract if user said consignees known but didn't provide any" in {
        val answers = {
          validAnswers
            .set(AllConsigneesQuery, Nil).success.value
            .set(AnyConsigneesKnownPage, true).success.value
        }
        val expected = List(MissingField(ConsigneeIdentityPage(Index(0))))
        val actual = new PartiesExtractor()(answers).extract().invalidValue.toList
        actual must contain theSameElementsAs (expected)
      }

      "should extract empty list of consignees if user said no consignees known" in {
        val answers = {
          validAnswers
            .set(AllConsigneesQuery, Nil).success.value
            .set(AnyConsigneesKnownPage, false).success.value
        }
        val expected = expectedResult.copy(consignees = Map.empty)
        val actual = new PartiesExtractor()(answers).extract().value
        actual must be(expected)
      }
    }

    "consignors" - {
      "should fail when not provided" in {
        val answers = validAnswers.set(AllConsignorsQuery, Nil).success.value
        val expected = List(MissingField(ConsignorIdentityPage(Index(0))))
        val actual = new PartiesExtractor()(answers).extract().invalidValue.toList
        actual must contain theSameElementsAs (expected)
      }
    }

    "notifiedParties" - {
      "should fail when both notifiedParties and Consignees are empty" in {
        val answers = {
          validAnswers
            .set(AllNotifiedPartiesQuery, Nil).success.value
            .set(AnyConsigneesKnownPage, false).success.value
            .set(AllConsigneesQuery, Nil).success.value
        }

        val expected = List(MissingField(NotifiedPartyIdentityPage(Index(0))))
        val actual = new PartiesExtractor()(answers).extract().invalidValue.toList
        actual must contain theSameElementsAs (expected)
      }

      "should allow empty list if consignees are specified" in {
        val answers = {
          validAnswers
            .set(AllNotifiedPartiesQuery, Nil).success.value
            .set(AnyConsigneesKnownPage, true).success.value
            .set(AllConsigneesQuery, consignees).success.value
        }

        val expected = expectedResult.copy(notifiedParties = Map.empty)
        val actual = new PartiesExtractor()(answers).extract().value
        actual must be(expected)
      }
    }
  }
}
