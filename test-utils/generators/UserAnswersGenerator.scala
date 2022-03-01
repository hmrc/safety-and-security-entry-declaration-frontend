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

import models.UserAnswers
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.TryValues
import pages._
import pages.consignees._
import pages.goods._
import pages.preDeclaration._
import pages.routeDetails._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersGenerator extends TryValues {
  self: Generators =>

  val generators: Seq[Gen[(QuestionPage[_], JsValue)]] =
    arbitrary[(CarrierPaymentMethodPage, JsValue)] ::
      arbitrary[(AddPaymentMethodPage, JsValue)] ::
      arbitrary[(UnloadingCodePage, JsValue)] ::
      arbitrary[(NotifiedPartyNamePage, JsValue)] ::
      arbitrary[(NotifiedPartyIdentityPage, JsValue)] ::
      arbitrary[(NotifiedPartyEORIPage, JsValue)] ::
      arbitrary[(NotifiedPartyAddressPage, JsValue)] ::
      arbitrary[(ConsigneeNamePage, JsValue)] ::
      arbitrary[(ConsigneeIdentityPage, JsValue)] ::
      arbitrary[(ConsigneeEORIPage, JsValue)] ::
      arbitrary[(ConsigneeAddressPage, JsValue)] ::
      arbitrary[(ConsignorNamePage, JsValue)] ::
      arbitrary[(ConsignorEORIPage, JsValue)] ::
      arbitrary[(ConsignorAddressPage, JsValue)] ::
      arbitrary[(ConsigneeKnownPage, JsValue)] ::
      arbitrary[(GoodsItemGrossWeightPage, JsValue)] ::
      arbitrary[(ConsignorsIdentityPage, JsValue)] ::
      arbitrary[(DangerousGoodCodePage, JsValue)] ::
      arbitrary[(DangerousGoodPage, JsValue)] ::
      arbitrary[(OverallCrnKnownPage.type, JsValue)] ::
      arbitrary[(OverallCrnPage.type, JsValue)] ::
      arbitrary[(GoodsItemCrnKnownPage, JsValue)] ::
      arbitrary[(GoodsItemCrnPage, JsValue)] ::
      arbitrary[(DocumentPage, JsValue)] ::
      arbitrary[(AddAnyDocumentsPage, JsValue)] ::
      arbitrary[(NumberOfPiecesPage, JsValue)] ::
      arbitrary[(NumberOfPackagesPage, JsValue)] ::
      arbitrary[(MarkOrNumberPage, JsValue)] ::
      arbitrary[(KindOfPackagePage, JsValue)] ::
      arbitrary[(AddMarkOrNumberPage, JsValue)] ::
      arbitrary[(DeclarationPlacePage.type, JsValue)] ::
      arbitrary[(GoodsDescriptionPage, JsValue)] ::
      arbitrary[(CommodityCodeKnownPage, JsValue)] ::
      arbitrary[(CommodityCodePage, JsValue)] ::
      arbitrary[(RemoveCountryEnRoutePage, JsValue)] ::
      arbitrary[(GoodsPassThroughOtherCountriesPage.type, JsValue)] ::
      arbitrary[(CountryEnRoutePage, JsValue)] ::
      arbitrary[(CustomsOfficeOfFirstEntryPage.type, JsValue)] ::
      arbitrary[(CountryOfOriginPage.type, JsValue)] ::
      arbitrary[(ArrivalDateAndTimePage.type, JsValue)] ::
      arbitrary[(CarriersEORIPage.type, JsValue)] ::
      arbitrary[(IdentifyCarrierPage.type, JsValue)] ::
      arbitrary[(TransportModePage.type, JsValue)] ::
      arbitrary[(TotalGrossWeightPage.type, JsValue)] ::
      arbitrary[(GrossWeightPage.type, JsValue)] ::
      arbitrary[(LodgingPersonTypePage.type, JsValue)] ::
      arbitrary[(LocalReferenceNumberPage.type, JsValue)] ::
      Nil

  implicit lazy val arbitraryUserData: Arbitrary[UserAnswers] = {

    import models._

    Arbitrary {
      for {
        id <- nonEmptyString
        data <- generators match {
          case Nil => Gen.const(Map[QuestionPage[_], JsValue]())
          case _ => Gen.mapOf(oneOf(generators))
        }
      } yield UserAnswers(
        id = id,
        lrn = LocalReferenceNumber("ABC123"),
        data = data.foldLeft(Json.obj()) {
          case (obj, (path, value)) =>
            obj.setObject(path.path, value).get
        }
      )
    }
  }
}
