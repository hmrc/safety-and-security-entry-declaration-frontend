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

package forms.preDeclaration

import forms.behaviours.DecimalFieldBehaviours
import org.scalacheck.Gen
import play.api.data.FormError

import scala.math.BigDecimal.RoundingMode

class TotalGrossWeightFormProviderSpec extends DecimalFieldBehaviours {

  val form = new TotalGrossWeightFormProvider()()

  ".value" - {

    val fieldName = "value"

    val minimum = BigDecimal(0.001)
    val maximum = BigDecimal(99999999.999)
    val precision = 3

    val validDataGenerator = Gen
      .choose(minimum, maximum)
      .map(_.setScale(precision, RoundingMode.HALF_DOWN))
      .map(_.toString)

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validDataGenerator
    )

    behave like decimalField(
      form,
      fieldName,
      nonNumericError = FormError(fieldName, "totalGrossWeight.error.nonNumeric"),
      invalidPrecisionError = FormError(fieldName, "totalGrossWeight.error.precision"),
      precision = precision
    )

    behave like decimalWithRange(
      form,
      fieldName,
      minimum = minimum,
      maximum = maximum,
      expectedError =
        FormError(fieldName, "totalGrossWeight.error.outOfRange", Seq(minimum, maximum)),
      precision = precision
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, "totalGrossWeight.error.required")
    )
  }
}
