package forms

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class ConsigneeKnownFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "consigneeKnown.error.required"
  val invalidKey = "error.boolean"

  val form = new ConsigneeKnownFormProvider()()

  ".value" - {

    val fieldName = "value"

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
