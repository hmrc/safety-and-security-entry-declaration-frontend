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

import base.SpecBase
import forms.behaviours.StringFieldBehaviours
import generators.ModelGenerators
import models.DangerousGood
import org.scalacheck.Arbitrary.arbitrary
import play.api.data.FormError
import services.DangerousGoodsService

class DangerousGoodCodeFormProviderSpec
  extends StringFieldBehaviours
  with SpecBase
  with ModelGenerators {

  val requiredKey = "dangerousGoodCode.error.required"
  val lengthKey = "dangerousGoodCode.error.length"
  val maxLength = 200
  val application = applicationBuilder(None).build()

  def service: DangerousGoodsService = application.injector.instanceOf[DangerousGoodsService]

  val form = new DangerousGoodCodeFormProvider(service)()

  ".value" - {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      arbitrary[DangerousGood].map(_.code)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

    "must not bind any values other than valid dangerous goods" in {
      val invalidAnswers =
        arbitrary[String].suchThat(x => !allDangerousGoods.map(_.code).contains(x))

      forAll(invalidAnswers) { answer =>
        val result = form.bind(Map("value" -> answer)).apply(fieldName)
        result.errors must contain only FormError(fieldName, requiredKey)
      }
    }
  }
}
