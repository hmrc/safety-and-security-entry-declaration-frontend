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

package viewmodels

import base.SpecBase
import controllers.consignees.{routes => consigneeRoutes}
import controllers.consignors.{routes => consignorRoutes}
import controllers.goods.{routes => goodsRoutes}
import models.{Address, GbEori, Index, LodgingPersonType, ProvideGrossWeight, TraderIdentity, UserAnswers}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.EmptyWaypoints
import pages.consignees.{ConsigneeEORIPage, NotifiedPartyEORIPage}
import pages.consignors.ConsignorEORIPage
import pages.goods.CommodityCodeKnownPage
import pages.predec.{
  CarrierEORIPage,
  CarrierIdentityPage,
  DeclarationPlacePage,
  LodgingPersonTypePage,
  ProvideGrossWeightPage,
  TotalGrossWeightPage
}
import queries.consignees.{ConsigneeKeyQuery, NotifiedPartyKeyQuery}
import queries.consignors.ConsignorKeyQuery

class TaskListViewModelSpec
  extends AnyFreeSpec
  with SpecBase
  with Matchers
  with ScalaCheckPropertyChecks
  with OptionValues {

  "On the task list" - {
    val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()
    val predecIdx = 0
    val consignorsIdx = 3
    val consigneesIdx = 4
    val goodsIdx = 5
    val location = "test-declaration-location"
    val totalMass = 1000
    val carrierEORI = arbitrary[GbEori].sample.value
    val name = "Name"
    val address = arbitrary[Address].sample.value

    "For the predec section" - {
      "When we have a complete section" - {
        "It will show a `complete` status" in {
          val validAnswers = {
            arbitrary[UserAnswers].sample.value
              .set(DeclarationPlacePage, location)
              .success
              .value
              .set(ProvideGrossWeightPage, ProvideGrossWeight.Overall)
              .success
              .value
              .set(TotalGrossWeightPage, BigDecimal.exact(totalMass))
              .success
              .value
              .set(LodgingPersonTypePage, LodgingPersonType.Representative)
              .success
              .value
              .set(CarrierIdentityPage, TraderIdentity.GBEORI)
              .success
              .value
              .set(CarrierEORIPage, carrierEORI)
              .success
              .value
          }

          val result = TaskListViewModel.fromAnswers(validAnswers)(messages(application))

          result.rows(predecIdx).completionStatusTag.attributes mustEqual CompletionStatus.tag(
            CompletionStatus.NotStarted
          )(messages(application))
        }
      }
    }

    "For the consignors section" - {
      "When we already have some" - {
        "we go to the consignors listing page" in {
          val answers =
            emptyUserAnswers
              .set(ConsignorEORIPage(Index(0)), GbEori("123456789000"))
              .success
              .value
              .set(ConsignorKeyQuery(Index(0)), 1)
              .success
              .value

          val result = TaskListViewModel.fromAnswers(answers)(messages(application))

          result.rows(consignorsIdx).link mustEqual consignorRoutes.AddConsignorController
            .onPageLoad(EmptyWaypoints, answers.lrn)
        }
      }
      "When have don't have any" - {
        "we go to the first consignor input" in {
          val result = TaskListViewModel.fromAnswers(emptyUserAnswers)(messages(application))

          result.rows(consignorsIdx).link mustEqual consignorRoutes.ConsignorIdentityController.onPageLoad(
            EmptyWaypoints,
            emptyUserAnswers.lrn,
            Index(0)
          )
        }
      }
    }

    "For the consignees and notified parties section" - {
      "When we already have some consignees" - {
        "we go to the consignees and notified parties check page" in {
          val answers =
            emptyUserAnswers
              .set(ConsigneeEORIPage(Index(0)), GbEori("123456789000"))
              .success
              .value
              .set(ConsigneeKeyQuery(Index(0)), 1)
              .success
              .value

          val result = TaskListViewModel.fromAnswers(answers)(messages(application))

          result.rows(consigneesIdx).link mustEqual consigneeRoutes.CheckConsigneesAndNotifiedPartiesController
            .onPageLoad(EmptyWaypoints, answers.lrn)
        }
      }

      "When we already have some notified parties and no consignees" - {
        "we go to the consignees and notified parties check page" in {
          val answers =
            emptyUserAnswers
              .set(NotifiedPartyEORIPage(Index(0)), GbEori("123456789000"))
              .success
              .value
              .set(NotifiedPartyKeyQuery(Index(0)), 1)
              .success
              .value

          val result = TaskListViewModel.fromAnswers(answers)(messages(application))

          result.rows(consigneesIdx).link mustEqual consigneeRoutes.CheckConsigneesAndNotifiedPartiesController
            .onPageLoad(EmptyWaypoints, answers.lrn)
        }
      }

      "When we don't have any" - {
        "we go to the beginning of the journey for consignees and notified parties" in {
          val result = TaskListViewModel.fromAnswers(emptyUserAnswers)(messages(application))

          result.rows(consigneesIdx).link mustEqual consigneeRoutes.AnyConsigneesKnownController
            .onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn)
        }
      }
    }

    "For the goods section" - {
      "When we already have some" - {
        "we go to the goods listing page" in {
          val answers =
            emptyUserAnswers
              .set(CommodityCodeKnownPage(Index(0)), true)
              .success
              .value

          val result = TaskListViewModel.fromAnswers(answers)(messages(application))

          result.rows(goodsIdx).link mustEqual goodsRoutes.AddGoodsController.onPageLoad(EmptyWaypoints, answers.lrn)
        }
      }
      "When have don't have any" - {
        "we go to the first good input" in {
          val result = TaskListViewModel.fromAnswers(emptyUserAnswers)(messages(application))

          result.rows(goodsIdx).link mustEqual goodsRoutes.InitialiseGoodsItemController.initialise(
            EmptyWaypoints,
            emptyUserAnswers.lrn,
            Index(0)
          )
        }
      }
    }
  }
}
