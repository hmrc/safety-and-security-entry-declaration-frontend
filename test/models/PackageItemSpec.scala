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

import base.SpecBase
import generators.Generators
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsSuccess, Json}

class PackageItemSpec extends SpecBase with ScalaCheckPropertyChecks with Generators {

  "A package item" - {

    "must deserialise" - {

      "from a bulk item" in {

        forAll(
          Gen.oneOf(KindOfPackage.bulkKindsOfPackage) -> "kind of package",
          stringsWithMaxLength(140) -> "mark or number"
        ) {
          case (kindOfPackage, markOrNumber) =>
            val json = Json.obj(
              "kindOfPackage" -> Json.toJson(kindOfPackage),
              "markOrNumber" -> markOrNumber
            )

            json.validate[PackageItem] mustEqual JsSuccess(
              BulkPackageItem(kindOfPackage, Some(markOrNumber))
            )
        }
      }

      "from an unpacked item" in {

        forAll(
          Gen.oneOf(KindOfPackage.unpackedKindsOfPackage) -> "kind of package",
          stringsWithMaxLength(140) -> "mark or number",
          Gen.choose(1, 99999) -> "number of pieces"
        ) {
          case (kindOfPackage, markOrNumber, numberOfPieces) =>
            val json = Json.obj(
              "kindOfPackage" -> Json.toJson(kindOfPackage),
              "numberOfPieces" -> numberOfPieces,
              "markOrNumber" -> markOrNumber
            )

            json.validate[PackageItem] mustEqual JsSuccess(
              UnpackedPackageItem(kindOfPackage, numberOfPieces, Some(markOrNumber))
            )
        }
      }

      "from a standard item" in {

        forAll(
          Gen.oneOf(KindOfPackage.standardKindsOfPackages) -> "kind of package",
          stringsWithMaxLength(140) -> "mark or number",
          Gen.choose(1, 99999) -> "number of pacakges"
        ) {
          case (kindOfPackage, markOrNumber, numberOfPackages) =>
            val json = Json.obj(
              "kindOfPackage" -> Json.toJson(kindOfPackage),
              "numberOfPackages" -> numberOfPackages,
              "markOrNumber" -> markOrNumber
            )

            json.validate[PackageItem] mustEqual JsSuccess(
              StandardPackageItem(kindOfPackage, numberOfPackages, markOrNumber)
            )
        }
      }
    }
  }
}
