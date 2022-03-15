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
import pages.consignors._
import pages._
import pages.consignees._
import pages.goods._
import pages.predec._
import pages.routedetails._
import pages.transport._

trait PageGenerators {

  implicit lazy val arbitraryItemContainerNumberPage: Arbitrary[ItemContainerNumberPage.type] =
    Arbitrary(ItemContainerNumberPage)

  implicit lazy val arbitraryUnloadingPlacePage: Arbitrary[UnloadingPlacePage] =
    Arbitrary(UnloadingPlacePage(Index(0)))

  implicit lazy val arbitraryNotifiedPartyPage: Arbitrary[NotifiedPartyPage] =
    Arbitrary(NotifiedPartyPage(Index(0)))

  implicit lazy val arbitraryLoadingPlacePage: Arbitrary[LoadingPlacePage] =
    Arbitrary(LoadingPlacePage(Index(0)))

  implicit lazy val arbitraryConsignorPage: Arbitrary[ConsignorPage] =
    Arbitrary(ConsignorPage(Index(0)))

  implicit lazy val arbitraryConsigneeKnownPage: Arbitrary[ConsigneeKnownPage] =
    Arbitrary(ConsigneeKnownPage(Index(0)))

  implicit lazy val arbitraryConsigneePage: Arbitrary[ConsigneePage] =
    Arbitrary(ConsigneePage(Index(0)))

  implicit lazy val arbitrarySealPage: Arbitrary[SealPage.type] =
    Arbitrary(SealPage)

  implicit lazy val arbitraryRoroUnaccompaniedIdentityPage: Arbitrary[RoroUnaccompaniedIdentityPage.type] =
    Arbitrary(RoroUnaccompaniedIdentityPage)

  implicit lazy val arbitraryRoroAccompaniedIdentityPage: Arbitrary[RoroAccompaniedIdentityPage.type] =
    Arbitrary(RoroAccompaniedIdentityPage)

  implicit lazy val arbitraryRoadIdentityPage: Arbitrary[RoadIdentityPage.type] =
    Arbitrary(RoadIdentityPage)

  implicit lazy val arbitraryRemoveSealPage: Arbitrary[RemoveSealPage.type] =
    Arbitrary(RemoveSealPage)

  implicit lazy val arbitraryRemoveOverallDocumentPage: Arbitrary[RemoveOverallDocumentPage.type] =
    Arbitrary(RemoveOverallDocumentPage)

  implicit lazy val arbitraryRailIdentityPage: Arbitrary[RailIdentityPage.type] =
    Arbitrary(RailIdentityPage)

  implicit lazy val arbitraryOverallDocumentPage: Arbitrary[OverallDocumentPage.type] =
    Arbitrary(OverallDocumentPage)

  implicit lazy val arbitraryNationalityOfTransportPage: Arbitrary[NationalityOfTransportPage.type] =
    Arbitrary(NationalityOfTransportPage)

  implicit lazy val arbitraryMaritimeIdentityPage: Arbitrary[MaritimeIdentityPage.type] =
    Arbitrary(MaritimeIdentityPage)

  implicit lazy val arbitraryAnyOverallDocumentsPage: Arbitrary[AnyOverallDocumentsPage.type] =
    Arbitrary(AnyOverallDocumentsPage)

  implicit lazy val arbitraryAirIdentityPage: Arbitrary[AirIdentityPage.type] =
    Arbitrary(AirIdentityPage)

  implicit lazy val arbitraryAddSealPage: Arbitrary[AddSealPage.type] =
    Arbitrary(AddSealPage)

  implicit lazy val arbitraryAddOverallDocumentPage: Arbitrary[AddOverallDocumentPage.type] =
    Arbitrary(AddOverallDocumentPage)

  implicit lazy val arbitraryAddAnySealsPage: Arbitrary[AddAnySealsPage.type] =
    Arbitrary(AddAnySealsPage)

  implicit lazy val arbitraryPlaceOfUnloadingPage: Arbitrary[PlaceOfUnloadingPage] =
    Arbitrary(PlaceOfUnloadingPage(Index(0)))

  implicit lazy val arbitraryPlaceOfLoadingPage: Arbitrary[PlaceOfLoadingPage] =
    Arbitrary(PlaceOfLoadingPage(Index(0)))

