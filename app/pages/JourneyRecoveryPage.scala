package pages

import controllers.routes
import models.LocalReferenceNumber
import play.api.libs.json.JsPath
import play.api.mvc.Call

object JourneyRecoveryPage extends DataPage[Unit] {

  override def route(breadcrumbs: Breadcrumbs, lrn: LocalReferenceNumber): Call =
    routes.JourneyRecoveryController.onPageLoad()

  override def path: JsPath = JsPath
}
