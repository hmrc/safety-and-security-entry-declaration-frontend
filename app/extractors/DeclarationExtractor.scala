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

import models.UserAnswers
import models.completion.answers._

/**
 * Extract an entire declaration from user answers into a modelled form
 */
class DeclarationExtractor()(override protected implicit val answers: UserAnswers)
  extends Extractor[Declaration] {

  protected def extractedPredec: ValidationResult[Predec] = {
    new PredecExtractor().extract()
  }

  protected def extractedTransport: ValidationResult[Transport] = {
    new TransportExtractor().extract()
  }

  protected def extractedRouteDetails: ValidationResult[RouteDetails] = {
    new RouteDetailsExtractor().extract()
  }

  protected def extractedParties: ValidationResult[Parties] = {
    new PartiesExtractor().extract()
  }

  protected def extractItems(
    predec: Predec,
    routeDetails: RouteDetails,
    parties: Parties
  ): ValidationResult[List[GoodsItem]] = {
    new GoodsItemsExtractor(predec, routeDetails, parties).extract()
  }

  override def extract(): ValidationResult[Declaration] = {
    (extractedPredec, extractedTransport, extractedRouteDetails, extractedParties).tupled.andThen {
      case (predec, transport, route, parties) =>
        extractItems(predec, route, parties) map { items =>
          Declaration(predec, transport, route, items)
        }
    }
  }
}
