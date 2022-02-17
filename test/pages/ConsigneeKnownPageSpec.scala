package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class ConsigneeKnownPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeKnownPage" - {

    beRetrievable[Boolean](ConsigneeKnownPage)

    beSettable[Boolean](ConsigneeKnownPage)

    beRemovable[Boolean](ConsigneeKnownPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ConsigneeKnownPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneeKnownPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
