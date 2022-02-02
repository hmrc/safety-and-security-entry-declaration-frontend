package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class GrossWeightSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "GrossWeight" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(GrossWeight.values.toSeq)

      forAll(gen) {
        grossWeight =>

          JsString(grossWeight.toString).validate[GrossWeight].asOpt.value mustEqual grossWeight
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!GrossWeight.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[GrossWeight] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(GrossWeight.values.toSeq)

      forAll(gen) {
        grossWeight =>

          Json.toJson(grossWeight) mustEqual JsString(grossWeight.toString)
      }
    }
  }
}
