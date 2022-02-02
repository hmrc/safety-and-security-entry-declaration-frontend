package pages

import models.TransportMode
import pages.behaviours.PageBehaviours

class TransportModeSpec extends PageBehaviours {

  "TransportModePage" - {

    beRetrievable[TransportMode](TransportModePage)

    beSettable[TransportMode](TransportModePage)

    beRemovable[TransportMode](TransportModePage)
  }
}
