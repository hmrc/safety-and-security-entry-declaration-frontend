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

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms._
import models.AirIdentity

class AirIdentityFormProvider @Inject() extends Mappings {
  // IATA format is 3 alphanumeric + 4 numeric + optional single letter suffix
  private val expectedPattern = "^[A-Za-z0-9]{3}[0-9]{4}[A-za-z]?$"

  def apply(): Form[AirIdentity] = Form(
    mapping(
     "flightNumber" -> text("airIdentity.error.flightNumber.required")
       .verifying(regexp(expectedPattern, "airIdentity.error.flightNumber.format"))
    )(AirIdentity.apply)(AirIdentity.unapply)
  )
}
