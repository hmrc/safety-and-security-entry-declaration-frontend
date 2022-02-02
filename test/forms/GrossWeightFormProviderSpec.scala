package forms

import forms.behaviours.OptionFieldBehaviours
import models.GrossWeight
import play.api.data.FormError

class GrossWeightFormProviderSpec extends OptionFieldBehaviours {

  val form = new GrossWeightFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "grossWeight.error.required"

    behave like optionsField[GrossWeight](
      form,
      fieldName,
      validValues  = GrossWeight.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
