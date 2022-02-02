package pages

import models.GrossWeight
import pages.behaviours.PageBehaviours

class GrossWeightSpec extends PageBehaviours {

  "GrossWeightPage" - {

    beRetrievable[GrossWeight](GrossWeightPage)

    beSettable[GrossWeight](GrossWeightPage)

    beRemovable[GrossWeight](GrossWeightPage)
  }
}
