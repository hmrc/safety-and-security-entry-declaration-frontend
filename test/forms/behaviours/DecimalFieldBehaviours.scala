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

package forms.behaviours

import org.scalacheck.Gen
import play.api.data.{Form, FormError}

class DecimalFieldBehaviours extends FieldBehaviours {

  def decimalField(
                    form: Form[_],
                    fieldName: String,
                    nonNumericError: FormError,
                    invalidPrecisionError: FormError,
                    precision: Int
                  ): Unit = {

    "must not bind non-numeric values" in {

      forAll(nonNumerics) {
        nonNumeric =>
          val result = form.bind(Map(fieldName -> nonNumeric)).apply(fieldName)
          result.errors must contain only nonNumericError
      }
    }

    "must not bind decimals with too high a precision" in {

      val gen = for {
        highPrecision <- Gen.choose(precision + 1, precision * 2 + 1)
        digits        <- Gen.listOfN(highPrecision, Gen.numChar)
      } yield "1." + digits.mkString

      forAll(gen) {
        number =>
          val result = form.bind(Map(fieldName -> number)).apply(fieldName)
          result.errors must contain only invalidPrecisionError
      }
    }
  }

  def decimalWithRange(
                        form: Form[_],
                        fieldName: String,
                        minimum: BigDecimal,
                        maximum: BigDecimal,
                        precision: Int,
                        expectedError: FormError
                      ): Unit = {

    s"must not bind decimals outside the range $minimum to $maximum" in {

      forAll(decimalsOutsideRange(minimum, maximum, precision)) {
        number =>
          val result = form.bind(Map(fieldName -> number.toString)).apply(fieldName)
          result.errors must contain only expectedError
      }
    }
  }
}
