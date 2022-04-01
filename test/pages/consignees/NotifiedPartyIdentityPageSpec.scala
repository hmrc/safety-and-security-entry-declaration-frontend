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

package pages.consignees

import base.SpecBase
import controllers.consignees.routes
import models.{Address, Country, GbEori, NormalMode, NotifiedPartyIdentity}
import models.NotifiedPartyIdentity.{GBEORI, NameAddress}
import pages.{Breadcrumbs, EmptyBreadcrumbs}
import pages.behaviours.PageBehaviours

class NotifiedPartyIdentityPageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyIdentityPage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = EmptyBreadcrumbs

      "to Consignee EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(NotifiedPartyIdentityPage(index), GBEORI).success.value

        NotifiedPartyIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(routes.NotifiedPartyEORIController.onPageLoad(breadcrumbs, answers.lrn, index))
      }

      "to Consignee Name when answered `name & address`" in {
        val answers =
          emptyUserAnswers.set(NotifiedPartyIdentityPage(index), NameAddress).success.value

        NotifiedPartyIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(routes.NotifiedPartyNameController.onPageLoad(breadcrumbs, answers.lrn, index))
      }
    }

    "must navigate when the current breadcrumb is AddNotifiedParty" - {

      val breadcrumbs = Breadcrumbs(List(AddNotifiedPartyPage.breadcrumb(NormalMode)))

      "to Consignee EORI when answered `gb eori`" in {
        val answers = emptyUserAnswers.set(NotifiedPartyIdentityPage(index), GBEORI).success.value

        NotifiedPartyIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(routes.NotifiedPartyEORIController.onPageLoad(breadcrumbs, answers.lrn, index))
      }

      "to Consignee Name when answered `name & address`" in {
        val answers =
          emptyUserAnswers.set(NotifiedPartyIdentityPage(index), NameAddress).success.value

        NotifiedPartyIdentityPage(index)
          .navigate(breadcrumbs, answers)
          .mustEqual(routes.NotifiedPartyNameController.onPageLoad(breadcrumbs, answers.lrn, index))
      }
    }

    "must navigate when the current breadcrumb is CheckNotifiedParty" - {

      val breadcrumbs = Breadcrumbs(List(CheckNotifiedPartyPage(index).breadcrumb))

      "when the answer is GB EORI" - {

        "And Notified Party EORI has already been answered" - {

          "to Check Notified Party with the current breadcrumb removed" in {

            val answers =
              emptyUserAnswers
                .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
                .set(NotifiedPartyIdentityPage(index), GBEORI).success.value

            NotifiedPartyIdentityPage(index).navigate(breadcrumbs, answers)
              .mustEqual(routes.CheckNotifiedPartyController.onPageLoad(breadcrumbs.pop, lrn, index))
          }
        }

        "And Notified Party EORI has not been answered" - {

          "to Notified Party EORI" in {

            val answers = emptyUserAnswers.set(NotifiedPartyIdentityPage(index), GBEORI).success.value

            NotifiedPartyIdentityPage(index).navigate(breadcrumbs, answers)
              .mustEqual(routes.NotifiedPartyEORIController.onPageLoad(breadcrumbs, lrn, index))
          }
        }
      }

      "when the answer is Name and Address" - {

        "and Notified Party Name has already been answered" - {

          "to Check Notified Party with the current breadcrumb removed" in {

            val answers =
              emptyUserAnswers
                .set(NotifiedPartyNamePage(index), "Name").success.value
                .set(NotifiedPartyIdentityPage(index), NameAddress).success.value

            NotifiedPartyIdentityPage(index).navigate(breadcrumbs, answers)
              .mustEqual(routes.CheckNotifiedPartyController.onPageLoad(breadcrumbs.pop, lrn, index))
          }
        }

        "And Notified Party Name has not been answered" - {

          "to Notified Party Name" in {

            val answers = emptyUserAnswers.set(NotifiedPartyIdentityPage(index), NameAddress).success.value

            NotifiedPartyIdentityPage(index).navigate(breadcrumbs, answers)
              .mustEqual(routes.NotifiedPartyNameController.onPageLoad(breadcrumbs, lrn, index))
          }
        }
      }
    }

    "must remove Notified Party EORI when the answer is Name and Address" in {

      val answers =
        emptyUserAnswers
          .set(NotifiedPartyIdentityPage(index), NotifiedPartyIdentity.NameAddress).success.value
          .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value

      val result = NotifiedPartyIdentityPage(index).cleanup(Some(NotifiedPartyIdentity.NameAddress), answers).success.value

      result mustEqual answers.remove(NotifiedPartyEORIPage(index)).success.value
    }

    "must remove Notified Party Name and Notified Party Address when the answer is EORI" in {

      val answers =
        emptyUserAnswers
          .set(NotifiedPartyIdentityPage(index), NotifiedPartyIdentity.GBEORI).success.value
          .set(NotifiedPartyNamePage(index), "name").success.value
          .set(NotifiedPartyAddressPage(index), Address("street", "town", "post code", Country("GB", "United Kingdom"))).success.value

      val result = NotifiedPartyIdentityPage(index).cleanup(Some(NotifiedPartyIdentity.GBEORI), answers).success.value

      result.mustEqual(
        answers
          .remove(NotifiedPartyNamePage(index)).success.value
          .remove(NotifiedPartyAddressPage(index)).success.value)
    }
  }
}
