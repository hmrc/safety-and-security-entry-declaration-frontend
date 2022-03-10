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
import models.Country.allCountries
import play.api.data.Form
import play.api.data.Forms._
import models.{Country, PlaceOfUnloading}

class PlaceOfUnloadingFormProvider @Inject() extends Mappings {

  def apply(key: Int): Form[PlaceOfUnloading] = Form(
    mapping(
      "country" -> text("placeOfUnloading.error.country.required")
        .verifying("placeOfUnloading.error.country.required", x => allCountries.exists(_.code == x))
        .transform[Country](x => allCountries.find(_.code == x).get, _.code),
      "place" -> text("placeOfUnloading.error.place.required")
        .verifying(maxLength(32, "placeOfUnloading.error.place.length"))
    )(PlaceOfUnloading(key, _, _))(p => Some(p.country, p.place))
  )
}
