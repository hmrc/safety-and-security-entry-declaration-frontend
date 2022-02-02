package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.TotalGrossWeightPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object TotalGrossWeightSummary  {

  def row(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(TotalGrossWeightPage).map {
      answer =>

        SummaryListRowViewModel(
          key     = "totalGrossWeight.checkYourAnswersLabel",
          value   = ValueViewModel(answer.toString),
          actions = Seq(
            ActionItemViewModel("site.change", routes.TotalGrossWeightController.onPageLoad(CheckMode).url)
              .withVisuallyHiddenText(messages("totalGrossWeight.change.hidden"))
          )
        )
    }
}
