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
import models.DocumentType
import org.scalacheck.Gen
import play.api.data.FormError

class OverallDocumentFormProviderSpec extends StringFieldBehaviours {

  private val form = new OverallDocumentFormProvider()()

  ".type" - {

    val fieldName = "type"
    val requiredKey = "overallDocument.error.type.required"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      Gen.oneOf(DocumentType.allDocumentTypes) map { _.code }
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }

  ".reference" - {

    val fieldName = "reference"
    val requiredKey = "overallDocument.error.reference.required"
    val lengthKey = "overallDocument.error.reference.length"
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
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
