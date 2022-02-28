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
import pages.{routeDetails, _}
import pages.consignees._
import pages.routeDetails._

trait PageGenerators {
  implicit lazy val arbitraryCarrierPaymentMethodPage: Arbitrary[CarrierPaymentMethodPage] =
    Arbitrary(CarrierPaymentMethodPage(Index(0)))

  implicit lazy val arbitraryAddPaymentMethodPage: Arbitrary[AddPaymentMethodPage] =
    Arbitrary(AddPaymentMethodPage(Index(0)))

  implicit lazy val arbitraryUnloadingCodePage: Arbitrary[UnloadingCodePage] =
    Arbitrary(UnloadingCodePage(Index(0)))

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
    Arbitrary(ConsignorNamePage(Index(0)))

  implicit lazy val arbitraryConsignorEORIPage: Arbitrary[ConsignorEORIPage] =
    Arbitrary(ConsignorEORIPage(Index(0)))

  implicit lazy val arbitraryConsignorAddressPage: Arbitrary[ConsignorAddressPage] =
    Arbitrary(ConsignorAddressPage(Index(0)))

  implicit lazy val arbitraryConsigneeKnownPage: Arbitrary[ConsigneeKnownPage] =
    Arbitrary(consignees.ConsigneeKnownPage(Index(0)))

  implicit lazy val arbitraryGoodsItemGrossWeightPage: Arbitrary[GoodsItemGrossWeightPage] =
    Arbitrary(GoodsItemGrossWeightPage(Index(0)))

  implicit lazy val arbitraryConsignorsIdentityPage: Arbitrary[ConsignorsIdentityPage] =
    Arbitrary(ConsignorsIdentityPage(Index(0)))

  implicit lazy val arbitraryDangerousGoodCodePage: Arbitrary[DangerousGoodCodePage] =
    Arbitrary(DangerousGoodCodePage(Index(0)))

  implicit lazy val arbitraryDangerousGoodPage: Arbitrary[DangerousGoodPage] =
    Arbitrary(DangerousGoodPage(Index(0)))

  implicit lazy val arbitraryOverallCrnKnownPage: Arbitrary[OverallCrnKnownPage.type] =
    Arbitrary(OverallCrnKnownPage)

  implicit lazy val arbitraryOverallCrnPage: Arbitrary[OverallCrnPage.type] =
    Arbitrary(OverallCrnPage)

  implicit lazy val arbitraryGoodsItemCrnKnownPage: Arbitrary[GoodsItemCrnKnownPage] =
    Arbitrary(GoodsItemCrnKnownPage(Index(0)))

  implicit lazy val arbitraryGoodsItemCrnPage: Arbitrary[GoodsItemCrnPage] =
    Arbitrary(GoodsItemCrnPage(Index(0)))

  implicit lazy val arbitraryDocumentPage: Arbitrary[DocumentPage] =
    Arbitrary(DocumentPage(Index(0), Index(0)))

  implicit lazy val arbitraryAddAnyDocumentsPage: Arbitrary[AddAnyDocumentsPage] =
    Arbitrary(AddAnyDocumentsPage(Index(0)))

  implicit lazy val arbitraryNumberOfPiecesPage: Arbitrary[NumberOfPiecesPage] =
    Arbitrary(NumberOfPiecesPage(Index(0), Index(0)))

  implicit lazy val arbitraryNumberOfPackagesPage: Arbitrary[NumberOfPackagesPage] =
    Arbitrary(NumberOfPackagesPage(Index(0), Index(0)))

  implicit lazy val arbitraryMarkOrNumberPage: Arbitrary[MarkOrNumberPage] =
    Arbitrary(MarkOrNumberPage(Index(0), Index(0)))

  implicit lazy val arbitraryKindOfPackagePage: Arbitrary[KindOfPackagePage] =
    Arbitrary(KindOfPackagePage(Index(0), Index(0)))

  implicit lazy val arbitraryAddPackagePage: Arbitrary[AddPackagePage] =
    Arbitrary(AddPackagePage(Index(0)))

  implicit lazy val arbitraryAddMarkOrNumberPage: Arbitrary[AddMarkOrNumberPage] =
    Arbitrary(AddMarkOrNumberPage(Index(0), Index(0)))

  implicit lazy val arbitraryDeclarationPlacePage: Arbitrary[DeclarationPlacePage.type] =
    Arbitrary(DeclarationPlacePage)

  implicit lazy val arbitraryGoodsDescriptionPage: Arbitrary[GoodsDescriptionPage] =
    Arbitrary(GoodsDescriptionPage(Index(0)))

  implicit lazy val arbitraryCommodityCodeKnownPage: Arbitrary[CommodityCodeKnownPage] =
    Arbitrary(CommodityCodeKnownPage(Index(0)))

  implicit lazy val arbitraryCommodityCodePage: Arbitrary[CommodityCodePage] =
    Arbitrary(CommodityCodePage(Index(0)))

  implicit lazy val arbitraryRemoveCountryEnRoutePage: Arbitrary[RemoveCountryEnRoutePage] =
    Arbitrary(routeDetails.RemoveCountryEnRoutePage(Index(0)))

  implicit lazy val arbitraryGoodsPassThroughOtherCountriesPage
    : Arbitrary[GoodsPassThroughOtherCountriesPage.type] =
    Arbitrary(GoodsPassThroughOtherCountriesPage)

  implicit lazy val arbitraryCountryEnRoutePage: Arbitrary[CountryEnRoutePage] =
    Arbitrary(routeDetails.CountryEnRoutePage(Index(0)))

  implicit lazy val arbitraryAddCountryEnRoutePage: Arbitrary[AddCountryEnRoutePage.type] =
    Arbitrary(AddCountryEnRoutePage)

  implicit lazy val arbitraryCustomsOfficeOfFirstEntryPage
    : Arbitrary[CustomsOfficeOfFirstEntryPage.type] =
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
