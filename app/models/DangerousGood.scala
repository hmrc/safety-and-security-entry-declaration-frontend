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

import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.govukfrontend.views.viewmodels.select.SelectItem
import viewmodels.govuk.select._

case class DangerousGood(code: String, name: String)

object DangerousGood {

  implicit val format: OFormat[DangerousGood] = Json.format[DangerousGood]

  def selectItems(dangerousGoods: Seq[DangerousGood]): Seq[SelectItem] =
    SelectItem(value = None, text = "Select a dangerous good") +:
      dangerousGoods.map { dangerousGood =>
        SelectItemViewModel(
          value = dangerousGood.code,
          text = s"${dangerousGood.code}: ${dangerousGood.name}"
        )
      }
}
