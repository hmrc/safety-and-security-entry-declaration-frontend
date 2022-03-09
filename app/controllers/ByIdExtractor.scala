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

package controllers

import models.requests.DataRequest
import models.{Index, WithId}
import play.api.libs.json.Reads
import play.api.mvc.{AnyContent, Result}
import queries.Gettable

import scala.concurrent.Future

trait ByIdExtractor {

  protected def getItemId[A <: WithId](index: Index, allItemsQuery: Gettable[List[A]])
                                      (block: Int => Future[Result])
                                      (implicit request: DataRequest[AnyContent], ev: Reads[A]): Future[Result] =
    request.userAnswers.get(allItemsQuery).map {
      items =>
        items
          .lift(index.position)
          .map(item => block(item.id))
          .getOrElse(block(items.maxBy(_.id).id + 1))
    }.getOrElse { block(1) }
}
