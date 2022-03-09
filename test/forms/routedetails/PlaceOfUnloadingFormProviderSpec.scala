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

package forms.routedetails

import forms.behaviours.StringFieldBehaviours
import models.{Country, PlaceOfUnloading}
import org.scalacheck.Arbitrary.arbitrary
import play.api.data.FormError

class PlaceOfUnloadingFormProviderSpec extends StringFieldBehaviours {

  val placeMaxLength = 32
  val id = 123
  val form = new PlaceOfUnloadingFormProvider()(id)

  "must bind using the provided id" in {

    val country = arbitrary[Country].sample.value
    val place   = stringsWithMaxLength(placeMaxLength).sample.value

    val data = Map(
      "country" -> country.code,
      "place" -> place
    )

    val result = form.bind(data)
    result.errors mustBe empty
    result.value.value mustEqual PlaceOfUnloading(id, country, place)
  }

  ".country" - {

    val fieldName = "country"
    val requiredKey = "placeOfUnloading.error.country.required"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      arbitrary[Country].map(_.code)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

    "must not bind any values other than valid country codes" in {

      val invalidAnswers =
        arbitrary[String].suchThat(x => !Country.allCountries.map(_.code).contains(x))

      forAll(invalidAnswers) { answer =>
        val result = form.bind(Map("value" -> answer)).apply(fieldName)
        result.errors must contain only FormError(fieldName, requiredKey)
      }
    }
  }

  ".place" - {

    val fieldName = "place"
    val requiredKey = "placeOfUnloading.error.place.required"
    val lengthKey = "placeOfUnloading.error.place.length"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(placeMaxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = placeMaxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(placeMaxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
