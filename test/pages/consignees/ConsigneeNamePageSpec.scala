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
import pages.{Waypoints, EmptyWaypoints}
import pages.behaviours.PageBehaviours

class ConsigneeNamePageSpec extends SpecBase with PageBehaviours {

  "ConsigneeNamePage" - {

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to consignee address" in {

        ConsigneeNamePage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(
            routes.ConsigneeAddressController.onPageLoad(waypoints, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current waypoint is AddConsignee" - {

      val waypoints = Waypoints(List(AddConsigneePage.waypoint(NormalMode)))

      "to consignee address" in {

        ConsigneeNamePage(index)
          .navigate(waypoints, emptyUserAnswers)
          .mustEqual(
            routes.ConsigneeAddressController.onPageLoad(waypoints, emptyUserAnswers.lrn, index)
          )
      }
    }

    "must navigate when the current waypoint is Check Consignee" - {

      val waypoints = Waypoints(List(CheckConsigneePage(index).waypoint))

      "and Consignee Address has been answered" - {

        "to Check Consignee with the current waypoint removed" in {

          val answers =
            emptyUserAnswers
              .set(ConsigneeAddressPage(index), Address("street", "city", "AA11 1AA", Country("GB", "United Kingdom")))
              .success.value

          ConsigneeNamePage(index).navigate(waypoints, answers)
            .mustEqual(routes.CheckConsigneeController.onPageLoad(waypoints.pop, answers.lrn, index))
        }
      }

      "and Consignee Address has not been answered" - {

        "to Consignee Address" in {

          ConsigneeNamePage(index).navigate(waypoints, emptyUserAnswers)
            .mustEqual(routes.ConsigneeAddressController.onPageLoad(waypoints, emptyUserAnswers.lrn, index))
        }
      }
    }
  }
}
