package pages.$package$

import base.SpecBase
import controllers.$package$.{routes => $package$Routes}
import controllers.routes
import models.{$className$, CheckMode, NormalMode}
import pages.behaviours.PageBehaviours

class $className$Spec extends SpecBase with PageBehaviours {

  "$className$Page" - {

    beRetrievable[$className$]($className$Page)

    beSettable[$className$]($className$Page)

    beRemovable[$className$]($className$Page)

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
