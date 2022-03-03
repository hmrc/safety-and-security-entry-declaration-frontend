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

package forms

import forms.behaviours.DateBehaviours
import forms.routedetails.ArrivalDateAndTimeFormProvider
import models.ArrivalDateAndTime
import org.scalacheck.Gen
import play.api.data.FormError

import java.time.{LocalDate, LocalTime}

class ArrivalDateAndTimeFormProviderSpec extends DateBehaviours {

  val form = new ArrivalDateAndTimeFormProvider()()

  val validDates = datesBetween(LocalDate.of(2000, 1, 1), LocalDate.of(3000, 1, 1))

  val validTimes = for {
    hour <- Gen.choose(0, 23)
    minute <- Gen.choose(0, 59)
  } yield LocalTime.of(hour, minute)

  "must bind when valid dates and times are provided" in {

    forAll(validDates, validTimes) {
      case (date, time) =>
        val data = Map(
          "date.day" -> date.getDayOfMonth.toString,
          "date.month" -> date.getMonthValue.toString,
          "date.year" -> date.getYear.toString,
          "time.hour" -> time.getHour.toString,
          "time.minutes" -> time.getMinute.toString
        )

        val result = form.bind(data)

        result.value.value mustEqual ArrivalDateAndTime(date, time)
        result.errors mustBe empty
    }
  }

  "must not bind when date is missing" in {

    forAll(validTimes) { time =>

      val data = Map(
        "time.hour" -> time.getHour.toString,
        "time.minutes" -> time.getMinute.toString
      )

      val result = form.bind(data)

      result.errors must contain only FormError(
        "date",
        "arrivalDateAndTime.date.error.required.all"
      )
    }
  }

  "must not bind when date is invalid" in {

    forAll(validDates, validTimes) {
      case (date, time) =>
        val data = Map(
          "date.day" -> "invalid",
          "date.month" -> date.getMonthValue.toString,
          "date.year" -> date.getYear.toString,
          "time.hour" -> time.getHour.toString,
          "time.minutes" -> time.getMinute.toString
        )

        val result = form.bind(data)

        result.errors must contain only FormError("date", "arrivalDateAndTime.date.error.invalid")
    }
  }

  "must not bind when time is missing" in {

    forAll(validDates) { date =>

      val data = Map(
        "date.day" -> date.getDayOfMonth.toString,
        "date.month" -> date.getMonthValue.toString,
        "date.year" -> date.getYear.toString
      )

      val result = form.bind(data)

      result.errors must contain only FormError(
        "time",
        "arrivalDateAndTime.time.error.required.all"
      )
    }
  }

  "must not bind when time is invalid" in {

    forAll(validDates, validTimes) {
      case (date, time) =>
        val data = Map(
          "date.day" -> date.getDayOfMonth.toString,
          "date.month" -> date.getMonthValue.toString,
          "date.year" -> date.getYear.toString,
          "time.hour" -> "invalid",
          "time.minutes" -> time.getMinute.toString
        )

        val result = form.bind(data)

        result.errors must contain only FormError("time", "arrivalDateAndTime.time.error.invalid")
    }
  }
}
