package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait GrossWeight

object GrossWeight extends Enumerable.Implicits {

  case object Overall extends WithName("overall") with GrossWeight
  case object PerItem extends WithName("per item") with GrossWeight

  val values: Seq[GrossWeight] = Seq(
    Overall, PerItem
  )

  def options(implicit messages: Messages): Seq[RadioItem] = values.zipWithIndex.map {
    case (value, index) =>
      RadioItem(
        content = Text(messages(s"grossWeight.${value.toString}")),
        value   = Some(value.toString),
        id      = Some(s"value_$index")
      )
  }

  implicit val enumerable: Enumerable[GrossWeight] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
