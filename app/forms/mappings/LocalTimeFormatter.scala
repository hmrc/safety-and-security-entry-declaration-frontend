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

package forms.mappings


import play.api.data.FormError
import play.api.data.format.Formatter

import java.time.LocalTime
import scala.util.{Failure, Success, Try}

private[mappings] class LocalTimeFormatter(
                                            invalidKey: String,
                                            allRequiredKey: String,
                                            requiredKey: String,
                                            args: Seq[String] = Seq.empty
                                          ) extends Formatter[LocalTime] with Formatters {

  private val fieldKeys: List[String] = List("hour", "minutes")

  private def toTime(key: String, hour: Int, minute: Int): Either[Seq[FormError], LocalTime] =
    Try(LocalTime.of(hour, minute)) match {
      case Success(time) => Right(time)
      case Failure(_)    => Left(Seq(FormError(key, invalidKey, args)))
    }

  private def formatTime(key: String, data: Map[String, String]): Either[Seq[FormError], LocalTime] = {

    val int = intFormatter(
      requiredKey = invalidKey,
      wholeNumberKey = invalidKey,
      nonNumericKey = invalidKey,
      args
    )

    for {
      hour    <- int.bind(s"$key.hour", data).right
      minute <- int.bind(s"$key.minutes", data).right
      time    <- toTime(key, hour, minute).right
    } yield time
  }

  override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], LocalTime] = {

    val fields = fieldKeys.map {
      field =>
        field -> data.get(s"$key.$field").filter(_.nonEmpty)
    }.toMap

    lazy val missingFields = fields
      .withFilter(_._2.isEmpty)
      .map(_._1)
      .toList

    fields.count(_._2.isDefined) match {
      case 2 =>
        formatTime(key, data).left.map {
          _.map(_.copy(key = key, args = args))
        }
      case 1 =>
        Left(List(FormError(key, requiredKey, missingFields ++ args)))
      case _ =>
        Left(List(FormError(key, allRequiredKey, args)))
    }
  }

  override def unbind(key: String, value: LocalTime): Map[String, String] =
    Map(
      s"$key.hour"    -> value.getHour.toString,
      s"$key.minutes" -> value.getMinute.toString
    )
}
