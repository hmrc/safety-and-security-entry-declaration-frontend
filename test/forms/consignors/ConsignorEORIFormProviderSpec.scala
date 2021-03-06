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

package forms.consignors

import forms.behaviours.GbEoriFieldBehaviours
import play.api.data.FormError

class ConsignorEORIFormProviderSpec extends GbEoriFieldBehaviours {

  val requiredKey = "consignorEORI.error.required"
  val lengthKey = "consignorEORI.error.length"
  val maxLength = 50

  val form = new ConsignorEORIFormProvider()()

  ".value" - {

    val fieldName = "value"

    behave like gbEoriField(form, fieldName)

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
