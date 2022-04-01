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

package serialisation.xml

import scala.io.Source

import base.SpecBase

class ExampleSchemaValidationSpec extends SpecBase {
  "The example payload" - {
    "should validate against the CC315A schema correctly" in {
      val payload = Source.fromResource("payload/submission-1.xml").mkString
      XmlValidator.SubmissionValidator.validate(payload)
    }
  }
}