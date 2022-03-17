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

class AirIdentityFormProviderSpec extends StringFieldBehaviours {
  private val form = new AirIdentityFormProvider()()

  private val validValues: Gen[String] = {
    for {
      carrierCode <- Gen.listOfN(3, Gen.alphaNumChar)
      flightNumber <- Gen.listOfN(4, Gen.numChar)
      suffix <- Gen.option(Gen.alphaChar)
    } yield {
      (carrierCode ++ flightNumber ++ suffix).mkString
    }
  }

  private def requireError(answer: String, fieldName: String): Unit = {
    val result = form.bind(Map(fieldName -> answer)).apply(fieldName)
    result.errors must not have length(0)
  }

  ".flightNumber" - {
    val fieldName = "flightNumber"
    val requiredKey = "airIdentity.error.flightNumber.required"

    behave like fieldThatBindsValidData(form, fieldName, validValues)

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

    "should not allow alphabetical characters in the flight number" in {
      val values = for {
        carrierCode <- Gen.listOfN(3, Gen.alphaNumChar)
        flightNumber <- Gen.listOfN(4, Gen.alphaChar)
        suffix <- Gen.option(Gen.alphaChar)
      } yield {
        (carrierCode ++ flightNumber ++ suffix).mkString
      }

      forAll(values) { v => requireError(v, fieldName) }
    }

    "should not allow digits in the optional suffix" in {
      val values = for {
        carrierCode <- Gen.listOfN(3, Gen.alphaNumChar)
        flightNumber <- Gen.listOfN(4, Gen.numChar)
        suffix <- Gen.numChar
      } yield {
        (carrierCode ++ flightNumber :+ suffix).mkString
      }

      forAll(values) { v => requireError(v, fieldName) }
    }

    "should only allow one character after the flight number" in {
      val values = for {
        carrierCode <- Gen.listOfN(3, Gen.alphaNumChar)
        flightNumber <- Gen.listOfN(4, Gen.numChar)
        suffixLen <- Gen.choose(2, 10)
        suffix <- Gen.listOfN(suffixLen, Gen.alphaChar)
      } yield {
        (carrierCode ++ flightNumber ++ suffix).mkString
      }

      forAll(values) { v => requireError(v, fieldName) }
    }
  }
}
