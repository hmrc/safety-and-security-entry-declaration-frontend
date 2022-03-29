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
import controllers.consignees.{routes => consigneesRoutes}
import generators.Generators
import models.Index
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.Breadcrumbs

class CheckNotifiedPartyPageSpec extends SpecBase with ScalaCheckPropertyChecks with Generators {

  ".fromString" - {

    "must read from a valid breadcrumb" in {

      forAll(Gen.choose(1, 999)) {
        index =>
          val breadcrumb = s"check-notified-party-$index"

          CheckNotifiedPartyPage.fromString(breadcrumb).value mustEqual CheckNotifiedPartyPage(Index(index - 1))
      }
    }

    "must not read from an invalid breadcrumb" in {

      forAll(nonEmptyString) {
        s =>

          whenever(!s.startsWith("check-notified-party-")) {
            CheckNotifiedPartyPage.fromString(s) must not be defined
          }
      }
    }
  }


  "must navigate when there are no breadcrumbs" - {

    val breadcrumbs = Breadcrumbs.empty

    "to Add Notified Party" in {

      CheckNotifiedPartyPage(index).navigate(breadcrumbs, emptyUserAnswers)
        .mustEqual(consigneesRoutes.AddNotifiedPartyController.onPageLoad(breadcrumbs, emptyUserAnswers.lrn))
    }
  }

  "must navigate when the current breadcrumb is AddNotifiedParty" - {

    val breadcrumbs = Breadcrumbs(List(AddNotifiedPartyPage))

    "to Add Notified Party with the first breadcrumb removed" in {

      CheckNotifiedPartyPage(index).navigate(breadcrumbs, emptyUserAnswers)
        .mustEqual(consigneesRoutes.AddNotifiedPartyController.onPageLoad(breadcrumbs.pop, emptyUserAnswers.lrn))
    }
  }
}
