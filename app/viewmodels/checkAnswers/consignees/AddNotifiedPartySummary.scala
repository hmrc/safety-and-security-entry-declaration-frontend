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

package viewmodels.checkAnswers.consignees

import controllers.consignees.{routes => consigneesRoutes}
import models.{Index, NormalMode, TraderWithEori, TraderWithoutEori, UserAnswers}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.consignees.AllNotifiedPartiesQuery
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem

object AddNotifiedPartySummary  {

  def rows(answers: UserAnswers)(implicit messages: Messages): Seq[ListItem] =
    answers.get(AllNotifiedPartiesQuery).getOrElse(List.empty).zipWithIndex.map {
      case (notifiedParty, index) =>
        val name = notifiedParty match {
          case t: TraderWithEori    => HtmlFormat.escape(t.eori).toString
          case t: TraderWithoutEori => HtmlFormat.escape(t.name).toString
        }

        ListItem(
          name      = name,
          changeUrl = consigneesRoutes.CheckNotifiedPartyController.onPageLoad(answers.lrn, Index(index)).url,
          removeUrl = consigneesRoutes.RemoveNotifiedPartyController.onPageLoad(NormalMode, answers.lrn, Index(index)).url
        )
    }
}