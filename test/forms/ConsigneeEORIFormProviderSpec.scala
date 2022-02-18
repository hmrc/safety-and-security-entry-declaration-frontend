package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class ConsigneeEORIFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "consigneeEORI.error.required"
  val lengthKey = "consigneeEORI.error.length"
  val maxLength = 35

  val form = new ConsigneeEORIFormProvider()()

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
