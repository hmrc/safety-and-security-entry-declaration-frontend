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
import models.{Address, Country}
import models.Country.internationalCountries
import play.api.data.Form
import play.api.data.Forms.mapping

import javax.inject.Inject

class ConsignorAddressFormProvider @Inject() extends Mappings {
  def apply() : Form[Address] = Form(
    mapping(
      "streetAndNumber" -> text("address.streetAndNumber.error.required").verifying(maxLength(35,"address.streetAndNumber.error.length")),
      "city" -> text("address.city.error.required").verifying(maxLength(35,"address.city.error.length")),
      "postCode" -> text("address.postCode.error.required").verifying(maxLength(9,"address.postCode.error.length")),
      "country" -> text("address.country.error.required").verifying("address.country.error.required", value => internationalCountries.exists(_.code == value))
        .transform[Country](value => internationalCountries.find(_.code == value).get, _.code)
    )(Address.apply)(Address.unapply)
  )
}
