package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class NotifiedPartyNamePageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyNamePage" - {

    beRetrievable[String](NotifiedPartyNamePage)

    beSettable[String](NotifiedPartyNamePage)

    beRemovable[String](NotifiedPartyNamePage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        NotifiedPartyNamePage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        NotifiedPartyNamePage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
