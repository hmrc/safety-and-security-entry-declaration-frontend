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

package viewmodels.checkAnswers.goods

import controllers.goods.{routes => goodsRoutes}
import models.{Index, UserAnswers}
import pages.goods.CheckGoodsItemPage
import pages.{AddItemPage, Waypoints}
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import queries.goods.AllGoodsItemNamesQuery
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.addtoalist.ListItem

object GoodsSummary {

  def rows(answers: UserAnswers, waypoints: Waypoints, sourcePage: AddItemPage)
          (implicit messages: Messages): List[ListItem] =
    answers.get(AllGoodsItemNamesQuery).getOrElse(List.empty).zipWithIndex.map {
      case (good, index) =>
        ListItem(
          name = HtmlFormat.escape(good.name).toString,
          changeUrl = CheckGoodsItemPage(Index(index)).changeLink(waypoints, answers.lrn, sourcePage).url,
          removeUrl = goodsRoutes.RemoveGoodsController
            .onPageLoad(waypoints, answers.lrn, Index(index))
            .url
        )
      }
}
