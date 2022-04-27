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

package forms.goods

import forms.behaviours.OptionFieldBehaviours
import models.PlaceOfUnloading
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import play.api.data.FormError

class UnloadingPlaceFormProviderSpec extends OptionFieldBehaviours {

  val formProvider = new UnloadingPlaceFormProvider()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "unloadingPlace.error.required"

    "must bind any values passed to the form" in {

      forAll(Gen.nonEmptyListOf(arbitrary[PlaceOfUnloading])) {
        placesOfUnloading =>

          val form = formProvider(placesOfUnloading.map(_.key))
          placesOfUnloading.map {
            placeOfUnloading =>
              val result = form.bind(Map("value" -> placeOfUnloading.key.toString))(fieldName)
              result.value.value mustEqual placeOfUnloading.key.toString
              result.errors mustBe empty
          }
      }
    }

    "must not bind any values not passed to the form" in {

      forAll(arbitrary[String], Gen.nonEmptyListOf(arbitrary[PlaceOfUnloading])) {
        case (invalidValue, placeOfUnloading) =>

          whenever(!placeOfUnloading.map(_.key.toString).contains(invalidValue)) {
            val form = formProvider(placeOfUnloading.map(_.key))
            val result = form.bind(Map("value" -> invalidValue))(fieldName)
            result.errors must contain only FormError(fieldName, requiredKey)
          }
      }
    }

    behave like mandatoryField(
      formProvider(List(1, 2, 3)),
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
