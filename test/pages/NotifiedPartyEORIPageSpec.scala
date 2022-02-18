package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class NotifiedPartyEORIPageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyEORIPage" - {

    beRetrievable[String](NotifiedPartyEORIPage)

    beSettable[String](NotifiedPartyEORIPage)

    beRemovable[String](NotifiedPartyEORIPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        NotifiedPartyEORIPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        NotifiedPartyEORIPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
