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

package viewmodels

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.tag.Tag
import viewmodels.govuk.tag._

trait CompletionStatus

object CompletionStatus {
  case object Completed extends CompletionStatus
  case object NotStarted extends CompletionStatus
  case object InProgress extends CompletionStatus
  case object Invalid extends CompletionStatus

  def tag(status: CompletionStatus)(implicit messages: Messages): Tag =
    status match {
      case Completed => TagViewModel(Text(messages("taskList.completed")))
      case NotStarted => TagViewModel(Text(messages("taskList.notStarted"))).grey()
      case InProgress => TagViewModel(Text(messages("taskList.inProgress"))).yellow()
      case Invalid => TagViewModel(Text(messages("taskList.invalid"))).red()
    }
}
