package pages

import pages.behaviours.PageBehaviours

class TotalGrossWeightPageSpec extends PageBehaviours {

  "TotalGrossWeightPage" - {

    beRetrievable[Int](TotalGrossWeightPage)

    beSettable[Int](TotalGrossWeightPage)

    beRemovable[Int](TotalGrossWeightPage)
  }
}
