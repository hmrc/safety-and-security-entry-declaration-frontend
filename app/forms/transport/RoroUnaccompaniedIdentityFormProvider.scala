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
import models.TransportIdentity.RoroUnaccompaniedIdentity
import play.api.data.Form
import play.api.data.Forms._

class RoroUnaccompaniedIdentityFormProvider @Inject() extends Mappings {

  private val validAlphaNumeric = "[A-Za-z0-9]+"
  private val validNumber = "[0-9]+"

  def apply(): Form[RoroUnaccompaniedIdentity] = Form(
    mapping(
      "trailerNumber" -> text("roroUnaccompaniedIdentity.error.trailerNumber.required")
        .verifying(
          firstError(
            maxLength(17, "roroUnaccompaniedIdentity.error.trailerNumber.length"),
            regexp(validAlphaNumeric, "roroUnaccompaniedIdentity.error.trailerNumber.invalid")
          )
        ),
      "imo" -> text("roroUnaccompaniedIdentity.error.imo.required")
        .verifying(
         firstError(
           maxLength(8, "roroUnaccompaniedIdentity.error.imo.length"),
           regexp(validNumber, "roroUnaccompaniedIdentity.error.imo.invalid")
         )
        ),
      "ferryCompany" -> optional(nonEmptyText)
        .verifying("roroUnaccompaniedIdentity.error.ferryCompany.length", _.forall(_.length <= 35))
        .transform[Option[String]](v => v.filterNot { _ == "" }, identity)
    )(RoroUnaccompaniedIdentity.apply)(RoroUnaccompaniedIdentity.unapply)
  )
}
