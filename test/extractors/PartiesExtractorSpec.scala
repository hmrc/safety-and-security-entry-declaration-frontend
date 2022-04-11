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

  private def tenTraders: List[Trader] = {
    Gen.choose(1, 10)
      .flatMap { len => Gen.listOfN(len, arbitrary[Trader]) }
      .sample.value
  }

  private val consigneeIdentity = arbitrary[ConsigneeIdentity].sample.value
  private val consignorIdentity = arbitrary[ConsignorIdentity].sample.value
  private val notifiedParty = arbitrary[NotifiedPartyIdentity].sample.value

  private val validAnswers = {
    arbitrary[UserAnswers].sample.value
      .set(AllConsigneesQuery, tenTraders).success.value
      .set(AllConsignorsQuery, tenTraders).success.value
      .set(AllNotifiedPartiesQuery, tenTraders).success.value
      .set(AnyConsigneesKnownPage, true).success.value
      .set(ConsigneeIdentityPage(Index(0)), consigneeIdentity).success.value
      .set(ConsignorIdentityPage(Index(0)), consignorIdentity).success.value
      .set(NotifiedPartyIdentityPage(Index(0)), notifiedParty).success.value
  }

  private def tradersToParties(traders: List[Trader]): Map[Int, Party] = {
    traders.map {
      case TraderWithEori(key, eori) => key -> Party.ByEori(eori)
      case TraderWithoutEori(key, name, addr) => key -> Party.ByAddress(name, addr)
    }.toMap
  }

  private val expectedResult: Parties = Parties(
    tradersToParties(tenTraders),
    tradersToParties(tenTraders),
    tradersToParties(tenTraders)
  )

  "The parties extractor" - {
    "should correctly extract valid parties" in {
      val actual = new PartiesExtractor()(validAnswers).extract().value
      actual must be(expectedResult)
    }
  }

  "consignees" - {
    "should fail when not provided" in {
      val answers = validAnswers.set(AllConsigneesQuery, Nil).success.value
      val expected = List(MissingField(ConsigneeIdentityPage(Index(0))))
      val actual = new PartiesExtractor()(answers).extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }
  }

  "consignors" - {
    "should fail when not provided" in {
      val answers = validAnswers.set(AllConsignorsQuery, Nil).success.value
      val expected = List(MissingField(ConsignorIdentityPage(Index(0))))
      val actual = new PartiesExtractor()(answers).extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }
  }

  "notifedParties" - {
    "should fail when not provided" in {
      val answers = validAnswers.set(AllNotifiedPartiesQuery, Nil).success.value
      val expected = List(MissingField(NotifiedPartyIdentityPage(Index(0))))
      val actual = new PartiesExtractor()(answers).extract().invalidValue.toList

      actual must contain theSameElementsAs(expected)
    }
  }

}