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
import models.completion.answers.Transport
import models.{Country, Index, TransportIdentity, TransportMode, UserAnswers}
import pages.transport._
import queries._

class TransportExtractor(
  override protected implicit val answers: UserAnswers
) extends Extractor[Transport] {

  private lazy val modeDetails: ValidationResult[(TransportIdentity, TransportMode, Option[Country])] =
    answers.get(TransportModePage) map { tm =>
      tm match {
        case TransportMode.Air =>
          requireAnswer(AirIdentityPage) map { id =>  (id, tm, None) }

        case TransportMode.Rail =>
          requireAnswer(RailIdentityPage) map { id => (id, tm, None) }

        case TransportMode.Maritime =>
          requireAnswer(MaritimeIdentityPage) map { id => (id, tm, None) }

        case TransportMode.Road =>
          val nationality = requireAnswer(NationalityOfTransportPage)
          val id = requireAnswer(RoadIdentityPage)
          (id, nationality).mapN { (ident, n) => (ident, tm, Some(n)) }

        case TransportMode.RoroAccompanied =>
          val nationality = requireAnswer(NationalityOfTransportPage)
          val id = requireAnswer(RoroAccompaniedIdentityPage)
          (id, nationality).mapN { (ident, n) => (ident, tm, Some(n)) }

        case TransportMode.RoroUnaccompanied =>
          val nationality = requireAnswer(NationalityOfTransportPage)
          val id = requireAnswer(RoroUnaccompaniedIdentityPage)
          (id, nationality).mapN { (ident, n) => (ident, tm, Some(n)) }

      }
    } getOrElse ValidationError.MissingField(TransportModePage).invalidNec

  override def extract(): ValidationResult[Transport] = {
    val documents = getList(
      AnyOverallDocumentsPage,
      AllOverallDocumentsQuery,
      OverallDocumentPage(Index(0))
    )
    val seals = getList(
      AddAnySealsPage,
      AllSealsQuery,
      SealPage(Index(0))
    )

    (modeDetails, documents, seals).mapN {
      case ((id, mode, nationality), documents, seals) =>
        Transport(id, mode, nationality, documents, seals)
    }
  }
}