  implicit lazy val arbitraryAddAnyNotifiedPartiesPage: Arbitrary[AddAnyNotifiedPartiesPage.type] =
    Arbitrary(AddAnyNotifiedPartiesPage)

  implicit lazy val arbitraryPaymentMethodPage: Arbitrary[PaymentMethodPage] =
    Arbitrary(goods.PaymentMethodPage(Index(0)))

  implicit lazy val arbitraryAddPaymentMethodPage: Arbitrary[AddPaymentMethodPage] =
    Arbitrary(goods.AddPaymentMethodPage(Index(0)))

  implicit lazy val arbitraryNotifiedPartyNamePage: Arbitrary[NotifiedPartyNamePage] =
    Arbitrary(consignees.NotifiedPartyNamePage(Index(0)))

  implicit lazy val arbitraryNotifiedPartyIdentityPage: Arbitrary[NotifiedPartyIdentityPage] =
    Arbitrary(consignees.NotifiedPartyIdentityPage(Index(0)))

  implicit lazy val arbitraryNotifiedPartyEORIPage: Arbitrary[NotifiedPartyEORIPage] =
    Arbitrary(consignees.NotifiedPartyEORIPage(Index(0)))

  implicit lazy val arbitraryNotifiedPartyAddressPage: Arbitrary[NotifiedPartyAddressPage] =
    Arbitrary(consignees.NotifiedPartyAddressPage(Index(0)))

  implicit lazy val arbitraryConsigneeNamePage: Arbitrary[ConsigneeNamePage] =
    Arbitrary(consignees.ConsigneeNamePage(Index(0)))

  implicit lazy val arbitraryConsigneeIdentityPage: Arbitrary[ConsigneeIdentityPage] =
    Arbitrary(consignees.ConsigneeIdentityPage(Index(0)))

  implicit lazy val arbitraryConsigneeEORIPage: Arbitrary[ConsigneeEORIPage] =
    Arbitrary(consignees.ConsigneeEORIPage(Index(0)))

  implicit lazy val arbitraryConsigneeAddressPage: Arbitrary[ConsigneeAddressPage] =
    Arbitrary(consignees.ConsigneeAddressPage(Index(0)))

  implicit lazy val arbitraryConsignorNamePage: Arbitrary[ConsignorNamePage] =
    Arbitrary(consignors.ConsignorNamePage(Index(0)))

  implicit lazy val arbitraryConsignorEORIPage: Arbitrary[ConsignorEORIPage] =
    Arbitrary(consignors.ConsignorEORIPage(Index(0)))

  implicit lazy val arbitraryConsignorAddressPage: Arbitrary[ConsignorAddressPage] =
    Arbitrary(consignors.ConsignorAddressPage(Index(0)))

  implicit lazy val arbitraryAnyConsigneesKnownPage: Arbitrary[AnyConsigneesKnownPage.type] =
    Arbitrary(consignees.AnyConsigneesKnownPage)

  implicit lazy val arbitraryGoodsItemGrossWeightPage: Arbitrary[GoodsItemGrossWeightPage] =
    Arbitrary(goods.GoodsItemGrossWeightPage(Index(0)))

  implicit lazy val arbitraryConsignorIdentityPage: Arbitrary[ConsignorIdentityPage] =
    Arbitrary(consignors.ConsignorIdentityPage(Index(0)))

  implicit lazy val arbitraryDangerousGoodCodePage: Arbitrary[DangerousGoodCodePage] =
    Arbitrary(goods.DangerousGoodCodePage(Index(0)))

  implicit lazy val arbitraryDangerousGoodPage: Arbitrary[DangerousGoodPage] =
    Arbitrary(goods.DangerousGoodPage(Index(0)))

  implicit lazy val arbitraryGoodsItemCrnKnownPage: Arbitrary[GoodsItemCrnKnownPage] =
    Arbitrary(goods.GoodsItemCrnKnownPage(Index(0)))

  implicit lazy val arbitraryGoodsItemCrnPage: Arbitrary[GoodsItemCrnPage] =
    Arbitrary(goods.GoodsItemCrnPage(Index(0)))

  implicit lazy val arbitraryDocumentPage: Arbitrary[DocumentPage] =
    Arbitrary(goods.DocumentPage(Index(0), Index(0)))

