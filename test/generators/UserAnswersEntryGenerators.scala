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
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryIdentifyCarrierUserAnswersEntry: Arbitrary[(IdentifyCarrierPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[IdentifyCarrierPage.type]
        value <- arbitrary[IdentifyCarrier].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryTransportModeUserAnswersEntry: Arbitrary[(TransportModePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[TransportModePage.type]
        value <- arbitrary[TransportMode].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryTotalGrossWeightUserAnswersEntry: Arbitrary[(TotalGrossWeightPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[TotalGrossWeightPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGrossWeightUserAnswersEntry: Arbitrary[(GrossWeightPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GrossWeightPage.type]
        value <- arbitrary[GrossWeight].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryLodgingPersonTypeUserAnswersEntry: Arbitrary[(LodgingPersonTypePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[LodgingPersonTypePage.type]
        value <- arbitrary[LodgingPersonType].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryLocalReferenceNumberUserAnswersEntry: Arbitrary[(LocalReferenceNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[LocalReferenceNumberPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }
}
