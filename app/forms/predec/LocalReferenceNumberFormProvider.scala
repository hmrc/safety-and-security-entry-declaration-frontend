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

package forms.predec

import forms.mappings.Mappings
import models.LocalReferenceNumber
import play.api.data.Form

import javax.inject.Inject

class LocalReferenceNumberFormProvider @Inject() extends Mappings {

  private val validData = "[A-Za-z0-9]+"

  def apply(): Form[LocalReferenceNumber] =
    Form(
      "value" -> text("localReferenceNumber.error.required")
        .verifying(
          firstError(
            maxLength(22, "localReferenceNumber.error.length"),
            regexp(validData, "localReferenceNumber.error.invalid")
          )
        )
        .transform[LocalReferenceNumber](LocalReferenceNumber(_), _.value)
    )
}
