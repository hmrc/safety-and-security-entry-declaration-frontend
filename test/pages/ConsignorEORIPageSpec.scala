package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class ConsignorEORIPageSpec extends SpecBase with PageBehaviours {

  "ConsignorEORIPage" - {

    beRetrievable[String](ConsignorEORIPage)

    beSettable[String](ConsignorEORIPage)

    beRemovable[String](ConsignorEORIPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ConsignorEORIPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsignorEORIPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
