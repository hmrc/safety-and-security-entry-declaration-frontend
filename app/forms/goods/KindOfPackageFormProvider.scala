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

package forms.goods

import forms.mappings.Mappings
import models.KindOfPackage
import models.KindOfPackage.allKindsOfPackage
import play.api.data.Form

import javax.inject.Inject

class KindOfPackageFormProvider @Inject() extends Mappings {

  def apply(): Form[KindOfPackage] =
    Form(
      "value" -> text("kindOfPackage.error.required")
        .verifying(
          "kindOfPackage.error.required",
          value => allKindsOfPackage.exists(_.code == value)
        )
        .transform[KindOfPackage](value => allKindsOfPackage.find(_.code == value).get, _.code)
    )
}
