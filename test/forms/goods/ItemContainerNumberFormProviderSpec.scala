package forms.goods

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class ItemContainerNumberFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "itemContainerNumber.error.required"
  val lengthKey = "itemContainerNumber.error.length"
  val maxLength = 20

  val form = new ItemContainerNumberFormProvider()()

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
