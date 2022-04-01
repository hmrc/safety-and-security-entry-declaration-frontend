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
import models.{CheckMode, NormalMode}
import org.scalatest.EitherValues
import pages.consignees.{AddConsigneePage, AddNotifiedPartyPage}
import play.api.mvc.QueryStringBindable

class BreadcrumbsSpec extends SpecBase with EitherValues {

  private val breadcrumb1 = AddConsigneePage.breadcrumb(NormalMode)
  private val breadcrumb2 = AddNotifiedPartyPage.breadcrumb(CheckMode)

  ".push" - {

    "must add the breadcrumb to the head of the list" - {

      "when the list is empty" in {

        Breadcrumbs(Nil).push(breadcrumb1) mustEqual Breadcrumbs(List(breadcrumb1))
      }

      "when the list is not empty" in {

        Breadcrumbs(List(breadcrumb1)).push(breadcrumb2) mustEqual Breadcrumbs(List(breadcrumb2, breadcrumb1))
      }
    }
  }

  ".fromString" - {

    "must read from a comma-separated list of valid breadcrumbs" in {

      val string = s"${breadcrumb1.urlFragment},${breadcrumb2.urlFragment}"

      Breadcrumbs.fromString(string).value mustEqual Breadcrumbs(List(breadcrumb1, breadcrumb2))
    }

    "must not read from an invalid string" in {

      Breadcrumbs.fromString("invalid") must not be defined
    }
  }

  "must bind from a query string" in {

    val bindable = implicitly[QueryStringBindable[Breadcrumbs]]

    val data = Map("key" -> List(s"${breadcrumb1.urlFragment},${breadcrumb2.urlFragment}"))
    val expectedResult = Breadcrumbs(List(breadcrumb1, breadcrumb2))

    bindable.bind("key", data).value.value mustEqual expectedResult
  }
}
