package pages

import base.SpecBase
import controllers.routes
import models.{ConsigneeIdentity, CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class ConsigneeIdentitySpec extends SpecBase with PageBehaviours {

  "ConsigneeIdentityPage" - {

    beRetrievable[ConsigneeIdentity](ConsigneeIdentityPage)

    beSettable[ConsigneeIdentity](ConsigneeIdentityPage)

    beRemovable[ConsigneeIdentity](ConsigneeIdentityPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ConsigneeIdentityPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneeIdentityPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
