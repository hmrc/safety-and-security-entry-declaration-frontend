package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class ConsignorEORIFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "consignorEORI.error.required"
  val lengthKey = "consignorEORI.error.length"
  val maxLength = 50

  val form = new ConsignorEORIFormProvider()()

  ".value" - {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
