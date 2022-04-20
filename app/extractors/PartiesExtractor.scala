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

import extractors.ValidationError.MissingField
import models._
import models.Trader._
import models.completion.Party
import models.completion.answers.Parties
import pages.consignees.{AnyConsigneesKnownPage, ConsigneeIdentityPage, NotifiedPartyIdentityPage}
import pages.consignors.ConsignorIdentityPage
import queries.consignees.{AllConsigneesQuery, AllNotifiedPartiesQuery}
import queries.consignors.AllConsignorsQuery

class PartiesExtractor(
  override protected implicit val answers: UserAnswers
) extends Extractor[Parties] {

  private lazy val consignees: ValidationResult[Map[Int, Party]] = {
    getList[Trader](
      AnyConsigneesKnownPage,
      AllConsigneesQuery,
      ConsigneeIdentityPage(Index(0))
    ).map(tradersToParties)
  }

  private lazy val consignors: ValidationResult[Map[Int, Party]] = {
    requireList(AllConsignorsQuery, ConsignorIdentityPage(Index(0))).map(tradersToParties)
  }

  private lazy val notifiedParties: ValidationResult[Map[Int, Party]] = {
    val traders: List[Trader] = answers.get(AllNotifiedPartiesQuery).flatMap {
      case Nil => None
      case results => Some(results)
    } getOrElse Nil
    tradersToParties(traders).validNec
  }

  private def tradersToParties(traders: List[Trader]): Map[Int, Party] = {
    traders.map {
      case TraderWithEori(key, eori) => key -> Party.ByEori(GbEori(eori))
      case TraderWithoutEori(key, name, addr) => key -> Party.ByAddress(name, addr)
    }.toMap
  }

  override def extract(): ValidationResult[Parties] = {
    val parties: ValidationResult[Parties] = (consignors, consignees, notifiedParties).mapN(Parties)
    parties.andThen {
      case Parties(_, consignees, notifiedParties) if consignees.isEmpty && notifiedParties.isEmpty =>
        MissingField(NotifiedPartyIdentityPage(Index(0))).invalidNec
      case p =>
        p.validNec
    }
  }
}
