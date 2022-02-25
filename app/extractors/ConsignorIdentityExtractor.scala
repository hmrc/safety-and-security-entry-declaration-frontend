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

import cats.implicits._

import extractors.ValidationError._
import models.{ConsignorIdentity => ConsignorIdentityAnswer, Index, UserAnswers}
import models.completion.answers.ConsignorIdentity
import pages.consignors._

class ConsignorIdentityExtractor(itemIndex: Index)(
  override protected implicit val answers: UserAnswers
) extends Extractor[ConsignorIdentity] {

  private def extractByEori(): ValidationResult[ConsignorIdentity] = {
    requireAnswer(ConsignorEORIPage(itemIndex)) map { ConsignorIdentity.ByEori(_) }
  }

  private def extractByAddress(): ValidationResult[ConsignorIdentity] = {
    val name = requireAnswer(ConsignorNamePage(itemIndex))
    val addr = requireAnswer(ConsignorAddressPage(itemIndex))

    (name, addr).mapN(ConsignorIdentity.ByAddress)
  }

  override def extract(): ValidationResult[ConsignorIdentity] = {
    val page = ConsignorIdentityPage(itemIndex)

    answers.get(page) map {
      case ConsignorIdentityAnswer.GBEORI => extractByEori()
      case ConsignorIdentityAnswer.NameAddress => extractByAddress()
    } getOrElse {
      MissingField(page).invalidNec
    }
  }
}
