package pages.goods

import base.SpecBase
import controllers.goods.{routes => goodsRoutes}
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class ShippingContainersPageSpec extends SpecBase with PageBehaviours {

  "ShippingContainersPage" - {

    beRetrievable[Boolean](ShippingContainersPage)

    beSettable[Boolean](ShippingContainersPage)

    beRemovable[Boolean](ShippingContainersPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ShippingContainersPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ShippingContainersPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
