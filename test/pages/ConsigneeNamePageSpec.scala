package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class ConsigneeNamePageSpec extends SpecBase with PageBehaviours {

  "ConsigneeNamePage" - {

    beRetrievable[String](ConsigneeNamePage)

    beSettable[String](ConsigneeNamePage)

    beRemovable[String](ConsigneeNamePage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ConsigneeNamePage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneeNamePage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
