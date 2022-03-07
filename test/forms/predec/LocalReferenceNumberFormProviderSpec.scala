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

import forms.behaviours.StringFieldBehaviours
import models.LocalReferenceNumber
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import play.api.data.FormError

class LocalReferenceNumberFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "localReferenceNumber.error.required"
  val lengthKey = "localReferenceNumber.error.length"
  val invalidKey = "localReferenceNumber.error.invalid"
  val maxLength = 22

  val form = new LocalReferenceNumberFormProvider()()

  ".value" - {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      arbitrary[LocalReferenceNumber].map(_.value)
    )

    "must not bind invalid data" in {

      val invalidData = for {
        noValidChars <- Gen.choose(0, 21)
        validChars <- Gen.listOfN(noValidChars, Gen.alphaNumChar)
        invalidChar <- Gen.oneOf('?', '.', ',')
      } yield (validChars :+ invalidChar).mkString

      forAll(invalidData) { invalidAnswer =>
        val result = form.bind(Map(fieldName -> invalidAnswer)).apply(fieldName)
        result.errors must contain only FormError(fieldName, invalidKey, Seq("[A-Za-z0-9]+"))
      }
    }

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
