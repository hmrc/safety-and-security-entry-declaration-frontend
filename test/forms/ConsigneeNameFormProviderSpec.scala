package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class ConsigneeNameFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "consigneeName.error.required"
  val lengthKey = "consigneeName.error.length"
  val maxLength = 35

  val form = new ConsigneeNameFormProvider()()

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
