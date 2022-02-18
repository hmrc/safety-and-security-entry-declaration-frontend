package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class ConsigneeAddressFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "consigneeAddress.error.required"
  val lengthKey = "consigneeAddress.error.length"
  val maxLength = 35

  val form = new ConsigneeAddressFormProvider()()

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
