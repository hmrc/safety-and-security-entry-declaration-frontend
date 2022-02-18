package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class ConsigneeEORIPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeEORIPage" - {

    beRetrievable[String](ConsigneeEORIPage)

    beSettable[String](ConsigneeEORIPage)

    beRemovable[String](ConsigneeEORIPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ConsigneeEORIPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneeEORIPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
