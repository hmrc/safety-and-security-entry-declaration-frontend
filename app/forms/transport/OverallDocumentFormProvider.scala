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
import models.OverallDocument

class OverallDocumentFormProvider @Inject() extends Mappings {

   def apply(): Form[OverallDocument] = Form(
     mapping(
      "field1" -> text("overallDocument.error.field1.required")
        .verifying(maxLength(100, "overallDocument.error.field1.length")),
      "field2" -> text("overallDocument.error.field2.required")
        .verifying(maxLength(100, "overallDocument.error.field2.length"))
    )(OverallDocument.apply)(OverallDocument.unapply)
   )
 }
