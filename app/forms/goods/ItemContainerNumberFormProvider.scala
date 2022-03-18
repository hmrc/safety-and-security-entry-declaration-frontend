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

import javax.inject.Inject
import forms.mappings.Mappings
import models.Container
import play.api.data.Form
import play.api.data.Forms._


class ItemContainerNumberFormProvider @Inject() extends Mappings {

  def apply(): Form[Container] =
    Form(
      mapping(
        "value" -> text("itemContainerNumber.error.required")
          .verifying(maxLength(17, "itemContainerNumber.error.length"))
      )(Container.apply)(Container.unapply)
    )
}
