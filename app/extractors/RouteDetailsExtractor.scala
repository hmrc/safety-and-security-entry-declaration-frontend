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

import java.time.{LocalDateTime, ZoneOffset}

import cats.implicits._

import models.{CustomsOffice => CustomsOfficeAnswer, _}
import models.completion.{CustomsOffice, Itinerary, LoadingPlace}
import models.completion.answers.RouteDetails
import pages.routedetails._
import queries.routedetails._

class RouteDetailsExtractor(
  override protected implicit val answers: UserAnswers
) extends Extractor[RouteDetails] {

  private lazy val customsOffice: ValidationResult[CustomsOffice] = {
    val customsOfficeId = requireAnswer(CustomsOfficeOfFirstEntryPage)
    val arrivalDate = requireAnswer(ArrivalDateAndTimePage)

    (customsOfficeId, arrivalDate).mapN { (id: CustomsOfficeAnswer, date: ArrivalDateAndTime) =>
      CustomsOffice(id.code, LocalDateTime.of(date.date, date.time).toInstant(ZoneOffset.UTC))
    }
  }

  private lazy val itinerary: ValidationResult[Itinerary] = {
    val originCountry = requireAnswer(CountryOfDeparturePage)
    val otherCountries = getList(
      GoodsPassThroughOtherCountriesPage,
      AllCountriesEnRouteQuery,
      CountryEnRoutePage(Index(0))
    )
    (originCountry, otherCountries).mapN { (first, others) => Itinerary(first +: others) }
  }

  override def extract(): ValidationResult[RouteDetails] = {
    val placesOfLoading: ValidationResult[Map[Int, LoadingPlace]] = {
      requireList(AllPlacesOfLoadingQuery, PlaceOfLoadingPage(Index(0))) map {
        _.map { p => p.key -> LoadingPlace(p.country, p.place) }.toMap
      }
    }
    val placesOfUnloading: ValidationResult[Map[Int, LoadingPlace]] = {
      requireList(AllPlacesOfUnloadingQuery, PlaceOfUnloadingPage(Index(0))) map {
        _.map { p => p.key -> LoadingPlace(p.country, p.place) }.toMap
      }
    }

    (placesOfLoading, placesOfUnloading, itinerary, customsOffice).mapN(RouteDetails)
  }
}
