package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import models.GrossWeight

class GrossWeightFormProvider @Inject() extends Mappings {

  def apply(): Form[GrossWeight] =
    Form(
      "value" -> enumerable[GrossWeight]("grossWeight.error.required")
    )
}
