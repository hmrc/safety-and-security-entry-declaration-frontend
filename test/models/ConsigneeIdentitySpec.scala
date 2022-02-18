package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class ConsigneeIdentitySpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "ConsigneeIdentity" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(ConsigneeIdentity.values.toSeq)

      forAll(gen) {
        consigneeIdentity =>

          JsString(consigneeIdentity.toString).validate[ConsigneeIdentity].asOpt.value mustEqual consigneeIdentity
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!ConsigneeIdentity.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[ConsigneeIdentity] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(ConsigneeIdentity.values.toSeq)

      forAll(gen) {
        consigneeIdentity =>

          Json.toJson(consigneeIdentity) mustEqual JsString(consigneeIdentity.toString)
      }
    }
  }
}
