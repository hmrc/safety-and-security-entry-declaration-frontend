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

package pages.consignors

import base.SpecBase
import controllers.consignors.routes
import models.{Address, Country, NormalMode}
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class ConsignorNamePageSpec extends SpecBase with PageBehaviours {

  "ConsignorNamePage" - {
    
    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to consignor address" in {

        ConsignorNamePage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(routes.ConsignorAddressController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
      }
    }

    "must navigate when the current waypoint is AddConsignor" - {

      val waypoints = Waypoints(List(AddConsignorPage.waypoint(NormalMode)))

      "to consignor address" in {

        ConsignorNamePage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(
            routes.ConsignorAddressController.onPageLoad(waypoints, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current waypoint is Check Consignor" - {

      val waypoints = Waypoints(List(CheckConsignorPage(index).waypoint))

      "and Consignor Address has been answered" - {

        "to Check Consignor with the current waypoint removed" in {

          val answers =
            emptyUserAnswers
              .set(ConsignorAddressPage(index), Address("street", "city", "AA11 1AA", Country("GB", "United Kingdom")))
              .success.value

          ConsignorNamePage(index).navigate(waypoints, answers)
            .mustEqual(routes.CheckConsignorController.onPageLoad(EmptyWaypoints, answers.lrn, index))
        }
      }

      "and Consignor Address has not been answered" - {

        "to Consignor Address" in {

          ConsignorNamePage(index).navigate(waypoints, emptyUserAnswers)
            .mustEqual(routes.ConsignorAddressController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
        }
      }
    }
  }
}
