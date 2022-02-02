package pages

import models.GrossWeight
import play.api.libs.json.JsPath

case object GrossWeightPage extends QuestionPage[GrossWeight] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "grossWeight"
}
