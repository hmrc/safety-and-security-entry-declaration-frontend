package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class NotifiedPartyAddressPageSpec extends SpecBase with PageBehaviours {

  "NotifiedPartyAddressPage" - {

    beRetrievable[String](NotifiedPartyAddressPage)

    beSettable[String](NotifiedPartyAddressPage)

    beRemovable[String](NotifiedPartyAddressPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        NotifiedPartyAddressPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        NotifiedPartyAddressPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
