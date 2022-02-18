package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class NotifiedPartyIdentitySpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "NotifiedPartyIdentity" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(NotifiedPartyIdentity.values.toSeq)

      forAll(gen) {
        notifiedPartyIdentity =>

          JsString(notifiedPartyIdentity.toString).validate[NotifiedPartyIdentity].asOpt.value mustEqual notifiedPartyIdentity
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!NotifiedPartyIdentity.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[NotifiedPartyIdentity] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(NotifiedPartyIdentity.values.toSeq)

      forAll(gen) {
        notifiedPartyIdentity =>

          Json.toJson(notifiedPartyIdentity) mustEqual JsString(notifiedPartyIdentity.toString)
      }
    }
  }
}
