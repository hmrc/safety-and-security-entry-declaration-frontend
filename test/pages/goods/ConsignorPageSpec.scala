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

package pages.goods

import base.SpecBase
import controllers.goods.{routes => goodsRoutes}
import controllers.routes
import models.{CheckMode, GbEori, Index, NormalMode}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.consignees.{ConsigneeEORIPage, NotifiedPartyEORIPage}
import queries.consignees.{ConsigneeKeyQuery, NotifiedPartyKeyQuery}

class ConsignorPageSpec extends SpecBase with PageBehaviours {

  "ConsignorPage" - {

    beRetrievable[Int](ConsignorPage(index))

    beSettable[Int](ConsignorPage(index))

    beRemovable[Int](ConsignorPage(index))

    val consignee1Eori = arbitrary[GbEori].sample.value
    val consignee2Eori = arbitrary[GbEori].sample.value
    val consignee1Key = 1
    val consignee2Key = 2
    val notifiedParty1Eori = arbitrary[GbEori].sample.value
    val notifiedParty2Eori = arbitrary[GbEori].sample.value
    val notifiedParty1Key = 1
    val notifiedParty2Key = 2

    "must navigate in Normal Mode" - {

      "when there is one consignee and no notified parties" - {

        "to wherever the Consignee page would navigate to" in {

          val answers =
            emptyUserAnswers
              .set(ConsigneeKeyQuery(Index(0)), consignee1Key).success.value
              .set(ConsigneeEORIPage(Index(0)), consignee1Eori).success.value

          ConsignorPage(index).navigate(NormalMode, answers)
            .mustEqual(ConsigneePage(index).navigate(NormalMode, answers))
        }
      }

      "when there are no consignees and one notified party" - {

        "to wherever the Notified Party page would navigate to" in {

          val answers =
            emptyUserAnswers
              .set(NotifiedPartyKeyQuery(Index(0)), notifiedParty1Key).success.value
              .set(NotifiedPartyEORIPage(Index(0)), notifiedParty1Eori).success.value

          ConsignorPage(index).navigate(NormalMode, answers)
            .mustEqual(NotifiedPartyPage(index).navigate(NormalMode, answers))
        }
      }

      "when there is one consignee and one notified party" - {

        "to Consignee Known" in {

          val answers =
            emptyUserAnswers
              .set(ConsigneeKeyQuery(Index(0)), consignee1Key).success.value
              .set(ConsigneeEORIPage(Index(0)), consignee1Eori).success.value
              .set(NotifiedPartyKeyQuery(Index(0)), notifiedParty1Key).success.value
              .set(NotifiedPartyEORIPage(Index(0)), notifiedParty1Eori).success.value

          ConsignorPage(index).navigate(NormalMode, answers)
            .mustEqual(goodsRoutes.ConsigneeKnownController.onPageLoad(NormalMode, answers.lrn, index))
        }
      }

      "when there are multiple consignees and one notified party" - {

        "to Consignee Known" in {

          val answers =
            emptyUserAnswers
              .set(ConsigneeKeyQuery(Index(0)), consignee1Key).success.value
              .set(ConsigneeEORIPage(Index(0)), consignee1Eori).success.value
              .set(ConsigneeKeyQuery(Index(1)), consignee2Key).success.value
              .set(ConsigneeEORIPage(Index(1)), consignee2Eori).success.value
              .set(NotifiedPartyKeyQuery(Index(0)), notifiedParty1Key).success.value
              .set(NotifiedPartyEORIPage(Index(0)), notifiedParty1Eori).success.value

          ConsignorPage(index).navigate(NormalMode, answers)
            .mustEqual(goodsRoutes.ConsigneeKnownController.onPageLoad(NormalMode, answers.lrn, index))
        }
      }

      "when there is one consignee and multiple notified parties" - {

        "to Consignee Known" in {

          val answers =
            emptyUserAnswers
              .set(ConsigneeKeyQuery(Index(0)), consignee1Key).success.value
              .set(ConsigneeEORIPage(Index(0)), consignee1Eori).success.value
              .set(NotifiedPartyKeyQuery(Index(0)), notifiedParty1Key).success.value
              .set(NotifiedPartyEORIPage(Index(0)), notifiedParty1Eori).success.value
              .set(NotifiedPartyKeyQuery(Index(1)), notifiedParty2Key).success.value
              .set(NotifiedPartyEORIPage(Index(1)), notifiedParty2Eori).success.value

          ConsignorPage(index).navigate(NormalMode, answers)
            .mustEqual(goodsRoutes.ConsigneeKnownController.onPageLoad(NormalMode, answers.lrn, index))
        }
      }

      "when there are multiple consignees and no notified parties" - {

        "to Consignee" in {

          val answers =
            emptyUserAnswers
              .set(ConsigneeKeyQuery(Index(0)), consignee1Key).success.value
              .set(ConsigneeEORIPage(Index(0)), consignee1Eori).success.value
              .set(ConsigneeKeyQuery(Index(1)), consignee2Key).success.value
              .set(ConsigneeEORIPage(Index(1)), consignee2Eori).success.value

          ConsignorPage(index).navigate(NormalMode, answers)
            .mustEqual(goodsRoutes.ConsigneeController.onPageLoad(NormalMode, answers.lrn, index))
        }
      }

      "when there are no consignees and multiple notified parties" - {

        "to Notified Party" in {

          val answers =
            emptyUserAnswers
              .set(NotifiedPartyKeyQuery(Index(0)), notifiedParty1Key).success.value
              .set(NotifiedPartyEORIPage(Index(0)), notifiedParty1Eori).success.value
              .set(NotifiedPartyKeyQuery(Index(1)), notifiedParty2Key).success.value
              .set(NotifiedPartyEORIPage(Index(1)), notifiedParty2Eori).success.value

          ConsignorPage(index).navigate(NormalMode, answers)
            .mustEqual(goodsRoutes.NotifiedPartyController.onPageLoad(NormalMode, answers.lrn, index))
        }
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsignorPage(index).navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
