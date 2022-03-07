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

package pages.routedetails

import controllers.routedetails.{routes => routedetailsRoutes}
import models.{Index, NormalMode, UserAnswers}
import pages.Page
import play.api.mvc.Call
import queries.routedetails.DeriveNumberOfPlacesOfUnloading

final case class RemovePlaceOfUnloadingPage(index: Index) extends Page {

  override protected def navigateInNormalMode(answers: UserAnswers): Call = {
    answers.get(DeriveNumberOfPlacesOfUnloading) match {
      case Some(n) if n > 0 =>
        routedetailsRoutes.AddPlaceOfUnloadingController.onPageLoad(NormalMode, answers.lrn)
      case _ =>
        routedetailsRoutes.PlaceOfUnloadingController.onPageLoad(NormalMode, answers.lrn, Index(0))
    }
  }
}
