package forms

import forms.behaviours.OptionFieldBehaviours
import models.NotifiedPartyIdentity
import play.api.data.FormError

class NotifiedPartyIdentityFormProviderSpec extends OptionFieldBehaviours {

  val form = new NotifiedPartyIdentityFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "notifiedPartyIdentity.error.required"

    behave like optionsField[NotifiedPartyIdentity](
      form,
      fieldName,
      validValues  = NotifiedPartyIdentity.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
