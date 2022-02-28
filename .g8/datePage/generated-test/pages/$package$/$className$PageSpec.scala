package pages.$package$

import base.SpecBase
import controllers.$package$.{routes => $package$Routes}
import controllers.routes
import models.{CheckMode, NormalMode}

import java.time.LocalDate
import org.scalacheck.Arbitrary
import pages.behaviours.PageBehaviours

class $className$PageSpec extends SpecBase with PageBehaviours {

  "$className$Page" - {

    implicit lazy val arbitraryLocalDate: Arbitrary[LocalDate] = Arbitrary {
      datesBetween(LocalDate.of(1900, 1, 1), LocalDate.of(2100, 1, 1))
    }

    beRetrievable[LocalDate]($className$Page)

    beSettable[LocalDate]($className$Page)

    beRemovable[LocalDate]($className$Page)

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
