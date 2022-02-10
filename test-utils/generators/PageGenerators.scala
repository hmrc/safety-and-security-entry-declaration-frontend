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

package generators

import models.Index
import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryDeclarationPlacePage: Arbitrary[DeclarationPlacePage.type] =
    Arbitrary(DeclarationPlacePage)

  implicit lazy val arbitraryGoodsDescriptionPage: Arbitrary[GoodsDescriptionPage] =
    Arbitrary(GoodsDescriptionPage(Index(0)))

  implicit lazy val arbitraryCommodityCodeKnownPage: Arbitrary[CommodityCodeKnownPage] =
    Arbitrary(CommodityCodeKnownPage(Index(0)))

  implicit lazy val arbitraryCommodityCodePage: Arbitrary[CommodityCodePage] =
    Arbitrary(CommodityCodePage(Index(0)))

  implicit lazy val arbitraryRemoveCountryEnRoutePage: Arbitrary[RemoveCountryEnRoutePage] =
    Arbitrary(RemoveCountryEnRoutePage(Index(0)))

  implicit lazy val arbitraryGoodsPassThroughOtherCountriesPage: Arbitrary[GoodsPassThroughOtherCountriesPage.type] =
    Arbitrary(GoodsPassThroughOtherCountriesPage)

  implicit lazy val arbitraryCountryEnRoutePage: Arbitrary[CountryEnRoutePage] =
    Arbitrary(CountryEnRoutePage(Index(0)))

  implicit lazy val arbitraryAddCountryEnRoutePage: Arbitrary[AddCountryEnRoutePage.type] =
    Arbitrary(AddCountryEnRoutePage)

  implicit lazy val arbitraryCustomsOfficeOfFirstEntryPage: Arbitrary[CustomsOfficeOfFirstEntryPage.type] =
    Arbitrary(CustomsOfficeOfFirstEntryPage)

  implicit lazy val arbitraryCountryOfOriginPage: Arbitrary[CountryOfOriginPage.type] =
    Arbitrary(CountryOfOriginPage)

  implicit lazy val arbitraryArrivalDateAndTimePage: Arbitrary[ArrivalDateAndTimePage.type] =
    Arbitrary(ArrivalDateAndTimePage)

  implicit lazy val arbitraryCarriersEORIPage: Arbitrary[CarriersEORIPage.type] =
    Arbitrary(CarriersEORIPage)

  implicit lazy val arbitraryIdentifyCarrierPage: Arbitrary[IdentifyCarrierPage.type] =
    Arbitrary(IdentifyCarrierPage)

  implicit lazy val arbitraryTransportModePage: Arbitrary[TransportModePage.type] =
    Arbitrary(TransportModePage)

  implicit lazy val arbitraryTotalGrossWeightPage: Arbitrary[TotalGrossWeightPage.type] =
    Arbitrary(TotalGrossWeightPage)

  implicit lazy val arbitraryGrossWeightPage: Arbitrary[GrossWeightPage.type] =
    Arbitrary(GrossWeightPage)

  implicit lazy val arbitraryLodgingPersonTypePage: Arbitrary[LodgingPersonTypePage.type] =
    Arbitrary(LodgingPersonTypePage)

  implicit lazy val arbitraryLocalReferenceNumberPage: Arbitrary[LocalReferenceNumberPage.type] =
    Arbitrary(LocalReferenceNumberPage)
}
