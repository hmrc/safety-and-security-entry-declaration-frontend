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
import queries.Gettable

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

  /**
   * Extract a list of items, as from the "add to list" pattern
   *
   * This assumes that having an empty list is an error, and that the list can be accessed via a
   * query
   *
   * @param query The query to run to fetch the list from user answers
   * @param requiredPage The page to report as missing if we extracted an empty list or entirely
   *   missed section; this is probably the page where you record the first element
   */
  def requireList[T : Reads](query: Gettable[List[T]], requiredPage: QuestionPage[_])(
    implicit answers: UserAnswers
  ): ValidationResult[List[T]] = {
    answers.get(query) flatMap {
      case Nil => None
      case results => Some(results.validNec)
    } getOrElse ValidationError.MissingField(requiredPage).invalidNec
  }

  /**
   * Extract list of items guarded by a "can you provide... / do you need to provide..." page
   *
   * This is effectively a combination of `getAnswer` and `requireList`: we check whether the user
   * has answered the "guard" question, as in `getAnswer`, and then we extract a list if they have
   * said yes, they will provide one or more items.
   *
   * This enforces the following rules:
   *   - If they answered yes to `canProvidePage`, there must be at least one item provided
   *   - If they answered no to `canProvidePage`, we'll return Nil
   *   - If they didn't answer `canProvidePage`, we'll error as that field is missing
   */
  def getList[T : Reads](
    canProvidePage: QuestionPage[Boolean],
    query: Gettable[List[T]],
    requiredPage: QuestionPage[_]
  )(implicit answers: UserAnswers): ValidationResult[List[T]] = {
    answers.get(canProvidePage) map {
      case true => requireList[T](query, requiredPage)
      case _ => Nil.validNec
    } getOrElse ValidationError.MissingField(canProvidePage).invalidNec
  }
}

object Extractors extends Extractors
