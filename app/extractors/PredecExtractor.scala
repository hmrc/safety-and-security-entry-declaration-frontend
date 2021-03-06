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
import models.completion.Party
import models.completion.answers.Predec
import models.{LocalReferenceNumber, LodgingPersonType, ProvideGrossWeight, TraderIdentity, UserAnswers}
import pages.predec._

class PredecExtractor(
  override protected implicit val answers: UserAnswers
) extends Extractor[Predec] {

  private def extractTotalMass(): ValidationResult[Option[BigDecimal]] = {
    // TODO: Could be DRYer as it's similar to getAnswer?
    answers.get(ProvideGrossWeightPage) map {
      case ProvideGrossWeight.Overall => requireAnswer(TotalGrossWeightPage) map { Some(_) }
      case ProvideGrossWeight.PerItem => None.validNec
    } getOrElse ValidationError.MissingField(ProvideGrossWeightPage).invalidNec
  }

  private def extractCarrier(): ValidationResult[Option[Party]] =
    answers.get(LodgingPersonTypePage) map {
      case LodgingPersonType.Carrier =>
        None.validNec

      case LodgingPersonType.Representative =>
        answers.get(CarrierIdentityPage) map {
          case TraderIdentity.GBEORI =>
            requireAnswer(CarrierEORIPage) map { eori =>
              Some(Party.ByEori(eori))
            }

          case TraderIdentity.NameAddress =>
            (
              requireAnswer(CarrierNamePage),
              requireAnswer(CarrierAddressPage)
            ).mapN {
              case (name, address) =>
                Some(Party.ByAddress(name, address))
            }
        } getOrElse ValidationError.MissingField(CarrierIdentityPage).invalidNec
    } getOrElse ValidationError.MissingField(LodgingPersonTypePage).invalidNec

  override def extract(): ValidationResult[Predec] = {
    val lrn: ValidationResult[LocalReferenceNumber] = answers.lrn.validNec
    val location = requireAnswer(DeclarationPlacePage)
    val carrier = extractCarrier()
    val totalMass = extractTotalMass()

    (lrn, location, carrier, totalMass).mapN(Predec)
  }
}
