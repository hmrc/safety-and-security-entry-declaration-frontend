package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class UnloadingCodePageSpec extends SpecBase with PageBehaviours {

  "UnloadingCodePage" - {

    beRetrievable[String](UnloadingCodePage)

    beSettable[String](UnloadingCodePage)

    beRemovable[String](UnloadingCodePage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        UnloadingCodePage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        UnloadingCodePage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
