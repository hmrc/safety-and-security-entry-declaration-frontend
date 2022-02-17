package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class ConsignorNamePageSpec extends SpecBase with PageBehaviours {

  "ConsignorNamePage" - {

    beRetrievable[String](ConsignorNamePage)

    beSettable[String](ConsignorNamePage)

    beRemovable[String](ConsignorNamePage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ConsignorNamePage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsignorNamePage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
