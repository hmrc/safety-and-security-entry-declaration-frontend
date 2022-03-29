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

package pages

import base.SpecBase
import models.Index
import pages.consignees._

class BreadcrumbSpec extends SpecBase {

  ".fromString" - {

    "must return AddConsigneePage when given its breadcrumb" in {

      Breadcrumb.fromString("add-consignee").value mustEqual AddConsigneePage
    }

    "must return AddNotifiedPartyPage when given its breadcrumb" in {

      Breadcrumb.fromString("add-notified-party").value mustEqual AddNotifiedPartyPage
    }

    "must return CheckConsigneesAndNotifiedPartiesPage when given its breadcrumb" in {

      Breadcrumb.fromString("check-consignees-notified-parties").value mustEqual CheckConsigneesAndNotifiedPartiesPage
    }

    "must return CheckConsigneePage when given its breadcrumb" in {

      Breadcrumb.fromString("check-consignee-1").value mustEqual CheckConsigneePage(Index(0))
    }

    "must return CheckNotifiedPartyPage when given its breadcrumb" in {

      Breadcrumb.fromString("check-notified-party-1").value mustEqual CheckNotifiedPartyPage(Index(0))
    }
  }
}
