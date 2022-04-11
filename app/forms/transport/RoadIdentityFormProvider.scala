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
import models.TransportIdentity.RoadIdentity
import play.api.data.Form
import play.api.data.Forms._

class RoadIdentityFormProvider @Inject() extends Mappings {
  private val alphanumericPattern = "[A-Za-z0-9]+"

  def apply(): Form[RoadIdentity] = Form(
    mapping(
      "vehicleRegistrationNumber" -> text("roadIdentity.error.vehicleRegistrationNumber.required")
        .verifying(
          firstError(
            maxLength(13, "roadIdentity.error.vehicleRegistrationNumber.length"),
            regexp(alphanumericPattern, "roadIdentity.error.vehicleRegistrationNumber.invalid")
          )
        ),
      "trailerNumber" -> text("roadIdentity.error.trailerNumber.required")
        .verifying(
          firstError(
            maxLength(13, "roadIdentity.error.trailerNumber.length"),
            regexp(alphanumericPattern, "roadIdentity.error.trailerNumber.invalid")
          )
        ),
      "ferryCompany" -> optional(nonEmptyText)
        .verifying("roadIdentity.error.ferryCompany.length", _.forall(_.length <= 35))
        .transform[Option[String]](v => v.filterNot { _ == "" }, identity)
    )(RoadIdentity.apply)(RoadIdentity.unapply)
  )
}
