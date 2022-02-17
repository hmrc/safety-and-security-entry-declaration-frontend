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

package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError


class AddressFormProviderSpec extends StringFieldBehaviours {
  val form = new AddressFormProvider()()

  ".streetAndNumber" - {
      val fieldName = "streetAndNumber"
      val lengthKey = "address.streetAndNumber.error.length"

      behave like fieldThatBindsValidData(
        form,
        fieldName,
        stringsWithMaxLength(35)
      )

      behave like fieldWithMaxLength(
        form,
        fieldName,
        maxLength = 35,
        lengthError = FormError(fieldName, lengthKey, Seq(35))
      )
  }

  ".city" - {
    val fieldName = "city"
    val lengthKey = "address.city.error.length"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(35)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = 35,
      lengthError = FormError(fieldName, lengthKey, Seq(35))
    )
  }

  ".postCode" - {
    val fieldName = "postCode"
    val lengthKey = "address.postCode.error.length"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(9)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = 35,
      lengthError = FormError(fieldName, lengthKey, Seq(9))
    )
  }

  ".country" - {
    val fieldName = "country"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithExactLength(2)
    )
  }

}
