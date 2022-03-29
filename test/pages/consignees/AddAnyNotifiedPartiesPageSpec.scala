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
import models.{ConsigneeIdentity, GbEori, Index, NotifiedPartyIdentity}
import pages.Breadcrumbs
import pages.behaviours.PageBehaviours
import queries.consignees.{ConsigneeKeyQuery, NotifiedPartyKeyQuery}

class AddAnyNotifiedPartiesPageSpec extends SpecBase with PageBehaviours {

  "AddAnyNotifiedPartiesPage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = Breadcrumbs.empty

      "to Notified Party Identity when the answer is yes" in {

        val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, true).success.value

        AddAnyNotifiedPartiesPage.navigate(breadcrumbs, answers)
          .mustEqual(routes.NotifiedPartyIdentityController.onPageLoad(breadcrumbs, answers.lrn, Index(0)))
      }

      "to Check Consignees and Notified Parties when the answer is no" in {

        val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, false).success.value

        AddAnyNotifiedPartiesPage.navigate(breadcrumbs, answers)
          .mustEqual(routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(breadcrumbs, answers.lrn))
      }
    }

    "must navigate when the current breadcrumb is CheckConsigneesAndNotifiedParties" - {

      val breadcrumbs = Breadcrumbs(List(CheckConsigneesAndNotifiedPartiesPage))

      "when the answer is yes" - {

        "and there are already some notified parties" - {

          "to Check Consignees and Notified Parties with the current breadcrumb removed" in {

            val answers =
              emptyUserAnswers
                .set(NotifiedPartyIdentityPage(index), NotifiedPartyIdentity.GBEORI).success.value
                .set(NotifiedPartyEORIPage(index), GbEori("123456789000")).success.value
                .set(NotifiedPartyKeyQuery(index), 1).success.value
                .set(AddAnyNotifiedPartiesPage, true).success.value

            AddAnyNotifiedPartiesPage.navigate(breadcrumbs, answers)
              .mustEqual(routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(breadcrumbs.pop, answers.lrn))
          }
        }

        "and there are no notified parties" - {

          "to Notified Party for index 0" in {

            val answers =
              emptyUserAnswers
                .set(AddAnyNotifiedPartiesPage, true).success.value

            AddAnyNotifiedPartiesPage.navigate(breadcrumbs, answers)
              .mustEqual(routes.NotifiedPartyIdentityController.onPageLoad(breadcrumbs, answers.lrn, index))
          }
        }
      }

      "when the answer is no" - {

        "and there are some consignees" - {

          "to Check Consignees and Notified Parties with the current breadcrumb removed" in {

            val answers =
              emptyUserAnswers
                .set(ConsigneeIdentityPage(index), ConsigneeIdentity.GBEORI).success.value
                .set(ConsigneeEORIPage(index), GbEori("123456789000")).success.value
                .set(ConsigneeKeyQuery(index), 1).success.value
                .set(AddAnyNotifiedPartiesPage, false).success.value

            AddAnyNotifiedPartiesPage.navigate(breadcrumbs, answers)
              .mustEqual(routes.CheckConsigneesAndNotifiedPartiesController.onPageLoad(breadcrumbs.pop, answers.lrn))
          }
        }

        "and there are no consignees" - {

          "to Any Consignees Known" in {

            val answers = emptyUserAnswers.set(AddAnyNotifiedPartiesPage, false).success.value

            AddAnyNotifiedPartiesPage.navigate(breadcrumbs, answers)
              .mustEqual(routes.AnyConsigneesKnownController.onPageLoad(breadcrumbs, answers.lrn))
          }
        }
      }
    }
  }
}
