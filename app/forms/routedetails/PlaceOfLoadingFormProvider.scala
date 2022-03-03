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

package forms.routedetails

import javax.inject.Inject
import forms.mappings.Mappings
import models.Country.internationalCountries
import play.api.data.Form
import play.api.data.Forms._
import models.{Country, PlaceOfLoading}

class PlaceOfLoadingFormProvider @Inject() extends Mappings {

  def apply(): Form[PlaceOfLoading] = Form(
    mapping(
      "country" -> text("placeOfLoading.error.country.required")
        .verifying("placeOfLoading.error.country.required", x => internationalCountries.exists(_.code == x))
        .transform[Country](x => internationalCountries.find(_.code == x).get, _.code),
      "place" -> text("placeOfLoading.error.place.required")
        .verifying(maxLength(32, "placeOfLoading.error.place.length"))
    )(PlaceOfLoading.apply)(PlaceOfLoading.unapply)
  )
}
