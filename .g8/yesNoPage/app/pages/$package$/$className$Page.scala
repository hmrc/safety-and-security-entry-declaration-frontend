package pages.$package$

import controller.$package$.{routes => $package$Routes}
import controllers.routes
import models.{NormalMode, UserAnswers}
import pages.QuestionPage
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object $className$Page extends QuestionPage[Boolean] {
  
  override def path: JsPath = JsPath \ toString
  
  override def toString: String = "$className;format="decap"$"
}
