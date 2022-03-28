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

package pages

import models.{LocalReferenceNumber, UserAnswers}
import play.api.mvc.Call
import queries.{Gettable, Settable}

trait DataPage[A] extends Page with Gettable[A] with Settable[A] {

  def navigate(breadcrumbs: Breadcrumbs, answers: UserAnswers): Call = {
    val targetPage = nextPage(breadcrumbs, answers)
    val updatedBreadcrumbs = updateBreadcrumbs(breadcrumbs, targetPage, answers)
    targetPage.route(updatedBreadcrumbs, answers.lrn)
  }

  protected def updateBreadcrumbs(breadcrumbs: Breadcrumbs, target: DataPage[_], answers: UserAnswers): Breadcrumbs =
    breadcrumbs.current.map {
      case b if b == target => breadcrumbs.pop
    }.getOrElse(breadcrumbs)

  protected def nextPage(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    breadcrumbs.current.map {
      case _: CheckAnswersBreadcrumb => nextPageCheckMode(breadcrumbs, answers)
      case _: AddItemBreadcrumb      => nextPageNormalMode(breadcrumbs, answers)
    }.getOrElse(nextPageNormalMode(breadcrumbs, answers))

  protected def nextPageCheckMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    breadcrumbs.current
      .getOrElse(JourneyRecoveryPage)

  protected def nextPageNormalMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): DataPage[_] =
    throw new NotImplementedError("nextPageCheckMode is not implemented")

  def route(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Call =
    throw new NotImplementedError("route is not implemented")
}
