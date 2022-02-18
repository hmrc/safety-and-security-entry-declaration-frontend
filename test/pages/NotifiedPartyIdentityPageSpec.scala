package pages

import base.SpecBase
import controllers.routes
import models.{NotifiedPartyIdentity, CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class NotifiedPartyIdentitySpec extends SpecBase with PageBehaviours {

  "NotifiedPartyIdentityPage" - {

    beRetrievable[NotifiedPartyIdentity](NotifiedPartyIdentityPage)

    beSettable[NotifiedPartyIdentity](NotifiedPartyIdentityPage)

    beRemovable[NotifiedPartyIdentity](NotifiedPartyIdentityPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        NotifiedPartyIdentityPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        NotifiedPartyIdentityPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
