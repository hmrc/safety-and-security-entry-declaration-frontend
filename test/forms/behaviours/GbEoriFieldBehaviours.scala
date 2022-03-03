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

package forms.behaviours

import models.GbEori
import org.scalacheck.Gen
import play.api.data.Form

trait GbEoriFieldBehaviours extends FieldBehaviours {
  private def expectValid(value: String, form: Form[GbEori], fieldName: String): Unit = {
    // Expected value has all non-numeric characters removed
    val expectedValue = value.filter { c => ('0' to '9').contains(c) }
    val boundForm = form.bind(Map(fieldName -> value))

    boundForm.errors mustBe empty
    boundForm.get.value mustBe expectedValue
  }

  private def expectInvalid(value: String, form: Form[GbEori], fieldName: String): Unit = {
    val boundForm = form.bind(Map(fieldName -> value))
    boundForm.errors must not be empty
  }

  def gbEoriField(form: Form[GbEori], fieldName: String): Unit = {
    "bind valid GB EORI" in {
      // Be permissive with several possible human readable formats
      val validEntries = Seq(
        "GB 123 456 789 000",
        "GB 123 456 789 000 000",
        "GB123456789000",
        "123 456 789 000",
        "123456789000"
      )

      // Generate some more variations
      val validEntriesGen = for {
        prefix <- Gen.oneOf("", " GB", "GB", "GB ")
        len <- Gen.oneOf(12, 15)
        suffix <- Gen.listOfN(len, Gen.numChar)
      } yield {
        s"$prefix${suffix.mkString}"
      }

      forAll(validEntriesGen) { s => expectValid(s, form, fieldName) }
      validEntries.foreach { s => expectValid(s, form, fieldName) }
    }

    "must not bind GB EORI with invalid prefixes" in {
      val entries = for {
        prefix <- Gen.oneOf("DE", "FR", "GG", "XX", "FOOOOO")
        len <- Gen.oneOf(12, 15)
        suffix <- Gen.listOfN(len, Gen.numChar)
      } yield {
        s"$prefix${suffix.mkString}"
      }

      forAll(entries) { s => expectInvalid(s, form, fieldName) }
    }

    "must not bind GB EORI with invalid length" in {
      val entries = for {
        prefix <- Gen.oneOf("", "GB")
        len <- Gen.oneOf(10, 11, 13, 14, 16, 17, 18)
        suffix <- Gen.listOfN(len, Gen.numChar)
      } yield {
        s"$prefix${suffix.mkString}"
      }
      forAll(entries) { s => expectInvalid(s, form, fieldName) }
    }
  }
}
