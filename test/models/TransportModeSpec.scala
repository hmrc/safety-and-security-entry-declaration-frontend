package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class TransportModeSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "TransportMode" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(TransportMode.values.toSeq)

      forAll(gen) {
        transportMode =>

          JsString(transportMode.toString).validate[TransportMode].asOpt.value mustEqual transportMode
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!TransportMode.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[TransportMode] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(TransportMode.values.toSeq)

      forAll(gen) {
        transportMode =>

          Json.toJson(transportMode) mustEqual JsString(transportMode.toString)
      }
    }
  }
}
