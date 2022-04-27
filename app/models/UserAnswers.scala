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

package models

import java.time.Instant
import scala.util.{Failure, Success, Try}

import play.api.libs.json._

import models.completion.DeclarationEvent
import models.completion.downstream.CorrelationId
import queries.{Derivable, Gettable, Settable}
import uk.gov.hmrc.mongo.play.json.formats.MongoJavatimeFormats

final case class UserAnswers(
  id: String,
  lrn: LocalReferenceNumber,
  declarationEvents: Map[CorrelationId, DeclarationEvent] = Map.empty,
  data: JsObject = Json.obj(),
  lastUpdated: Instant = Instant.now
) {

  def get[A](page: Gettable[A])(implicit rds: Reads[A]): Option[A] =
    Reads.optionNoError(Reads.at(page.path)).reads(data).getOrElse(None)

  def get[A, B](derivable: Derivable[A, B])(implicit rds: Reads[A]): Option[B] =
    get(derivable: Gettable[A]).map(derivable.derive)

  def set[A](page: Settable[A], value: A)(implicit writes: Writes[A]): Try[UserAnswers] = {

    val updatedData = data.setObject(page.path, Json.toJson(value)) match {
      case JsSuccess(jsValue, _) =>
        Success(jsValue)
      case JsError(errors) =>
        Failure(JsResultException(errors))
    }

    updatedData.flatMap { d =>
      val updatedAnswers = copy(data = d)
      page.cleanup(Some(value), updatedAnswers)
    }
  }

  def remove[A](page: Settable[A]): Try[UserAnswers] = {

    val updatedData = data.removeObject(page.path) match {
      case JsSuccess(jsValue, _) =>
        Success(jsValue)
      case JsError(_) =>
        Success(data)
    }

    updatedData.flatMap { d =>
      val updatedAnswers = copy(data = d)
      page.cleanup(None, updatedAnswers)
    }
  }

  /**
   * Record a submission or amendment request associated with these answers, by correlation ID
   */
  def withDeclarationEvent(corrId: CorrelationId, event: DeclarationEvent): UserAnswers =
    copy(
      declarationEvents = declarationEvents + (corrId -> event)
    )
}

object UserAnswers {
  implicit val declarationsReads = new Reads[Map[CorrelationId, DeclarationEvent]] {
    override def reads(v: JsValue): JsResult[Map[CorrelationId, DeclarationEvent]] =
      JsSuccess(
        v.as[Map[String, JsValue]].map {
          case (k, v) => CorrelationId(k) -> v.as[DeclarationEvent]
        }
      )
  }

  implicit val declarationsWrites = new Writes[Map[CorrelationId, DeclarationEvent]] {
    override def writes(m: Map[CorrelationId, DeclarationEvent]): JsValue =
      JsObject(
        m.map { case (k, v) => k.id -> Json.toJson(v) }
      )
  }

  val reads: Reads[UserAnswers] = {

    import play.api.libs.functional.syntax._

    (
      (__ \ "eori").read[String] and
        (__ \ "lrn").read[LocalReferenceNumber] and
        (__ \ "declarationEvents").read[Map[CorrelationId, DeclarationEvent]] and
        (__ \ "data").read[JsObject] and
        (__ \ "lastUpdated").read(MongoJavatimeFormats.instantFormat)
    )(UserAnswers.apply _)
  }

  val writes: OWrites[UserAnswers] = {

    import play.api.libs.functional.syntax._

    (
      (__ \ "eori").write[String] and
        (__ \ "lrn").write[LocalReferenceNumber] and
        (__ \ "declarationEvents").write[Map[CorrelationId, DeclarationEvent]] and
        (__ \ "data").write[JsObject] and
        (__ \ "lastUpdated").write(MongoJavatimeFormats.instantFormat)
    )(unlift(UserAnswers.unapply))
  }

  implicit val format: OFormat[UserAnswers] = OFormat(reads, writes)
}
