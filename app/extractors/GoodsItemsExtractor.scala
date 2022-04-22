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
import models.{Index, UserAnswers}
import models.completion.answers.{GoodsItem, Parties, RouteDetails}
import queries.goods._


class GoodsItemsExtractor(
  routeDetails: RouteDetails,
  parties: Parties
)(override protected implicit val answers: UserAnswers) extends Extractor[Seq[GoodsItem]] {

  override def extract(): ValidationResult[List[GoodsItem]] = {
    // FIXME: Return a failed ValidationResult in case of no items, not an exception
    val indexes = answers.get(DeriveNumberOfGoods) map {
      count => (0 to (count - 1)).toList.map {
        index => extractOne(Index(index))
      }.sequence
    } getOrElse {
      ValidationError.MissingQueryResult.invalidNec
    }
    indexes
  }

  protected def extractOne(index: Index): ValidationResult[GoodsItem] = {
    new GoodsItemExtractor(routeDetails.placesOfLoading, routeDetails.placesOfUnloading, parties, index).extract()
  }
}
