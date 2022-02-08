package pages

import java.time.LocalDate

import controllers.routes
import models.{NormalMode, UserAnswers}
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object $className$Page extends QuestionPage[LocalDate] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "$className;format="decap"$"
}
