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
import models.NormalMode
import pages.{Breadcrumbs, EmptyBreadcrumbs}
import pages.behaviours.PageBehaviours

class NotifiedPartyEORIPageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyEORIPage" - {

    "must navigate when there are no breadcrumbs" - {

      val breadcrumbs = EmptyBreadcrumbs

      "to Check Notified Party" in {

        NotifiedPartyEORIPage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            routes.CheckNotifiedPartyController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current breadcrumb is AddNotifiedParty" - {

      val breadcrumbs = Breadcrumbs(List(AddNotifiedPartyPage.breadcrumb((NormalMode))))

      "to Check Notified Party" in {

        NotifiedPartyEORIPage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            routes.CheckNotifiedPartyController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current breadcrumb is CheckNotifiedParty" - {

      val breadcrumbs = Breadcrumbs(List(CheckNotifiedPartyPage(index).breadcrumb))

      "to Check Notified Party with the current breadcrumb removed" in {

        NotifiedPartyEORIPage(index)
          .navigate(breadcrumbs, emptyUserAnswers)
          .mustEqual(
            routes.CheckNotifiedPartyController.onPageLoad(breadcrumbs.pop, emptyUserAnswers.lrn, index)
          )
      }
    }
  }
}
