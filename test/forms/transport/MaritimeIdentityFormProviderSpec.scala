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
import org.scalacheck.Gen
import play.api.data.FormError

class MaritimeIdentityFormProviderSpec extends StringFieldBehaviours {

  val form = new MaritimeIdentityFormProvider()()

  ".imo" - {

    val fieldName = "imo"
    val requiredKey = "maritimeIdentity.error.imo.required"
    val lengthKey = "maritimeIdentity.error.imo.length"
    val invalidKey = "maritimeIdentity.error.imo.invalid"
    val maxLength = 8

    val validBinding = for {
      len <- Gen.choose(1, maxLength)
      value <- Gen.listOfN(len, Gen.numChar)
    } yield value.mkString

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validBinding
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

    "must not bind invalid (alphabetical characters) characters data" in {

      val invalidData = for {
        noValidChars <- Gen.choose(0, maxLength - 1)
        validChars <- Gen.listOfN(noValidChars, Gen.numChar)
        invalidChar <- Gen.alphaChar
      } yield (validChars :+ invalidChar).mkString

      forAll(invalidData) { invalidAnswer =>
        val result = form.bind(Map(fieldName -> invalidAnswer)).apply(fieldName)
        result.errors must contain only FormError(fieldName, invalidKey, Seq("[0-9]+"))
      }
    }

    "must not bind invalid (symbol characters) data" in {

      val invalidData = for {
        noValidChars <- Gen.choose(0, maxLength - 1)
        validChars <- Gen.listOfN(noValidChars, Gen.numChar)
        invalidChar <- Gen.oneOf('?', '.', ',')
      } yield (validChars :+ invalidChar).mkString

      forAll(invalidData) { invalidAnswer =>
        val result = form.bind(Map(fieldName -> invalidAnswer)).apply(fieldName)
        result.errors must contain only FormError(fieldName, invalidKey, Seq("[0-9]+"))
      }
    }
  }

  ".conveyanceRefNum" - {

    val fieldName = "conveyanceRefNum"
    val requiredKey = "maritimeIdentity.error.conveyanceRefNum.required"
    val lengthKey = "maritimeIdentity.error.conveyanceRefNum.length"
    val invalidKey = "maritimeIdentity.error.conveyanceRefNum.invalid"
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

    "must not bind invalid data" in {

      val invalidData = for {
        noValidChars <- Gen.choose(0, maxLength - 1)
        validChars <- Gen.listOfN(noValidChars, Gen.alphaNumChar)
        invalidChar <- Gen.oneOf('?', '.', ',')
      } yield (validChars :+ invalidChar).mkString

      forAll(invalidData) { invalidAnswer =>
        val result = form.bind(Map(fieldName -> invalidAnswer)).apply(fieldName)
        result.errors must contain only FormError(fieldName, invalidKey, Seq("[A-Za-z0-9]+"))
      }
    }
  }
}
