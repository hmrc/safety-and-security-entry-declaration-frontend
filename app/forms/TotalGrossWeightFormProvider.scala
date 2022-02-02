package forms

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class TotalGrossWeightFormProvider @Inject() extends Mappings {

  def apply(): Form[Int] =
    Form(
      "value" -> int(
        "totalGrossWeight.error.required",
        "totalGrossWeight.error.wholeNumber",
        "totalGrossWeight.error.nonNumeric")
          .verifying(inRange(0, 99999999, "totalGrossWeight.error.outOfRange"))
    )
}
