package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class AddPaymentMethodPageSpec extends SpecBase with PageBehaviours {

  "AddPaymentMethodPage" - {

    beRetrievable[Boolean](AddPaymentMethodPage)

    beSettable[Boolean](AddPaymentMethodPage)

    beRemovable[Boolean](AddPaymentMethodPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        AddPaymentMethodPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        AddPaymentMethodPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
