package forms

import forms.behaviours.OptionFieldBehaviours
import models.TransportMode
import play.api.data.FormError

class TransportModeFormProviderSpec extends OptionFieldBehaviours {

  val form = new TransportModeFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "transportMode.error.required"

    behave like optionsField[TransportMode](
      form,
      fieldName,
      validValues  = TransportMode.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
