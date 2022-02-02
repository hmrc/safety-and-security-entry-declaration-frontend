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

package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class TransportModeSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "TransportMode" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(TransportMode.values.toSeq)

      forAll(gen) {
        transportMode =>

          JsString(transportMode.toString).validate[TransportMode].asOpt.value mustEqual transportMode
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!TransportMode.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[TransportMode] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(TransportMode.values.toSeq)

      forAll(gen) {
        transportMode =>

          Json.toJson(transportMode) mustEqual JsString(transportMode.toString)
      }
    }
  }
}
