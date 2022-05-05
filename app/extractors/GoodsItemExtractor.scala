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

import models.completion.answers.{GoodsItem, Parties, Predec}
import models._
import models.completion.downstream.{DangerousGoodsCode, GoodsItemIdentity, Package}
import models.completion.{LoadingPlace, Party}
import pages.goods._
import queries.goods._

class GoodsItemExtractor(
  predec: Predec,
  loadingPlaces: Map[Int, LoadingPlace],
  unloadingPlaces: Map[Int, LoadingPlace],
  parties: Parties,
  index: Index
)(override protected implicit val answers: UserAnswers) extends Extractor[GoodsItem] {

  private def convertPackage(packageItem: PackageItem): Package = {
    packageItem match {
      case p: BulkPackageItem =>
        Package(
          kindPackage = p.kind,
          numPackages = None,
          numPieces = None,
          mark = p.markOrNumber
        )
      case p: UnpackedPackageItem =>
        Package(
          kindPackage = p.kind,
          numPackages = None,
          numPieces = Some(p.numberOfPieces),
          mark = p.markOrNumber
        )
      case p: StandardPackageItem =>
        Package(
          kindPackage = p.kind,
          numPackages = Some(p.numberOfPackages),
          numPieces = None,
          mark = Some(p.markOrNumber)
        )
    }
  }

  private lazy val extractedGrossMass: ValidationResult[Option[BigDecimal]] = {
    if (predec.totalMass.isDefined) {
      None.validNec
    } else {
      requireAnswer(GoodsItemGrossWeightPage(index)) map { Some(_) }
    }
  }

  private lazy val extractedConsignor: ValidationResult[Party] = {
    requireAnswer(ConsignorPage(index)) map { parties.consignors(_) }
  }

  /**
   * Extract either the consignee or notified party, depending on which was specified
   *
   * @return A validated tuple of (consignee, notified party)
   */
  private lazy val extractedReceivingParty: ValidationResult[(Option[Party], Option[Party])] = {
    lazy val extractedConsignee: ValidationResult[Option[Party]] = {
      requireAnswer(ConsigneePage(index)) map { i => Some(parties.consignees(i)) }
    }
    lazy val extractedNotifiedParty: ValidationResult[Option[Party]] = {
      requireAnswer(NotifiedPartyPage(index)) map { i => Some(parties.notifiedParties(i)) }
    }
    lazy val missing: ValidationResult[Option[Party]] = None.validNec

    (parties.consignees, parties.notifiedParties) match {
      case (consignees, notifiedParties) if consignees.nonEmpty && notifiedParties.nonEmpty =>
        val page = ConsigneeKnownPage(index)

        answers.get(page) map {
          case true => (extractedConsignee, missing).tupled
          case false => (missing, extractedNotifiedParty).tupled
        } getOrElse ValidationError.MissingField(page).invalidNec

      case (consignees, notifiedParties) if consignees.nonEmpty =>
        (extractedConsignee, missing).tupled

      case (consignees, notifiedParties) =>
        (missing, extractedNotifiedParty).tupled
    }
  }

  private lazy val extractedDangerousGoodsCode: ValidationResult[Option[DangerousGoodsCode]] = {
    getAnswer(DangerousGoodPage(index),
      DangerousGoodCodePage(index)).map {_.map { c => DangerousGoodsCode(c.code) } }
  }

  private lazy val extractedIdentity: ValidationResult[GoodsItemIdentity] = {
    val page = CommodityCodeKnownPage(index)
    answers.get(page).map {
      case true =>
        requireAnswer(CommodityCodePage(index)).map(GoodsItemIdentity.ByCommodityCode)
      case false =>
        requireAnswer(GoodsDescriptionPage(index)).map(GoodsItemIdentity.WithDescription)
    } getOrElse ValidationError.MissingField(page).invalidNec
  }

  override def extract(): ValidationResult[GoodsItem] = {
    val extractedPackages: ValidationResult[List[Package]] = requireList(
      AllPackageItemsQuery(index),
      KindOfPackagePage(index, Index(0))
    ).map { _.map(convertPackage) }

    val extractedContainers = getList(
      AnyShippingContainersPage(index),
      AllContainersQuery(index),
      ItemContainerNumberPage(index, Index(0))
    )

    val extractedDocs = getList(
      AddAnyDocumentsPage(index),
      AllDocumentsQuery(index),
      DocumentPage(index, Index(0))
    )

    val extractedLoadingPlace: ValidationResult[LoadingPlace] = {
      requireAnswer(LoadingPlacePage(index)) map { loadingPlaces(_) }
    }
    val extractedUnloadingPlace: ValidationResult[LoadingPlace] = {
      requireAnswer(UnloadingPlacePage(index)) map { unloadingPlaces(_) }
    }

    (
      extractedIdentity,
      extractedConsignor,
      extractedReceivingParty,
      extractedLoadingPlace,
      extractedUnloadingPlace,
      extractedContainers,
      extractedPackages,
      extractedGrossMass,
      extractedDocs,
      extractedDangerousGoodsCode,
      requireAnswer(PaymentMethodPage(index))
    ).mapN {
      case (
        id,
        consignor,
        (
          consignee,
          notifiedParty
        ),
        loadingPlace,
        unloadingPlace,
        containers,
        packages,
        grossMass,
        docs,
        dangerousGoodsCode,
        paymentMethod
      ) =>
        GoodsItem(
          id,
          consignor,
          consignee,
          notifiedParty,
          loadingPlace,
          unloadingPlace,
          containers,
          packages,
          grossMass,
          docs,
          dangerousGoodsCode,
          paymentMethod
        )
    }
  }
}
