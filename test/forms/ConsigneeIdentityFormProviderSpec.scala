package forms

import forms.behaviours.OptionFieldBehaviours
import models.ConsigneeIdentity
import play.api.data.FormError

class ConsigneeIdentityFormProviderSpec extends OptionFieldBehaviours {

  val form = new ConsigneeIdentityFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "consigneeIdentity.error.required"

    behave like optionsField[ConsigneeIdentity](
      form,
      fieldName,
      validValues  = ConsigneeIdentity.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
