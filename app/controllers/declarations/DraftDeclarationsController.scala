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

package controllers.declarations

import controllers.actions._
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.declarations.DraftDeclarationsSummary
import viewmodels.govuk.summarylist._
import views.html.declarations.DraftDeclarationsView

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class DraftDeclarationsController @Inject() (
  cc: CommonControllerComponents,
  view: DraftDeclarationsView,
  repository: SessionRepository
)(implicit ec: ExecutionContext)
  extends FrontendBaseController
  with I18nSupport {

  protected val controllerComponents: MessagesControllerComponents = cc

  def onPageLoad(): Action[AnyContent] = {
    cc.identify.async { implicit request =>
      repository.getSummaryList(request.eori).map { lrns =>
        val summary = SummaryListViewModel(
          rows = lrns.map(DraftDeclarationsSummary.row)
        )
        Ok(view(summary))
      }
    }
  }
}
