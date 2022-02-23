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

import base.SpecBase
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.{Form, FormError}

import java.time.LocalTime

class LocalTimeMappingSpec extends SpecBase with Mappings with ScalaCheckPropertyChecks {

  val form = Form(
    "value" -> localTime(
      requiredKey = "error.required",
      allRequiredKey = "error.required.all",
      invalidKey = "error.invalid"
    )
  )

  val validTimes = for {
    hour <- Gen.choose(0, 23)
    minute <- Gen.choose(0, 59)
  } yield LocalTime.of(hour, minute)

  val invalidField = Gen.alphaStr.suchThat(_.nonEmpty)

  "must bind valid data" in {

    forAll(validTimes) { time =>

      val data = Map(
        "value.hour" -> time.getHour.toString,
        "value.minutes" -> time.getMinute.toString
      )

      val result = form.bind(data)

      result.value.value mustEqual time
    }
  }

  "must fail to bind an empty time" in {

    val result = form.bind(Map.empty[String, String])

    result.errors must contain only FormError("value", "error.required.all", List.empty)
  }

  "must fail to bind with a missing hour" in {

    forAll(validTimes) { time =>

      val data = Map("value.minutes" -> time.getMinute.toString)
      val result = form.bind(data)

      result.errors must contain only FormError("value", "error.required", List("hour"))
    }
  }

  "must fail to bind with an invalid hour" in {

    forAll(validTimes, invalidField) {
      case (time, field) =>
        val data = Map(
          "value.hour" -> field,
          "value.minutes" -> time.getMinute.toString
        )
        val result = form.bind(data)

        result.errors must contain only FormError("value", "error.invalid")
    }
  }

  "must fail to bind with a missing minutes" in {

    forAll(validTimes) { time =>

      val data = Map("value.hour" -> time.getHour.toString)
      val result = form.bind(data)

      result.errors must contain only FormError("value", "error.required", List("minutes"))
    }
  }

  "must fail to bind with an invalid minutes" in {

    forAll(validTimes, invalidField) {
      case (time, field) =>
        val data = Map(
          "value.hour" -> time.getHour.toString,
          "value.minutes" -> field
        )
        val result = form.bind(data)

        result.errors must contain only FormError("value", "error.invalid")
    }
  }

  "must unbind a time" in {

    forAll(validTimes) { time =>

      val filledForm = form.fill(time)

      filledForm("value.hour").value.value mustEqual time.getHour.toString
      filledForm("value.minutes").value.value mustEqual time.getMinute.toString
    }
  }
}
