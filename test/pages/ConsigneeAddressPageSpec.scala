package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours


class ConsigneeAddressPageSpec extends SpecBase with PageBehaviours {

  "ConsigneeAddressPage" - {

    beRetrievable[String](ConsigneeAddressPage)

    beSettable[String](ConsigneeAddressPage)

    beRemovable[String](ConsigneeAddressPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ConsigneeAddressPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ConsigneeAddressPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
