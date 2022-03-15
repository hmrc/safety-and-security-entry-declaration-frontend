package pages.goods

import base.SpecBase
import controllers.goods.{routes => goodsRoutes}
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class ItemContainerNumberPageSpec extends SpecBase with PageBehaviours {

  "ItemContainerNumberPage" - {

    beRetrievable[String](ItemContainerNumberPage)

    beSettable[String](ItemContainerNumberPage)

    beRemovable[String](ItemContainerNumberPage)

    "must navigate in Normal Mode" - {

      "to Index" in {

        ItemContainerNumberPage.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        ItemContainerNumberPage.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
