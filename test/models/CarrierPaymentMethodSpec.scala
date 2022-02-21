package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class CarrierPaymentMethodSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "CarrierPaymentMethod" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(CarrierPaymentMethod.values.toSeq)

      forAll(gen) {
        carrierPaymentMethod =>

          JsString(carrierPaymentMethod.toString).validate[CarrierPaymentMethod].asOpt.value mustEqual carrierPaymentMethod
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!CarrierPaymentMethod.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[CarrierPaymentMethod] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(CarrierPaymentMethod.values.toSeq)

      forAll(gen) {
        carrierPaymentMethod =>

          Json.toJson(carrierPaymentMethod) mustEqual JsString(carrierPaymentMethod.toString)
      }
    }
  }
}