  implicit lazy val arbitraryAddAnyDocumentsPage: Arbitrary[AddAnyDocumentsPage] =
    Arbitrary(goods.AddAnyDocumentsPage(Index(0)))

  implicit lazy val arbitraryNumberOfPiecesPage: Arbitrary[NumberOfPiecesPage] =
    Arbitrary(goods.NumberOfPiecesPage(Index(0), Index(0)))

  implicit lazy val arbitraryNumberOfPackagesPage: Arbitrary[NumberOfPackagesPage] =
    Arbitrary(goods.NumberOfPackagesPage(Index(0), Index(0)))

  implicit lazy val arbitraryMarkOrNumberPage: Arbitrary[MarkOrNumberPage] =
    Arbitrary(goods.MarkOrNumberPage(Index(0), Index(0)))

  implicit lazy val arbitraryKindOfPackagePage: Arbitrary[KindOfPackagePage] =
    Arbitrary(goods.KindOfPackagePage(Index(0), Index(0)))

  implicit lazy val arbitraryAddPackagePage: Arbitrary[AddPackagePage] =
    Arbitrary(goods.AddPackagePage(Index(0)))

  implicit lazy val arbitraryAddMarkOrNumberPage: Arbitrary[AddMarkOrNumberPage] =
    Arbitrary(goods.AddMarkOrNumberPage(Index(0), Index(0)))

  implicit lazy val arbitraryDeclarationPlacePage: Arbitrary[DeclarationPlacePage.type] =
    Arbitrary(DeclarationPlacePage)

  implicit lazy val arbitraryGoodsDescriptionPage: Arbitrary[GoodsDescriptionPage] =
    Arbitrary(goods.GoodsDescriptionPage(Index(0)))

  implicit lazy val arbitraryCommodityCodeKnownPage: Arbitrary[CommodityCodeKnownPage] =
    Arbitrary(goods.CommodityCodeKnownPage(Index(0)))

  implicit lazy val arbitraryCommodityCodePage: Arbitrary[CommodityCodePage] =
    Arbitrary(goods.CommodityCodePage(Index(0)))

  implicit lazy val arbitraryGoodsPassThroughOtherCountriesPage
    : Arbitrary[GoodsPassThroughOtherCountriesPage.type] =
    Arbitrary(GoodsPassThroughOtherCountriesPage)

  implicit lazy val arbitraryCountryEnRoutePage: Arbitrary[CountryEnRoutePage] =
    Arbitrary(routedetails.CountryEnRoutePage(Index(0)))

  implicit lazy val arbitraryAddCountryEnRoutePage: Arbitrary[AddCountryEnRoutePage.type] =
    Arbitrary(AddCountryEnRoutePage)

  implicit lazy val arbitraryCustomsOfficeOfFirstEntryPage
    : Arbitrary[CustomsOfficeOfFirstEntryPage.type] =
    Arbitrary(CustomsOfficeOfFirstEntryPage)

  implicit lazy val arbitraryCountryOfDeparturePage: Arbitrary[CountryOfDeparturePage.type] =
    Arbitrary(CountryOfDeparturePage)

  implicit lazy val arbitraryArrivalDateAndTimePage: Arbitrary[ArrivalDateAndTimePage.type] =
    Arbitrary(ArrivalDateAndTimePage)

  implicit lazy val arbitraryCarrierEORIPage: Arbitrary[CarrierEORIPage.type] =
    Arbitrary(CarrierEORIPage)

  implicit lazy val arbitraryTransportModePage: Arbitrary[TransportModePage.type] =
    Arbitrary(TransportModePage)

  implicit lazy val arbitraryTotalGrossWeightPage: Arbitrary[TotalGrossWeightPage.type] =
    Arbitrary(TotalGrossWeightPage)

  implicit lazy val arbitraryGrossWeightPage: Arbitrary[ProvideGrossWeightPage.type] =
    Arbitrary(ProvideGrossWeightPage)

  implicit lazy val arbitraryLodgingPersonTypePage: Arbitrary[LodgingPersonTypePage.type] =
    Arbitrary(LodgingPersonTypePage)

  implicit lazy val arbitraryLocalReferenceNumberPage: Arbitrary[LocalReferenceNumberPage.type] =
    Arbitrary(LocalReferenceNumberPage)
}
