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

import forms.behaviours.StringFieldBehaviours
import models.KindOfPackage
import org.scalacheck.Arbitrary.arbitrary
import play.api.data.FormError

class KindOfPackageFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "kindOfPackage.error.required"
  val lengthKey = "kindOfPackage.error.length"
  val maxLength = 2

  val form = new KindOfPackageFormProvider()()

  ".value" - {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      arbitrary[KindOfPackage].map(_.code)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

    "must not bind any values other than valid kind of package codes" in {

      val invalidAnswers =
        arbitrary[String].suchThat(x => !KindOfPackage.allKindsOfPackage.map(_.code).contains(x))

      forAll(invalidAnswers) { answer =>
        val result = form.bind(Map("value" -> answer)).apply(fieldName)
        result.errors must contain only FormError(fieldName, requiredKey)
      }
    }
  }
}
