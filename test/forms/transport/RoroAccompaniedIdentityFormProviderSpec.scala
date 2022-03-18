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

package forms.transport

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class RoroAccompaniedIdentityFormProviderSpec extends StringFieldBehaviours {

  val form = new RoroAccompaniedIdentityFormProvider()()

  ".vehicleRegistrationNumber" - {

    val fieldName = "vehicleRegistrationNumber"
    val requiredKey = "roroAccompaniedIdentity.error.vehicleRegistrationNumber.required"
    val lengthKey = "roroAccompaniedIdentity.error.vehicleRegistrationNumber.length"
    val maxLength = 13

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }

  ".trailerNumber" - {

    val fieldName = "trailerNumber"
    val requiredKey = "roroAccompaniedIdentity.error.trailerNumber.required"
    val lengthKey = "roroAccompaniedIdentity.error.trailerNumber.length"
    val maxLength = 13

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }

  ".ferryCompany" - {
    val fieldName = "ferryCompany"
    val lengthKey = "roroAccompaniedIdentity.error.ferryCompany.length"
    val maxLength = 35

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Nil)
    )
  }
}
