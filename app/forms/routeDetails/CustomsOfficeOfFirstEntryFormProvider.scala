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
import models.CustomsOffice
import models.CustomsOffice.allCustomsOffices
import play.api.data.Form

import javax.inject.Inject

class CustomsOfficeOfFirstEntryFormProvider @Inject() extends Mappings {

  def apply(): Form[CustomsOffice] =
    Form(
      "value" -> text("customsOfficeOfFirstEntry.error.required")
        .verifying(
          "customsOfficeOfFirstEntry.error.required",
          value => allCustomsOffices.exists(_.code == value)
        )
        .transform[CustomsOffice](value => allCustomsOffices.find(_.code == value).get, _.code)
    )
}
