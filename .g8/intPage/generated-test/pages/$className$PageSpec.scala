package pages

import base.SpecBase
import controllers.routes
import models.{CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class $className$PageSpec extends SpecBase with PageBehaviours {

  "$className$Page" - {

    beRetrievable[Int]($className$Page)

    beSettable[Int]($className$Page)

    beRemovable[Int]($className$Page)

    "must navigate in Normal Mode" - {

      "to Index" in {

        $className$Page.navigate(NormalMode, emptyUserAnswers)
          .mustEqual(routes.IndexController.onPageLoad)
      }
    }

    "must navigate in Check Mode" - {

      "to Check Your Answers" in {

        $className$Page.navigate(CheckMode, emptyUserAnswers)
          .mustEqual(routes.CheckYourAnswersController.onPageLoad(emptyUserAnswers.lrn))
      }
    }
  }
}
