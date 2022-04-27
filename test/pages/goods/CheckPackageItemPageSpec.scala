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
import controllers.goods.routes
import generators.Generators
import models.{Index, NormalMode}
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.{EmptyWaypoints, Waypoints}

class CheckPackageItemPageSpec extends SpecBase with ScalaCheckPropertyChecks with Generators {

  ".fromString" - {

    "must read from a valid waypoint" in {

      forAll(Gen.choose(1, 999), Gen.choose(1, 999)) {
        case (index1, index2) =>
          val waypoint = s"check-package-$index1-$index2"

          CheckPackageItemPage.waypointFromString(waypoint).value
            .mustEqual(CheckPackageItemPage(Index(index1 - 1), Index(index2 - 1)).waypoint)
      }
    }

    "must not read from an invalid waypoint" in {

      forAll(nonEmptyString) {
        s =>

          whenever(!s.startsWith("check-package-")) {
            CheckPackageItemPage.waypointFromString(s) must not be defined
          }
      }
    }
  }

  "must navigate when there are no waypoints" - {

    val waypoints = EmptyWaypoints

    "to Add Package" in {

      CheckPackageItemPage(index, index).navigate(waypoints, emptyUserAnswers)
        .mustEqual(routes.AddPackageController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
    }
  }

  "must navigate when the current waypoint is Add Package" - {

    val waypoints = Waypoints(List(AddPackagePage(index).waypoint(NormalMode)))

    "to Add Package with the current waypoint removed" in {

      CheckPackageItemPage(index, index).navigate(waypoints, emptyUserAnswers)
        .mustEqual(routes.AddPackageController.onPageLoad(EmptyWaypoints, emptyUserAnswers.lrn, index))
    }
  }
}
