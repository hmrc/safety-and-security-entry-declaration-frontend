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

package controllers.goods

import config.IndexLimits.maxGoods
import controllers.actions.CommonControllerComponents
import models.{Index, LocalReferenceNumber, UserAnswers, WithKey}
import pages.Waypoints
import pages.goods._
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import queries.Settable
import queries.consignees.{AllConsigneesQuery, AllNotifiedPartiesQuery}
import queries.consignors.AllConsignorsQuery
import queries.routedetails.{AllPlacesOfLoadingQuery, AllPlacesOfUnloadingQuery}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

class InitialiseGoodsItemController @Inject() (
  cc: CommonControllerComponents
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
    with I18nSupport {

  protected val controllerComponents: MessagesControllerComponents = cc

  def initialise(waypoints: Waypoints, lrn: LocalReferenceNumber, itemIndex: Index): Action[AnyContent] =
    (cc.authAndGetData(lrn) andThen cc.limitIndex(itemIndex, maxGoods)).async {
      implicit request =>

        val consignors = request.userAnswers.get(AllConsignorsQuery)
        val consignees = request.userAnswers.get(AllConsigneesQuery)
        val notifiedParties = request.userAnswers.get(AllNotifiedPartiesQuery)
        val placesOfLoading = request.userAnswers.get(AllPlacesOfLoadingQuery)
        val placesOfUnloading = request.userAnswers.get(AllPlacesOfUnloadingQuery)

        val consignor = consignors.flatMap { c =>
          if (c.size == 1) c.headOption else None
        }

        val consignee = consignees.flatMap { c =>
          if (c.size == 1 && (notifiedParties.isEmpty || notifiedParties.contains(Nil))) c.headOption else None
        }

        val notifiedParty = notifiedParties.flatMap { n =>
          if (n.size == 1 && (consignees.isEmpty || consignees.contains(Nil))) n.headOption else None
        }

        val placeOfLoading = placesOfLoading.flatMap { l =>
          if (l.size == 1) l.headOption else None
        }

        val placeOfUnloading = placesOfUnloading.flatMap { u =>
          if (u.size == 1) u.headOption else None
        }

        for {
          a <- updateAnswers(request.userAnswers, ConsignorPage(itemIndex), consignor)
          b <- updateAnswers(a, ConsigneePage(itemIndex), consignee)
          c <- updateAnswers(b, NotifiedPartyPage(itemIndex), notifiedParty)
          d <- updateAnswers(c, LoadingPlacePage(itemIndex), placeOfLoading)
          e <- updateAnswers(d, UnloadingPlacePage(itemIndex), placeOfUnloading)
          _ <- cc.sessionRepository.set(e)
        } yield Redirect(InitialiseGoodsItemPage(itemIndex).navigate(waypoints, e))
    }

  private def updateAnswers[A <: WithKey](
    answers: UserAnswers,
    page: Settable[Int],
    value: Option[A]
  ): Future[UserAnswers] =
    Future.fromTry(
      value
        .map(v => answers.set(page, v.key))
        .getOrElse(Success(answers))
    )
}
