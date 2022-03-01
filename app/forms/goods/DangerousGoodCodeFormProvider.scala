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
import models.DangerousGood
import play.api.data.Form
import services.DangerousGoodsService

import javax.inject.Inject

class DangerousGoodCodeFormProvider @Inject() (dangerousGoodsService: DangerousGoodsService) extends Mappings {

  def apply(): Form[DangerousGood] =
    Form(
      "value" -> text("dangerousGoodCode.error.required")
        .verifying(
          "dangerousGoodCode.error.required",
          value => dangerousGoodsService.allDangerousGoods.exists(_.code == value)
        )
        .transform[DangerousGood](
          value => dangerousGoodsService.allDangerousGoods.find(_.code == value).get,
          _.code
        )
    )
}
