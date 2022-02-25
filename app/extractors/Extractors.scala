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

import cats.data._
import cats.implicits._
import play.api.libs.json.Reads

import models.UserAnswers
import pages.QuestionPage

/**
 * Extractor DSL to neatly handle extracting answers in common patterns
 */
trait Extractors {
  type ValidationResult[A] = ValidatedNec[ValidationError, A]

  def requireAnswer[T : Reads](page: QuestionPage[T])(
    implicit answers: UserAnswers
  ): ValidationResult[T] = {
    Validated.fromOption(answers.get(page), ValidationError.MissingField(page)).toValidatedNec
  }

  /**
   * Get the answer to a question guarded by a "can you provide..." page
   *
   * This enforces the following rules:
   *  - If they answered yes to `canProvidePage`, the answer must be provided
   *  - If they answered no to `canProvidePage`, we'll return None
   *  - If they didn't answer `canProvidePage`, we'll error as that field is missing
   *
   * @param canProvidePage The question page asking if the field is being provided
   * @param answerPage The question page providing the answer
   */
  def getAnswer[T : Reads](canProvidePage: QuestionPage[Boolean], answerPage: QuestionPage[T])(
    implicit answers: UserAnswers
  ): ValidationResult[Option[T]] = {
    answers.get(canProvidePage) map {
      case true => requireAnswer[T](answerPage) map { Some(_) }
      case _ => None.validNec
    } getOrElse ValidationError.MissingField(canProvidePage).invalidNec
  }
}

object Extractors extends Extractors
