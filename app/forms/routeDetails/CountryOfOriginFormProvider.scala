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

package forms.routeDetails

import forms.mappings.Mappings
import models.Country
import models.Country.internationalCountries
import play.api.data.Form

import javax.inject.Inject

class CountryOfOriginFormProvider @Inject() extends Mappings {

  def apply(): Form[Country] =
    Form(
      "value" -> text("countryOfOrigin.error.required")
        .verifying(
          "countryOfOrigin.error.required",
          value => internationalCountries.exists(_.code == value)
        )
        .transform[Country](value => internationalCountries.find(_.code == value).get, _.code)
    )
}