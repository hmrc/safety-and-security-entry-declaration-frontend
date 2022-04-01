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
import models.{Address, Country, NormalMode}
import pages.{Breadcrumbs, EmptyBreadcrumbs}
import pages.behaviours.PageBehaviours

class NotifiedPartyNamePageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyNamePage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = EmptyBreadcrumbs

      "to notified party address" in {

        NotifiedPartyNamePage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            routes.NotifiedPartyAddressController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current breadcrumb is AddNotifiedParty" - {

      val breadcrumbs = Breadcrumbs(List(AddNotifiedPartyPage.breadcrumb(NormalMode)))

      "to notified party address" in {

        NotifiedPartyNamePage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            routes.NotifiedPartyAddressController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current breadcrumb is CheckNotifiedParty" - {

      val breadcrumbs = Breadcrumbs(List(CheckNotifiedPartyPage(index).breadcrumb))

      "when Notified Party Address has been answered" - {

        "to Check Notified Party with the current breadcrumb removed" in {

          val answers =
            emptyUserAnswers
              .set(NotifiedPartyAddressPage(index), Address("street", "town", "post code", Country("GB", "United Kingdom")))
              .success.value

          NotifiedPartyNamePage(index).navigate(breadcrumbs, answers)
            .mustEqual(routes.CheckNotifiedPartyController.onPageLoad(breadcrumbs.pop, answers.lrn, index))
        }
      }

      "when Notified Party Address has not been answered" - {

        "to Notified Party Address" in {

          NotifiedPartyNamePage(index).navigate(breadcrumbs, emptyUserAnswers)
            .mustEqual(routes.NotifiedPartyAddressController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index))
        }
      }
    }
  }
}
