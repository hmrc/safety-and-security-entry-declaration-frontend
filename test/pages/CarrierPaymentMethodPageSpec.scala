package pages

import base.SpecBase
import controllers.routes
import models.{CarrierPaymentMethod, CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class CarrierPaymentMethodSpec extends SpecBase with PageBehaviours {

  "CarrierPaymentMethodPage" - {

    beRetrievable[CarrierPaymentMethod](CarrierPaymentMethodPage)

    beSettable[CarrierPaymentMethod](CarrierPaymentMethodPage)

    beRemovable[CarrierPaymentMethod](CarrierPaymentMethodPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        CarrierPaymentMethodPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        CarrierPaymentMethodPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
