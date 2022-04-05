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

import models.NormalMode
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers

class PageSpec extends AnyFreeSpec with Matchers {

  ".updateWaypoints" - {

    "going from a regular page to a regular page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestPage1.updateWaypoints(EmptyWaypoints, TestPage2) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with a check answers page" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckConsigneePage.waypoint))
          TestPage1.updateWaypoints(waypoints, TestPage2) mustEqual waypoints
        }
      }
    }

    "going from a regular page to an add-to-list question page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestPage1.updateWaypoints(EmptyWaypoints, TestConsigneePage1) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with the add-item page of its section" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestAddConsigneePage.waypoint(NormalMode)))
          TestPage1.updateWaypoints(waypoints, TestConsigneePage1) mustEqual waypoints
        }
      }

      "when the original waypoints start with something other than the add-item page of its section" - {

        "must add the add-item page of this section to the original waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckConsigneePage.waypoint))
          TestPage1.updateWaypoints(waypoints, TestConsigneePage1)
            .mustEqual(waypoints.set(TestAddConsigneePage.waypoint(NormalMode)))
        }
      }
    }

    "going from a regular page to a check answers page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestPage1.updateWaypoints(EmptyWaypoints, TestCheckConsigneePage) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with that check answers page" - {

        "must remove the check answers page from the waypoints" in new Fixture {

          val waypoints: Waypoints =
            Waypoints(List(TestCheckConsigneePage.waypoint, TestCheckNotifiedPartyPage.waypoint))

          TestPage1.updateWaypoints(waypoints, TestCheckConsigneePage)
            .mustEqual(Waypoints(List(TestCheckNotifiedPartyPage.waypoint)))
        }
      }
    }

    "going from an add-to-list question page to an add-to-list question page in the same section" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestConsigneePage1.updateWaypoints(EmptyWaypoints, TestConsigneePage2) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints are not empty" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckConsigneePage.waypoint))
          TestConsigneePage1.updateWaypoints(waypoints, TestConsigneePage2) mustEqual waypoints
        }
      }
    }

    "going from an add-to-list question page to the add-item page for that section" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestConsigneePage1.updateWaypoints(EmptyWaypoints, TestAddConsigneePage) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with the add-item page" - {

        "must remove the add-item page from the waypoints" in new Fixture {

          val waypoints: Waypoints =
            Waypoints(List(TestAddConsigneePage.waypoint(NormalMode), TestCheckNotifiedPartyPage.waypoint))

          TestConsigneePage1.updateWaypoints(waypoints, TestAddConsigneePage)
            .mustEqual(Waypoints(List(TestCheckNotifiedPartyPage.waypoint)))
        }
      }

      "when the original waypoints start with something other than the add-item page" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints =
            Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestConsigneePage1.updateWaypoints(waypoints, TestAddConsigneePage) mustEqual waypoints
        }
      }
    }

    "going from an add-to-list question page to a check answers page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestConsigneePage1.updateWaypoints(EmptyWaypoints, TestCheckConsigneePage) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with the check answers page" - {

        "must remove the check answers page from the waypoints" in new Fixture {

          val waypoints: Waypoints =
            Waypoints(List(TestCheckConsigneePage.waypoint, TestCheckNotifiedPartyPage.waypoint))

          TestConsigneePage1.updateWaypoints(waypoints, TestCheckConsigneePage)
            .mustEqual(Waypoints(List(TestCheckNotifiedPartyPage.waypoint)))
        }
      }

      "when the original waypoints start with something other than the check answers page" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints =
            Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestConsigneePage1.updateWaypoints(waypoints, TestCheckConsigneePage) mustEqual waypoints
        }
      }
    }

    "going from an add-item page to an add-to-list question page in this section" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestAddConsigneePage.updateWaypoints(EmptyWaypoints, TestConsigneePage1) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints are not empty" - {

        "must add itself to the waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestAddConsigneePage.updateWaypoints(waypoints, TestConsigneePage1)
            .mustEqual(waypoints.set(TestAddConsigneePage.waypoint(NormalMode)))
        }
      }
    }

    "going from an add-item page to an add-to-list question page in another section" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestAddConsigneePage.updateWaypoints(EmptyWaypoints, TestNotifiedPartyPage1) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints are not empty" - {

        "must return the original waypoints with the add-item page of the new section added" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestAddConsigneePage.updateWaypoints(waypoints, TestNotifiedPartyPage1)
            .mustEqual(waypoints.set(TestAddNotifiedPartyPage.waypoint(NormalMode)))
        }
      }
    }

    "going from an add-item page to a regular page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestAddConsigneePage.updateWaypoints(EmptyWaypoints, TestPage1) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints are not empty" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestAddConsigneePage.updateWaypoints(waypoints, TestPage1) mustEqual waypoints
        }
      }
    }

    "going from an add-item page to a check answers page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestAddConsigneePage.updateWaypoints(EmptyWaypoints, TestCheckConsigneePage) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with the check answers page" - {

        "must remove the check answers page from the waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckConsigneePage.waypoint))

          TestAddConsigneePage.updateWaypoints(waypoints, TestCheckConsigneePage) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with something other than the check answers page" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestAddConsigneePage.updateWaypoints(waypoints, TestCheckConsigneePage) mustEqual waypoints
        }
      }
    }

    "going from a check answers page to a regular page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestCheckConsigneePage.updateWaypoints(EmptyWaypoints, TestPage1) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints are not empty" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestCheckConsigneePage.updateWaypoints(waypoints, TestCheckConsigneePage) mustEqual waypoints
        }
      }
    }

    "going from a check answers page to an add-item page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestCheckConsigneePage.updateWaypoints(EmptyWaypoints, TestAddConsigneePage) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with the add-item page" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints =
            Waypoints(List(TestAddConsigneePage.waypoint(NormalMode), TestCheckNotifiedPartyPage.waypoint))

          TestCheckConsigneePage.updateWaypoints(waypoints, TestAddConsigneePage)
            .mustEqual(Waypoints(List(TestCheckNotifiedPartyPage.waypoint)))
        }
      }

      "when the original waypoints start with something other than the add-item page" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestCheckConsigneePage.updateWaypoints(waypoints, TestAddConsigneePage) mustEqual waypoints
        }
      }
    }

    "going from a check answers page to a different check answers page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestCheckConsigneePage.updateWaypoints(EmptyWaypoints, TestCheckNotifiedPartyPage) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with the target check answers page" - {

        "must remove the check answers page from the waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestCheckConsigneePage.updateWaypoints(waypoints, TestCheckNotifiedPartyPage) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with something other than the target check answers page" - {

        "must return the original waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestAddNotifiedPartyPage.waypoint(NormalMode)))

          TestCheckConsigneePage.updateWaypoints(waypoints, TestCheckNotifiedPartyPage) mustEqual waypoints
        }
      }
    }

    "going from a check answers page to an add-to-list question page" - {

      "when the original waypoints are empty" - {

        "must return empty waypoints" in new Fixture {

          TestCheckConsigneePage.updateWaypoints(EmptyWaypoints, TestNotifiedPartyPage1) mustEqual EmptyWaypoints
        }
      }

      "when the original waypoints start with the add-item page of the target page's section" - {

        "must return the original the waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestAddNotifiedPartyPage.waypoint(NormalMode)))

          TestCheckConsigneePage.updateWaypoints(waypoints, TestNotifiedPartyPage1) mustEqual waypoints
        }
      }

      "when the original waypoints start with something other than the add-item page of the target page's section" - {

        "must add the add-item page of the target page's section to the waypoints" in new Fixture {

          val waypoints: Waypoints = Waypoints(List(TestCheckNotifiedPartyPage.waypoint))

          TestCheckConsigneePage.updateWaypoints(waypoints, TestNotifiedPartyPage1)
            .mustEqual(waypoints.set(TestAddNotifiedPartyPage.waypoint(NormalMode)))
        }
      }
    }
  }

  trait Fixture {
    case object TestPage1 extends Page

    case object TestPage2 extends Page

    case object TestAddConsigneePage extends AddItemPage {
      override val normalModeUrlFragment: String = "add-consignee"
      override val checkModeUrlFragment: String = "change-consignee"
    }

    case object TestConsigneePage1 extends Page with AddToListQuestionPage {
      override val section: AddToListSection = ConsigneeSection
      override val addItemWaypoint: Waypoint = TestAddConsigneePage.waypoint(NormalMode)
    }

    case object TestConsigneePage2 extends Page with AddToListQuestionPage {
      override val section: AddToListSection = ConsigneeSection
      override val addItemWaypoint: Waypoint = TestAddConsigneePage.waypoint(NormalMode)
    }

    case object TestAddNotifiedPartyPage extends AddItemPage {
      override val normalModeUrlFragment: String = "add-thing"
      override val checkModeUrlFragment: String = "change-thing"
    }

    case object TestNotifiedPartyPage1 extends Page with AddToListQuestionPage {
      override val section: AddToListSection = NotifiedPartySection
      override val addItemWaypoint: Waypoint = TestAddNotifiedPartyPage.waypoint(NormalMode)
    }

    case object TestNotifiedPartyPage2 extends Page with AddToListQuestionPage {
      override val section: AddToListSection = NotifiedPartySection
      override val addItemWaypoint: Waypoint = TestAddNotifiedPartyPage.waypoint(NormalMode)
    }

    case object TestCheckConsigneePage extends CheckAnswersPage {
      override val urlFragment: String = "check-consignee"
    }

    case object TestCheckNotifiedPartyPage extends CheckAnswersPage {
      override val urlFragment: String = "check-notified-party"
    }
  }
}