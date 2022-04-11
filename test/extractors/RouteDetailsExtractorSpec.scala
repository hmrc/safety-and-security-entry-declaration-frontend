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
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

import base.SpecBase
import extractors.ValidationError.MissingField
import models.completion._
import models.completion.answers._
import models.{CustomsOffice => CustomsOfficeAnswer, _}
import pages.routedetails._
import queries.routedetails._

class RouteDetailsExtractorSpec extends SpecBase {
  private val originCountry = arbitrary[Country].sample.value

  private val placesOfLoading = {
    Gen.choose(1, 10)
      .flatMap { len => Gen.listOfN(len, arbitrary[PlaceOfLoading]) }
      .sample.value
  }
  private val placesOfUnloading = {
    Gen.choose(1, 10)
      .flatMap { len => Gen.listOfN(len, arbitrary[PlaceOfUnloading]) }
      .sample.value
  }

  private val extraCountries = {
    Gen.choose(1, 10)
      .flatMap { len => Gen.listOfN(len, arbitrary[Country]) }
      .sample.value
  }
  
  private val customsOfficeAnswer = arbitrary[CustomsOfficeAnswer].sample.value
  private val arrivalDatetime = arbitrary[ArrivalDateAndTime].sample.value

  private val expectedResult = RouteDetails(
    placesOfLoading.map { p => p.key -> LoadingPlace(p.country, p.place) }.toMap,
    placesOfUnloading.map { p => p.key -> LoadingPlace(p.country, p.place) }.toMap,
    Itinerary(originCountry +: extraCountries),
    CustomsOffice(
      customsOfficeAnswer.code,
      LocalDateTime.of(arrivalDatetime.date, arrivalDatetime.time).toInstant(ZoneOffset.UTC)
    )
  )

  private val validAnswers = {
    arbitrary[UserAnswers].sample.value
      .set(CountryOfDeparturePage, originCountry).success.value
      .set(AllPlacesOfLoadingQuery, placesOfLoading).success.value
      .set(GoodsPassThroughOtherCountriesPage, true).success.value
      .set(AllCountriesEnRouteQuery, extraCountries).success.value
      .set(CustomsOfficeOfFirstEntryPage, customsOfficeAnswer).success.value
      .set(ArrivalDateAndTimePage, arrivalDatetime).success.value
      .set(AllPlacesOfUnloadingQuery, placesOfUnloading).success.value
  }

  "The routedetails extractor" - {
    "should correctly extract valid routedetails" in {
      val actual = new RouteDetailsExtractor()(validAnswers).extract().value
      actual must be(expectedResult)
    }

    "places of loading" - {
      "should fail when not provided" in {
        val answers = validAnswers.set(AllPlacesOfLoadingQuery, Nil).success.value

        val expected = List(MissingField(PlaceOfLoadingPage(Index(0))))
        val actual = new RouteDetailsExtractor()(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }
    }

    "places of unloading" - {
      "should fail when not provided" in {
        val answers = validAnswers.set(AllPlacesOfUnloadingQuery, Nil).success.value

        val expected = List(MissingField(PlaceOfUnloadingPage(Index(0))))
        val actual = new RouteDetailsExtractor()(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }
    }

    "itinerary" - {
      "should correctly extract if goods do not pass through other countries" in {
        val answers = {
          validAnswers
            .set(GoodsPassThroughOtherCountriesPage, false).success.value
            .set(AllCountriesEnRouteQuery, Nil).success.value
        }

        val expected = expectedResult.copy(itinerary = Itinerary(List(originCountry)))
        val actual = new RouteDetailsExtractor()(answers).extract().value

        actual must be(expected)
      }

      "should fail if no answer to country of departure page" in {
        val answers = validAnswers.remove(CountryOfDeparturePage).success.value

        val expected = List(MissingField(CountryOfDeparturePage))
        val actual = new RouteDetailsExtractor()(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }

      "should fail if no answer to goods pass through other countries page" in {
        val answers = validAnswers.remove(GoodsPassThroughOtherCountriesPage).success.value

        val expected = List(MissingField(GoodsPassThroughOtherCountriesPage))
        val actual = new RouteDetailsExtractor()(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }

      "should fail if goods pass through other countries but no other countries provided" in {
        val answers = validAnswers.set(AllCountriesEnRouteQuery, Nil).success.value

        val expected = List(MissingField(CountryEnRoutePage(Index(0))))
        val actual = new RouteDetailsExtractor()(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }
    }

    "customs office" - {
      "should fail if no customs office code is selected" in {
        val answers = validAnswers.remove(CustomsOfficeOfFirstEntryPage).success.value

        val expected = List(MissingField(CustomsOfficeOfFirstEntryPage))
        val actual = new RouteDetailsExtractor()(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }

      "should fail if no customs office datetime is provided" in {
        val answers = validAnswers.remove(ArrivalDateAndTimePage).success.value

        val expected = List(MissingField(ArrivalDateAndTimePage))
        val actual = new RouteDetailsExtractor()(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }
    }
  }
}
