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

  def navigate(waypoints: Waypoints, answers: UserAnswers): Call = {
    val targetPage = nextPage(waypoints, answers)
    val updatedWaypoints = updateWaypoints(waypoints, targetPage)
    targetPage.route(updatedWaypoints, answers.lrn)
  }

  protected[pages] def updateWaypoints(waypoints: Waypoints, target: Page): Waypoints =
    waypoints match {
      case EmptyWaypoints =>
        EmptyWaypoints

      case w: NonEmptyWaypoints =>
        (this, target) match {
          case (a: AddToListQuestionPage, b: AddToListQuestionPage) if a.section == b.section =>
              waypoints

          case (_, thatPage: AddToListQuestionPage) =>
            if (w.current == thatPage.addItemWaypoint) {
              waypoints
            } else {
              waypoints.push(thatPage.addItemWaypoint)
            }

          case _ =>
            w.current match {
              case x if x.page == target => w.pop
              case _ => w
            }
        }
    }

  protected def nextPage(waypoints: Waypoints, answers: UserAnswers): Page =
    waypoints match {
      case EmptyWaypoints =>
        nextPageNormalMode(waypoints, answers)

      case b: NonEmptyWaypoints =>
        b.mode match {
          case CheckMode  => nextPageCheckMode(b, answers)
          case NormalMode => nextPageNormalMode(b, answers)
        }
    }

  protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    waypoints.current.page

  protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    throw new NotImplementedError("nextPageNormalMode is not implemented")

  def route(waypoints: Waypoints, lrn: LocalReferenceNumber): Call =
    throw new NotImplementedError("route is not implemented")

  def changeLink(waypoints: Waypoints, lrn: LocalReferenceNumber, sourcePage: CheckAnswersPage): Call =
    route(waypoints.push(sourcePage.waypoint), lrn)

  def changeLink(waypoints: Waypoints, lrn: LocalReferenceNumber, sourcePage: AddItemPage): Call =
    route(waypoints.push(sourcePage.waypoint(CheckMode)), lrn)
}

object Page {

  implicit def toString(page: Page): String =
    page.toString
}
