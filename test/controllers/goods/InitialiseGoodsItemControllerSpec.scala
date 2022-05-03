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

package controllers.goods

import base.SpecBase
import config.IndexLimits.maxGoods
import controllers.{routes => baseRoutes}
import models.{GbEori, Index, PlaceOfLoading, PlaceOfUnloading, UserAnswers}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito
import org.mockito.Mockito.{times, verify, when}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.consignees.{ConsigneeEORIPage, NotifiedPartyEORIPage}
import pages.consignors.ConsignorEORIPage
import pages.goods.{ConsigneePage, ConsignorPage, InitialiseGoodsItemPage, LoadingPlacePage, NotifiedPartyPage, UnloadingPlacePage}
import pages.routedetails.{PlaceOfLoadingPage, PlaceOfUnloadingPage}
import play.api.Application
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import queries.consignees.{ConsigneeKeyQuery, NotifiedPartyKeyQuery}
import queries.consignors.ConsignorKeyQuery
import repositories.SessionRepository

import scala.concurrent.Future

class InitialiseGoodsItemControllerSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {

  lazy val request =
    FakeRequest(GET, routes.InitialiseGoodsItemController.initialise(EmptyWaypoints, lrn, index).url)

  val mockSessionRepository: SessionRepository = mock[SessionRepository]

  override def beforeEach(): Unit = {
    Mockito.reset(mockSessionRepository)
    when(mockSessionRepository.set(any())) thenReturn Future.successful(true)
    super.beforeEach()
  }

  def buildApp(answers: UserAnswers): Application =
    applicationBuilder(Some(answers))
      .overrides(bind[SessionRepository].toInstance(mockSessionRepository))
      .build()

  "Initialise Goods Item Controller" - {

    "must set this item's Consignor when there is only one consignor" in {

      val answers =
        emptyUserAnswers
          .set(ConsignorKeyQuery(index), 1).success.value
          .set(ConsignorEORIPage(index), GbEori("123456798000")).success.value

      val expectedAnswers = answers.set(ConsignorPage(index), 1).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must not set this item's Consignor when there are multiple consignors" in {

      val answers =
        emptyUserAnswers
          .set(ConsignorKeyQuery(index), 1).success.value
          .set(ConsignorEORIPage(index), GbEori("123456798000")).success.value
          .set(ConsignorKeyQuery(Index(1)), 2).success.value
          .set(ConsignorEORIPage(Index(1)), GbEori("123456789001")).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(answers))
      }
    }

    "must set this item's Consignee when there is only one consignee and no notified parties" in {

      val answers =
        emptyUserAnswers
          .set(ConsigneeKeyQuery(index), 1).success.value
          .set(ConsigneeEORIPage(index), GbEori("123456798000")).success.value

      val expectedAnswers = answers.set(ConsigneePage(index), 1).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must not set this item's Consignee when there are multiple consignees and no notified parties" in {

      val answers =
        emptyUserAnswers
          .set(ConsigneeKeyQuery(index), 1).success.value
          .set(ConsigneeEORIPage(index), GbEori("123456798000")).success.value
          .set(ConsigneeKeyQuery(Index(1)), 2).success.value
          .set(ConsigneeEORIPage(Index(1)), GbEori("123456798001")).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(answers))
      }
    }

    "must set this item's Notified Party when there is only one notified party and no consignees" in {

      val answers =
        emptyUserAnswers
          .set(NotifiedPartyKeyQuery(index), 1).success.value
          .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value

      val expectedAnswers = answers.set(NotifiedPartyPage(index), 1).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must not set this item's Notified Party when there are multiple notified parties and no consignees" in {

      val answers =
        emptyUserAnswers
          .set(NotifiedPartyKeyQuery(index), 1).success.value
          .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
          .set(NotifiedPartyKeyQuery(Index(1)), 2).success.value
          .set(NotifiedPartyEORIPage(Index(1)), GbEori("123456789001")).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(answers))
      }
    }

    "must not set this item's Consignee or Notified Party when there is one consignee and one notified party" in {

      val answers =
        emptyUserAnswers
          .set(ConsigneeKeyQuery(index), 1).success.value
          .set(ConsigneeEORIPage(index), GbEori("123456798000")).success.value
          .set(NotifiedPartyKeyQuery(index), 1).success.value
          .set(NotifiedPartyEORIPage(index), GbEori("123456798000")).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(answers))
      }
    }

    "must set this item's Loading Place when there is only one place of loading" in {

      val placeOfLoading = arbitrary[PlaceOfLoading].sample.value

      val answers = emptyUserAnswers.set(PlaceOfLoadingPage(index), placeOfLoading).success.value

      val expectedAnswers = answers.set(LoadingPlacePage(index), placeOfLoading.key).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must not set this item's Loading place when there are multiple places of loading" in {

      val placeOfLoading1 = arbitrary[PlaceOfLoading].sample.value
      val placeOfLoading2 = arbitrary[PlaceOfLoading].sample.value

      val answers =
        emptyUserAnswers
          .set(PlaceOfLoadingPage(index), placeOfLoading1).success.value
          .set(PlaceOfLoadingPage(Index(1)), placeOfLoading2).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(answers))
      }
    }

    "must set this item's Unloading Place when there is only one place of unloading" in {

      val placeOfUnloading = arbitrary[PlaceOfUnloading].sample.value

      val answers = emptyUserAnswers.set(PlaceOfUnloadingPage(index), placeOfUnloading).success.value

      val expectedAnswers = answers.set(UnloadingPlacePage(index), placeOfUnloading.key).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must not set this item's Unloading place when there are multiple places of loading" in {

      val placeOfUnloading1 = arbitrary[PlaceOfUnloading].sample.value
      val placeOfUnloading2 = arbitrary[PlaceOfUnloading].sample.value

      val answers =
        emptyUserAnswers
          .set(PlaceOfUnloadingPage(index), placeOfUnloading1).success.value
          .set(PlaceOfUnloadingPage(Index(1)), placeOfUnloading2).success.value

      val application = buildApp(answers)

      running(application) {

        route(application, request).value.futureValue
        verify(mockSessionRepository, times(1)).set(eqTo(answers))
      }
    }

    "must redirect to the next page" in {

      val application = buildApp(emptyUserAnswers)

      running(application) {

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value
          .mustEqual(InitialiseGoodsItemPage(index).navigate(EmptyWaypoints, emptyUserAnswers).url)
      }
    }

    "must redirect to Journey Recovery for a GET if the item index is equal to or higher than that maximum" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(GET, routes.InitialiseGoodsItemController.initialise(EmptyWaypoints, lrn, Index(maxGoods)).url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
