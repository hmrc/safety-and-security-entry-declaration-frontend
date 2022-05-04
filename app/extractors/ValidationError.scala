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

package extractors

import pages.QuestionPage

sealed trait ValidationError {
  val message: String

  override def toString: String = message
}

object ValidationError {
  case class MissingField[T](page: QuestionPage[T]) extends ValidationError {
    override val message: String = s"Field $page is missing!"
  }

  case object MissingQueryResult extends ValidationError{
    override val message: String = "Missing query result"
  }

}
