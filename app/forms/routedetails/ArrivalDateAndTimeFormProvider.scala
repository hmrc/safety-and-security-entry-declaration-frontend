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

import forms.mappings.Mappings
import models.ArrivalDateAndTime
import play.api.data.Form
import play.api.data.Forms._

import javax.inject.Inject

class ArrivalDateAndTimeFormProvider @Inject() extends Mappings {

  def apply(): Form[ArrivalDateAndTime] =
    Form(
      mapping(
        "date" -> localDate(
          invalidKey = "arrivalDateAndTime.date.error.invalid",
          allRequiredKey = "arrivalDateAndTime.date.error.required.all",
          twoRequiredKey = "arrivalDateAndTime.date.error.required.two",
          requiredKey = "arrivalDateAndTime.date.error.required"
        ),
        "time" -> localTime(
          invalidKey = "arrivalDateAndTime.time.error.invalid",
          allRequiredKey = "arrivalDateAndTime.time.error.required.all",
          requiredKey = "arrivalDateAndTime.time.error.required"
        )
      )(ArrivalDateAndTime.apply)(ArrivalDateAndTime.unapply)
    )
}
