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

package viewmodels.checkAnswers

import controllers.routes
import models.{Index, NormalMode, UserAnswers}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.AllPackageItemsQuery
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem

object PackageSummary {

  // TODO: Introduce `Mode`
  def rows(answers: UserAnswers, itemIndex: Index)(implicit messages: Messages): Seq[ListItem] =
    answers.get(AllPackageItemsQuery(itemIndex)).getOrElse(List.empty).zipWithIndex.map {
      case (packageItem, index) =>

        ListItem(
          name = HtmlFormat.escape(packageItem.kind.code + " - " + packageItem.kind.name).toString,
          changeUrl = routes.CheckPackageItemController.onPageLoad(NormalMode, answers.lrn, itemIndex, Index(index)).url,
          removeUrl = routes.RemovePackageController.onPageLoad(NormalMode, answers.lrn, itemIndex, Index(index)).url
        )
    }
}
