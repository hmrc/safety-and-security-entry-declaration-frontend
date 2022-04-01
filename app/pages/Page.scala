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

import controllers.routes
import models.{CheckMode, LocalReferenceNumber, Mode, NormalMode, UserAnswers}
import play.api.mvc.Call

import scala.language.implicitConversions

trait Page {

  def navigate(mode: Mode, answers: UserAnswers): Call =
    mode match {
      case NormalMode => navigateInNormalMode(answers)
      case CheckMode => navigateInCheckMode(answers)
    }

  protected def navigateInNormalMode(answers: UserAnswers): Call =
    routes.IndexController.onPageLoad

  protected def navigateInCheckMode(answers: UserAnswers): Call =
    routes.CheckYourAnswersController.onPageLoad(answers.lrn)

  // ******************************

  def navigate(breadcrumbs: Breadcrumbs, answers: UserAnswers): Call = {
    val targetPage = nextPage(breadcrumbs, answers)
    val updatedBreadcrumbs = updateBreadcrumbs(breadcrumbs, targetPage)
    targetPage.route(updatedBreadcrumbs, answers.lrn)
  }

  protected def updateBreadcrumbs(breadcrumbs: Breadcrumbs, target: Page): Breadcrumbs = {

    (this, target) match {
      case (_, thatPage: AddToListQuestionPage) =>
        if (breadcrumbs.list.isEmpty || breadcrumbs.current.contains(thatPage.addItemBreadcrumb)) {
          breadcrumbs
        } else {
          breadcrumbs.push(thatPage.addItemBreadcrumb)
        }

      case _ =>
        breadcrumbs.current.map {
          case b if b.page == target => breadcrumbs.pop
          case _ => breadcrumbs
        }.getOrElse(breadcrumbs)
    }
  }

  protected def nextPage(breadcrumbs: Breadcrumbs, answers: UserAnswers): Page =
    breadcrumbs.mode match {
      case CheckMode  => nextPageCheckMode(breadcrumbs, answers)
      case NormalMode => nextPageNormalMode(breadcrumbs, answers)
    }

  protected def nextPageCheckMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): Page =
    breadcrumbs.current
      .map(_.page)
      .orRecover

  protected def nextPageNormalMode(breadcrumbs: Breadcrumbs, answers: UserAnswers): Page =
    throw new NotImplementedError("nextPageCheckMode is not implemented")

  def route(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Call =
    throw new NotImplementedError("route is not implemented")

  def changeLink(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber, sourcePage: CheckAnswersPage): Call =
    route(breadcrumbs.push(sourcePage.breadcrumb), lrn)
}

object Page {

  implicit def toString(page: Page): String =
    page.toString
}
