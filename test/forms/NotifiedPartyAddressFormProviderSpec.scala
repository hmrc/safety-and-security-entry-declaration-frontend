package forms

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class NotifiedPartyAddressFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "notifiedPartyAddress.error.required"
  val lengthKey = "notifiedPartyAddress.error.length"
  val maxLength = 35

  val form = new NotifiedPartyAddressFormProvider()()

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
