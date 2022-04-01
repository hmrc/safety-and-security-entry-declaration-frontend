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

  ".updateBreadcrumbs" - {

    "going from a regular page to a regular page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestPage1.updateBreadcrumbs(EmptyBreadcrumbs, TestPage2) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs start with a check answers page" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckConsigneePage.breadcrumb))
          TestPage1.updateBreadcrumbs(breadcrumbs, TestPage2) mustEqual breadcrumbs
        }
      }
    }

    "going from a regular page to an add-to-list question page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestPage1.updateBreadcrumbs(EmptyBreadcrumbs, TestConsigneePage1) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs start with the add-item page of its section" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestAddConsigneePage.breadcrumb(NormalMode)))
          TestPage1.updateBreadcrumbs(breadcrumbs, TestConsigneePage1) mustEqual breadcrumbs
        }
      }

      "when the original breadcrumbs start with something other than the add-item page of its section" - {

        "must add the add-item page of this section to the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckConsigneePage.breadcrumb))
          TestPage1.updateBreadcrumbs(breadcrumbs, TestConsigneePage1)
            .mustEqual(breadcrumbs.push(TestAddConsigneePage.breadcrumb(NormalMode)))
        }
      }
    }

    "going from a regular page to a check answers page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestPage1.updateBreadcrumbs(EmptyBreadcrumbs, TestCheckConsigneePage) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs start with that check answers page" - {

        "must remove the check answers page from the breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs =
            Breadcrumbs(List(TestCheckConsigneePage.breadcrumb, TestCheckNotifiedPartyPage.breadcrumb))

          TestPage1.updateBreadcrumbs(breadcrumbs, TestCheckConsigneePage)
            .mustEqual(breadcrumbs.pop)
        }
      }
    }

    "going from an add-to-list question page to an add-to-list question page in the same section" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestConsigneePage1.updateBreadcrumbs(EmptyBreadcrumbs, TestConsigneePage2) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs are not empty" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckConsigneePage.breadcrumb))
          TestConsigneePage1.updateBreadcrumbs(breadcrumbs, TestConsigneePage2) mustEqual breadcrumbs
        }
      }
    }

    "going from an add-to-list question page to the add-item page for that section" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestConsigneePage1.updateBreadcrumbs(EmptyBreadcrumbs, TestAddConsigneePage) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs start with the add-item page" - {

        "must remove the add-item page from the breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs =
            Breadcrumbs(List(TestAddConsigneePage.breadcrumb(NormalMode), TestCheckNotifiedPartyPage.breadcrumb))

          TestConsigneePage1.updateBreadcrumbs(breadcrumbs, TestAddConsigneePage) mustEqual breadcrumbs.pop
        }
      }

      "when the original breadcrumbs start with something other than the add-item page" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs =
            Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestConsigneePage1.updateBreadcrumbs(breadcrumbs, TestAddConsigneePage) mustEqual breadcrumbs
        }
      }
    }

    "going from an add-to-list question page to a check answers page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestConsigneePage1.updateBreadcrumbs(EmptyBreadcrumbs, TestCheckConsigneePage) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs start with the check answers page" - {

        "must remove the check answers page from the breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs =
            Breadcrumbs(List(TestCheckConsigneePage.breadcrumb, TestCheckNotifiedPartyPage.breadcrumb))

          TestConsigneePage1.updateBreadcrumbs(breadcrumbs, TestCheckConsigneePage) mustEqual breadcrumbs.pop
        }
      }

      "when the original breadcrumbs start with something other than the check answers page" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs =
            Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestConsigneePage1.updateBreadcrumbs(breadcrumbs, TestCheckConsigneePage) mustEqual breadcrumbs
        }
      }
    }

    "going from an add-item page to an add-to-list question page in this section" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestAddConsigneePage.updateBreadcrumbs(EmptyBreadcrumbs, TestConsigneePage1) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs are not empty" - {

        "must add itself to the breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestAddConsigneePage.updateBreadcrumbs(breadcrumbs, TestConsigneePage1)
            .mustEqual(breadcrumbs.push(TestAddConsigneePage.breadcrumb(NormalMode)))
        }
      }
    }

    "going from an add-item page to an add-to-list question page in another section" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestAddConsigneePage.updateBreadcrumbs(EmptyBreadcrumbs, TestNotifiedPartyPage1) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs are not empty" - {

        "must return the original breadcrumbs with the add-item page of the new section added" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestAddConsigneePage.updateBreadcrumbs(breadcrumbs, TestNotifiedPartyPage1)
            .mustEqual(breadcrumbs.push(TestAddNotifiedPartyPage.breadcrumb(NormalMode)))
        }
      }
    }

    "going from an add-item page to a regular page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestAddConsigneePage.updateBreadcrumbs(EmptyBreadcrumbs, TestPage1) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs are not empty" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestAddConsigneePage.updateBreadcrumbs(breadcrumbs, TestPage1) mustEqual breadcrumbs
        }
      }
    }

    "going from an add-item page to a check answers page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestAddConsigneePage.updateBreadcrumbs(EmptyBreadcrumbs, TestCheckConsigneePage) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs start with the check answers page" - {

        "must remove the check answers page from the breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckConsigneePage.breadcrumb))

          TestAddConsigneePage.updateBreadcrumbs(breadcrumbs, TestCheckConsigneePage) mustEqual breadcrumbs.pop
        }
      }

      "when the original breadcrumbs start with something other than the check answers page" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestAddConsigneePage.updateBreadcrumbs(breadcrumbs, TestCheckConsigneePage) mustEqual breadcrumbs
        }
      }
    }

    "going from a check answers page to a regular page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestCheckConsigneePage.updateBreadcrumbs(EmptyBreadcrumbs, TestPage1) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs are not empty" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestCheckConsigneePage.updateBreadcrumbs(breadcrumbs, TestCheckConsigneePage) mustEqual breadcrumbs
        }
      }
    }

    "going from a check answers page to an add-item page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestCheckConsigneePage.updateBreadcrumbs(EmptyBreadcrumbs, TestAddConsigneePage) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs start with the add-item page" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs =
            Breadcrumbs(List(TestAddConsigneePage.breadcrumb(NormalMode), TestCheckNotifiedPartyPage.breadcrumb))

          TestCheckConsigneePage.updateBreadcrumbs(breadcrumbs, TestAddConsigneePage) mustEqual breadcrumbs.pop
        }
      }

      "when the original breadcrumbs start with something other than the add-item page" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestCheckConsigneePage.updateBreadcrumbs(breadcrumbs, TestAddConsigneePage) mustEqual breadcrumbs
        }
      }
    }

    "going from a check answers page to a different check answers page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestCheckConsigneePage.updateBreadcrumbs(EmptyBreadcrumbs, TestCheckNotifiedPartyPage) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs start with the target check answers page" - {

        "must remove the check answers page from the breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestCheckConsigneePage.updateBreadcrumbs(breadcrumbs, TestCheckNotifiedPartyPage) mustEqual breadcrumbs.pop
        }
      }

      "when the original breadcrumbs start with something other than the target check answers page" - {

        "must return the original breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestAddNotifiedPartyPage.breadcrumb(NormalMode)))

          TestCheckConsigneePage.updateBreadcrumbs(breadcrumbs, TestCheckNotifiedPartyPage) mustEqual breadcrumbs
        }
      }
    }

    "going from a check answers page to an add-to-list question page" - {

      "when the original breadcrumbs are empty" - {

        "must return empty breadcrumbs" in new Fixture {

          TestCheckConsigneePage.updateBreadcrumbs(EmptyBreadcrumbs, TestNotifiedPartyPage1) mustEqual EmptyBreadcrumbs
        }
      }

      "when the original breadcrumbs start with the add-item page of the target page's section" - {

        "must return the original the breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestAddNotifiedPartyPage.breadcrumb(NormalMode)))

          TestCheckConsigneePage.updateBreadcrumbs(breadcrumbs, TestNotifiedPartyPage1) mustEqual breadcrumbs
        }
      }

      "when the original breadcrumbs start with something other than the add-item page of the target page's section" - {

        "must add the add-item page of the target page's section to the breadcrumbs" in new Fixture {

          val breadcrumbs: Breadcrumbs = Breadcrumbs(List(TestCheckNotifiedPartyPage.breadcrumb))

          TestCheckConsigneePage.updateBreadcrumbs(breadcrumbs, TestNotifiedPartyPage1)
            .mustEqual(breadcrumbs.push(TestAddNotifiedPartyPage.breadcrumb(NormalMode)))
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
      override val addItemBreadcrumb: Breadcrumb = TestAddConsigneePage.breadcrumb(NormalMode)
    }

    case object TestConsigneePage2 extends Page with AddToListQuestionPage {
      override val section: AddToListSection = ConsigneeSection
      override val addItemBreadcrumb: Breadcrumb = TestAddConsigneePage.breadcrumb(NormalMode)
    }

    case object TestAddNotifiedPartyPage extends AddItemPage {
      override val normalModeUrlFragment: String = "add-thing"
      override val checkModeUrlFragment: String = "change-thing"
    }

    case object TestNotifiedPartyPage1 extends Page with AddToListQuestionPage {
      override val section: AddToListSection = NotifiedPartySection
      override val addItemBreadcrumb: Breadcrumb = TestAddNotifiedPartyPage.breadcrumb(NormalMode)
    }

    case object TestNotifiedPartyPage2 extends Page with AddToListQuestionPage {
      override val section: AddToListSection = NotifiedPartySection
      override val addItemBreadcrumb: Breadcrumb = TestAddNotifiedPartyPage.breadcrumb(NormalMode)
    }

    case object TestCheckConsigneePage extends CheckAnswersPage {
      override val urlFragment: String = "check-consignee"
    }

    case object TestCheckNotifiedPartyPage extends CheckAnswersPage {
      override val urlFragment: String = "check-notified-party"
    }
  }
}