package forms

import forms.behaviours.OptionFieldBehaviours
import models.CarrierPaymentMethod
import play.api.data.FormError

class CarrierPaymentMethodFormProviderSpec extends OptionFieldBehaviours {

  val form = new CarrierPaymentMethodFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "carrierPaymentMethod.error.required"

    behave like optionsField[CarrierPaymentMethod](
      form,
      fieldName,
      validValues  = CarrierPaymentMethod.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
