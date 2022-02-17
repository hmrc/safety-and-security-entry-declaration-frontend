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

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class TotalGrossWeightFormProvider @Inject() extends Mappings {

  def apply(): Form[BigDecimal] =
    Form(
      "value" -> decimal(
        requiredKey         = "totalGrossWeight.error.required",
        nonNumericKey       = "totalGrossWeight.error.nonNumeric",
        invalidPrecisionKey = "totalGrossWeight.error.precision",
        precision           = 3
      ).verifying(inRange(BigDecimal(0.001), BigDecimal(99999999.999), "totalGrossWeight.error.outOfRange"))
    )
}
