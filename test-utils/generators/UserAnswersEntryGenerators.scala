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

import models._
import models.TransportIdentity._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages.consignees._
import pages.consignors._
import pages.goods._
import pages.predec._
import pages.routedetails._
import pages.transport._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryShippingContainersUserAnswersEntry: Arbitrary[(AnyShippingContainersPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AnyShippingContainersPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryItemContainerNumberUserAnswersEntry: Arbitrary[(ItemContainerNumberPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ItemContainerNumberPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNotifiedPartyUserAnswersEntry: Arbitrary[(NotifiedPartyPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[NotifiedPartyPage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryLoadingPlaceUserAnswersEntry: Arbitrary[(LoadingPlacePage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[LoadingPlacePage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsignorUserAnswersEntry: Arbitrary[(ConsignorPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ConsignorPage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeKnownUserAnswersEntry: Arbitrary[(ConsigneeKnownPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ConsigneeKnownPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeUserAnswersEntry: Arbitrary[(ConsigneePage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ConsigneePage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitrarySealUserAnswersEntry: Arbitrary[(SealPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[SealPage]
        value <- arbitrary[String].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRoroUnaccompaniedIdentityUserAnswersEntry: Arbitrary[(RoroUnaccompaniedIdentityPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RoroUnaccompaniedIdentityPage.type]
        value <- arbitrary[RoroUnaccompaniedIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRoroAccompaniedIdentityUserAnswersEntry: Arbitrary[(RoroAccompaniedIdentityPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RoroAccompaniedIdentityPage.type]
        value <- arbitrary[RoroAccompaniedIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRoadIdentityUserAnswersEntry: Arbitrary[(RoadIdentityPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RoadIdentityPage.type]
        value <- arbitrary[RoadIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRemoveSealUserAnswersEntry: Arbitrary[(RemoveSealPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RemoveSealPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRemoveOverallDocumentUserAnswersEntry: Arbitrary[(RemoveOverallDocumentPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RemoveOverallDocumentPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRailIdentityUserAnswersEntry: Arbitrary[(RailIdentityPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RailIdentityPage.type]
        value <- arbitrary[RailIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryOverallDocumentUserAnswersEntry: Arbitrary[(OverallDocumentPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[OverallDocumentPage]
        value <- arbitrary[Document].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNationalityOfTransportUserAnswersEntry: Arbitrary[(NationalityOfTransportPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[NationalityOfTransportPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMaritimeIdentityUserAnswersEntry: Arbitrary[(MaritimeIdentityPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[MaritimeIdentityPage.type]
        value <- arbitrary[MaritimeIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAnyOverallDocumentsUserAnswersEntry: Arbitrary[(AnyOverallDocumentsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AnyOverallDocumentsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAirIdentityUserAnswersEntry: Arbitrary[(AirIdentityPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AirIdentityPage.type]
        value <- arbitrary[AirIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddSealUserAnswersEntry: Arbitrary[(AddSealPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddSealPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddOverallDocumentUserAnswersEntry: Arbitrary[(AddOverallDocumentPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddOverallDocumentPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddAnySealsUserAnswersEntry: Arbitrary[(AddAnySealsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddAnySealsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPlaceOfUnloadingUserAnswersEntry: Arbitrary[(PlaceOfUnloadingPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PlaceOfUnloadingPage]
        value <- arbitrary[PlaceOfUnloading].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPlaceOfLoadingUserAnswersEntry: Arbitrary[(PlaceOfLoadingPage, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PlaceOfLoadingPage]
        value <- arbitrary[PlaceOfLoading].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddAnyNotifiedPartiesUserAnswersEntry: Arbitrary[(AddAnyNotifiedPartiesPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddAnyNotifiedPartiesPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPaymentMethodUserAnswersEntry
    : Arbitrary[(PaymentMethodPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[PaymentMethodPage]
        value <- arbitrary[PaymentMethod].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNotifiedPartyNameUserAnswersEntry
    : Arbitrary[(NotifiedPartyNamePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NotifiedPartyNamePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNotifiedPartyIdentityUserAnswersEntry
    : Arbitrary[(NotifiedPartyIdentityPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NotifiedPartyIdentityPage]
        value <- arbitrary[NotifiedPartyIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNotifiedPartyEORIUserAnswersEntry
    : Arbitrary[(NotifiedPartyEORIPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NotifiedPartyEORIPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNotifiedPartyAddressUserAnswersEntry
    : Arbitrary[(NotifiedPartyAddressPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NotifiedPartyAddressPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeNameUserAnswersEntry
    : Arbitrary[(ConsigneeNamePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsigneeNamePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeIdentityUserAnswersEntry
    : Arbitrary[(ConsigneeIdentityPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsigneeIdentityPage]
        value <- arbitrary[ConsigneeIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeEORIUserAnswersEntry
    : Arbitrary[(ConsigneeEORIPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsigneeEORIPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsigneeAddressUserAnswersEntry
    : Arbitrary[(ConsigneeAddressPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsigneeAddressPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsignorNameUserAnswersEntry
    : Arbitrary[(ConsignorNamePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsignorNamePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsignorEORIUserAnswersEntry
    : Arbitrary[(ConsignorEORIPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsignorEORIPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsignorAddressUserAnswersEntry
    : Arbitrary[(ConsignorAddressPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsignorAddressPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAnyConsigneesKnownUserAnswersEntry
    : Arbitrary[(AnyConsigneesKnownPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[AnyConsigneesKnownPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGoodsItemGrossWeightUserAnswersEntry
    : Arbitrary[(GoodsItemGrossWeightPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[GoodsItemGrossWeightPage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsignorIdentityUserAnswersEntry
    : Arbitrary[(ConsignorIdentityPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ConsignorIdentityPage]
        value <- arbitrary[ConsignorIdentity].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDangerousGoodCodeUserAnswersEntry
    : Arbitrary[(DangerousGoodCodePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[DangerousGoodCodePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDangerousGoodUserAnswersEntry
    : Arbitrary[(DangerousGoodPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[DangerousGoodPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDocumentUserAnswersEntry: Arbitrary[(DocumentPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[DocumentPage]
        value <- arbitrary[Document].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddAnyDocumentsUserAnswersEntry
    : Arbitrary[(AddAnyDocumentsPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[AddAnyDocumentsPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNumberOfPiecesUserAnswersEntry
    : Arbitrary[(NumberOfPiecesPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NumberOfPiecesPage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNumberOfPackagesUserAnswersEntry
    : Arbitrary[(NumberOfPackagesPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[NumberOfPackagesPage]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryMarkOrNumberUserAnswersEntry: Arbitrary[(MarkOrNumberPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[MarkOrNumberPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryKindOfPackageUserAnswersEntry
    : Arbitrary[(KindOfPackagePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[KindOfPackagePage]
        value <- arbitrary[KindOfPackage].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddMarkOrNumberUserAnswersEntry
    : Arbitrary[(AddMarkOrNumberPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[AddMarkOrNumberPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDeclarationPlaceUserAnswersEntry
    : Arbitrary[(DeclarationPlacePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[DeclarationPlacePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGoodsDescriptionUserAnswersEntry
    : Arbitrary[(GoodsDescriptionPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[GoodsDescriptionPage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCommodityCodeKnownUserAnswersEntry
    : Arbitrary[(CommodityCodeKnownPage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CommodityCodeKnownPage]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCommodityCodeUserAnswersEntry
    : Arbitrary[(CommodityCodePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CommodityCodePage]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGoodsPassThroughOtherCountriesUserAnswersEntry
    : Arbitrary[(GoodsPassThroughOtherCountriesPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[GoodsPassThroughOtherCountriesPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCountryEnRouteUserAnswersEntry
    : Arbitrary[(CountryEnRoutePage, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CountryEnRoutePage]
        value <- arbitrary[Country].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddCountryEnRouteUserAnswersEntry
    : Arbitrary[(AddCountryEnRoutePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[AddCountryEnRoutePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCustomsOfficeOfFirstEntryUserAnswersEntry
    : Arbitrary[(CustomsOfficeOfFirstEntryPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CustomsOfficeOfFirstEntryPage.type]
        value <- arbitrary[CustomsOffice].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCountryOfDepartureUserAnswersEntry
    : Arbitrary[(CountryOfDeparturePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CountryOfDeparturePage.type]
        value <- arbitrary[Country].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryArrivalDateAndTimeUserAnswersEntry
    : Arbitrary[(ArrivalDateAndTimePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ArrivalDateAndTimePage.type]
        value <- arbitrary[ArrivalDateAndTime].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCarrierEORIUserAnswersEntry
    : Arbitrary[(CarrierEORIPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[CarrierEORIPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryTransportModeUserAnswersEntry
    : Arbitrary[(TransportModePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[TransportModePage.type]
        value <- arbitrary[TransportMode].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryTotalGrossWeightUserAnswersEntry
    : Arbitrary[(TotalGrossWeightPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[TotalGrossWeightPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGrossWeightUserAnswersEntry
    : Arbitrary[(ProvideGrossWeightPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[ProvideGrossWeightPage.type]
        value <- arbitrary[ProvideGrossWeight].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryLodgingPersonTypeUserAnswersEntry
    : Arbitrary[(LodgingPersonTypePage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[LodgingPersonTypePage.type]
        value <- arbitrary[LodgingPersonType].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryLocalReferenceNumberUserAnswersEntry
    : Arbitrary[(LocalReferenceNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page <- arbitrary[LocalReferenceNumberPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }
}
