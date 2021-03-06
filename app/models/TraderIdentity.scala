/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait TraderIdentity

object TraderIdentity extends Enumerable.Implicits {

  case object GBEORI extends WithName("GBEORI") with TraderIdentity
  case object NameAddress extends WithName("nameAddress") with TraderIdentity

  val values: Seq[TraderIdentity] = Seq(
    GBEORI,
    NameAddress
  )

  def options(implicit messages: Messages): Seq[RadioItem] =
    values.zipWithIndex.map {
      case (value, index) =>
        RadioItem(
          content = Text(messages(s"traderIdentity.${value.toString}")),
          value = Some(value.toString),
          id = Some(s"value_$index")
        )
    }

  implicit val enumerable: Enumerable[TraderIdentity] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
