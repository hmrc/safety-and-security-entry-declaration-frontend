package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class ConsignorAddressPageSpec extends SpecBase with PageBehaviours {

  "ConsignorAddressPage" - {

    beRetrievable[String](ConsignorAddressPage)

    beSettable[String](ConsignorAddressPage)

    beRemovable[String](ConsignorAddressPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ConsignorAddressPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsignorAddressPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
