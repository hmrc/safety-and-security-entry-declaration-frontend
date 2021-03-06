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

import models.Enumerable
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.{Form, FormError}

object MappingsSpec {

  sealed trait Foo
  case object Bar extends Foo
  case object Baz extends Foo

  object Foo {

    val values: Set[Foo] = Set(Bar, Baz)

    implicit val fooEnumerable: Enumerable[Foo] =
      Enumerable(values.toSeq.map(v => v.toString -> v): _*)
  }
}

class MappingsSpec
  extends AnyFreeSpec
  with Matchers
  with OptionValues
  with Mappings
  with ScalaCheckPropertyChecks {

  import MappingsSpec._

  "text" - {

    val testForm: Form[String] =
      Form(
        "value" -> text()
      )

    "must bind a valid string" in {
      val result = testForm.bind(Map("value" -> "foobar"))
      result.get mustEqual "foobar"
    }

    "must not bind an empty string" in {
      val result = testForm.bind(Map("value" -> ""))
      result.errors must contain(FormError("value", "error.required"))
    }

    "must not bind a string of whitespace only" in {
      val result = testForm.bind(Map("value" -> " \t"))
      result.errors must contain(FormError("value", "error.required"))
    }

    "must not bind an empty map" in {
      val result = testForm.bind(Map.empty[String, String])
      result.errors must contain(FormError("value", "error.required"))
    }

    "must return a custom error message" in {
      val form = Form("value" -> text("custom.error"))
      val result = form.bind(Map("value" -> ""))
      result.errors must contain(FormError("value", "custom.error"))
    }

    "must unbind a valid value" in {
      val result = testForm.fill("foobar")
      result.apply("value").value.value mustEqual "foobar"
    }
  }

  "boolean" - {

    val testForm: Form[Boolean] =
      Form(
        "value" -> boolean()
      )

    "must bind true" in {
      val result = testForm.bind(Map("value" -> "true"))
      result.get mustEqual true
    }

    "must bind false" in {
      val result = testForm.bind(Map("value" -> "false"))
      result.get mustEqual false
    }

    "must not bind a non-boolean" in {
      val result = testForm.bind(Map("value" -> "not a boolean"))
      result.errors must contain(FormError("value", "error.boolean"))
    }

    "must not bind an empty value" in {
      val result = testForm.bind(Map("value" -> ""))
      result.errors must contain(FormError("value", "error.required"))
    }

    "must not bind an empty map" in {
      val result = testForm.bind(Map.empty[String, String])
      result.errors must contain(FormError("value", "error.required"))
    }

    "must unbind" in {
      val result = testForm.fill(true)
      result.apply("value").value.value mustEqual "true"
    }
  }

  "int" - {

    val testForm: Form[Int] =
      Form(
        "value" -> int()
      )

    "must bind a valid integer" in {
      val result = testForm.bind(Map("value" -> "1"))
      result.get mustEqual 1
    }

    "must not bind an empty value" in {
      val result = testForm.bind(Map("value" -> ""))
      result.errors must contain(FormError("value", "error.required"))
    }

    "must not bind an empty map" in {
      val result = testForm.bind(Map.empty[String, String])
      result.errors must contain(FormError("value", "error.required"))
    }

    "must unbind a valid value" in {
      val result = testForm.fill(123)
      result.apply("value").value.value mustEqual "123"
    }
  }

  "enumerable" - {

    val testForm = Form(
      "value" -> enumerable[Foo]()
    )

    "must bind a valid option" in {
      val result = testForm.bind(Map("value" -> "Bar"))
      result.get mustEqual Bar
    }

    "must not bind an invalid option" in {
      val result = testForm.bind(Map("value" -> "Not Bar"))
      result.errors must contain(FormError("value", "error.invalid"))
    }

    "must not bind an empty map" in {
      val result = testForm.bind(Map.empty[String, String])
      result.errors must contain(FormError("value", "error.required"))
    }
  }

  "decimal" - {

    val testForm: Form[BigDecimal] = Form(
      "value" -> decimal(precision = 2)
    )

    "must bind integers" in {

      forAll(arbitrary[Int]) { int =>
        val result = testForm.bind(Map("value" -> int.toString))
        result.get mustEqual BigDecimal(int)
        result.errors mustBe empty
      }
    }

    "must bind valid decimals with a precision up to that specified" in {

      val gen = for {
        number <- arbitrary[Int]
        decimals <- Gen.choose(0, 99)
      } yield number + "." + decimals

      forAll(gen) { decimal =>
        val result = testForm.bind(Map("value" -> decimal))
        result.get mustEqual BigDecimal(decimal)
        result.errors mustBe empty
      }
    }

    "must bind valid numbers with spaces and commas" in {

      val result = testForm.bind(Map("value" -> " 1, 234 . 56 "))
      result.get mustEqual BigDecimal("1234.56")
      result.errors mustBe empty
    }

    "must not bind values with non-numeric characters except spaces and commas" in {

      val result = testForm.bind(Map("value" -> "abc"))
      result.errors must contain only FormError("value", "error.nonNumeric")
    }

    "must not bind decimals with too high a precision" in {

      val gen = for {
        number <- arbitrary[Int]
        decimals <- Gen.choose(100, Int.MaxValue)
      } yield number + "." + decimals

      forAll(gen) { decimal =>
        val result = testForm.bind(Map("value" -> decimal))
        result.errors must contain only FormError("value", "error.precision")
      }
    }

    "must not bind an empty value" in {
      val result = testForm.bind(Map("value" -> ""))
      result.errors must contain only FormError("value", "error.required")
    }

    "must not bind an empty map" in {
      val result = testForm.bind(Map.empty[String, String])
      result.errors must contain only FormError("value", "error.required")
    }

    "must unbind a valid value" in {
      val result = testForm.fill(BigDecimal(1.23))
      result.apply("value").value.value mustEqual "1.23"
    }
  }

  "inList" - {

    val allowedValues = Gen.nonEmptyListOf(arbitrary[String]).sample.value

    val testForm: Form[String] = Form(
      "fieldName" -> inList[String](
        allowedValues = allowedValues,
        allowedValuesAsString = s => s,
        requiredKey = "error.required"
      )
    )

    "must bind all allowed values" in {

      for (allowedValue <- allowedValues) {
        val result = testForm.bind(Map("fieldName" -> allowedValue))
        result.apply("fieldName").value.value mustEqual allowedValue
      }
    }

    "must not bind any other values" in {

      forAll(arbitrary[String] suchThat (!allowedValues.contains(_))) {
        invalidValue =>
          val result = testForm.bind(Map("fieldName" -> invalidValue))
          result.apply("fieldName").errors must contain only FormError("fieldName", "error.required")
      }
    }

    "must not bind an empty value" in {
      val result = testForm.bind(Map("fieldName" -> ""))
      result.apply("fieldName").errors must contain only FormError("fieldName", "error.required")
    }

    "must not bind an empty map" in {
      val result = testForm.bind(Map.empty[String, String])
      result.apply("fieldName").errors must contain only FormError("fieldName", "error.required")
    }
  }
}
