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

import java.time.{LocalDate, LocalTime}
import play.api.data.FieldMapping
import play.api.data.Forms.of
import models.{Enumerable, GbEori}

trait Mappings extends Formatters with Constraints {

  protected def text(
    errorKey: String = "error.required",
    args: Seq[String] = Seq.empty
  ): FieldMapping[String] =
    of(stringFormatter(errorKey, args))

  protected def gbEori(
    errorKey: String = "error.required",
    lengthKey: String = "error.eori.length",
    nonNumericKey: String = "error.eori.numeric",
    args: Seq[String] = Nil
  ): FieldMapping[GbEori] = {
    of(gbEoriFormatter(errorKey, lengthKey, nonNumericKey, args))
  }

  protected def int(
    requiredKey: String = "error.required",
    wholeNumberKey: String = "error.wholeNumber",
    nonNumericKey: String = "error.nonNumeric",
    args: Seq[String] = Seq.empty
  ): FieldMapping[Int] =
    of(intFormatter(requiredKey, wholeNumberKey, nonNumericKey, args))

  protected def decimal(
    requiredKey: String = "error.required",
    nonNumericKey: String = "error.nonNumeric",
    invalidPrecisionKey: String = "error.precision",
    precision: Int,
    args: Seq[String] = Seq.empty
  ): FieldMapping[BigDecimal] =
    of(decimalFormatter(requiredKey, nonNumericKey, invalidPrecisionKey, precision, args))

  protected def boolean(
    requiredKey: String = "error.required",
    invalidKey: String = "error.boolean",
    args: Seq[String] = Seq.empty
  ): FieldMapping[Boolean] =
    of(booleanFormatter(requiredKey, invalidKey, args))

  protected def enumerable[A](
    requiredKey: String = "error.required",
    invalidKey: String = "error.invalid",
    args: Seq[String] = Seq.empty
  )(implicit ev: Enumerable[A]): FieldMapping[A] =
    of(enumerableFormatter[A](requiredKey, invalidKey, args))

  protected def localDate(
    invalidKey: String,
    allRequiredKey: String,
    twoRequiredKey: String,
    requiredKey: String,
    args: Seq[String] = Seq.empty
  ): FieldMapping[LocalDate] =
    of(new LocalDateFormatter(invalidKey, allRequiredKey, twoRequiredKey, requiredKey, args))

  protected def localTime(
    invalidKey: String,
    allRequiredKey: String,
    requiredKey: String,
    args: Seq[String] = Seq.empty
  ): FieldMapping[LocalTime] =
    of(new LocalTimeFormatter(invalidKey, allRequiredKey, requiredKey, args))
}
