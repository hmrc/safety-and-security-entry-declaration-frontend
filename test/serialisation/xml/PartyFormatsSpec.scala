/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package serialisation.xml

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import base.SpecBase
import models.completion.Party
import models.{Address, Country}

class PartyFormatsSpec
  extends SpecBase
  with ScalaCheckPropertyChecks
  with PartyFormats
  with XmlImplicits {

  private val partyByEori: Gen[Party] = for {
    country <- Gen.oneOf(Country.allCountries)
    number <- Gen.listOfN(12, Gen.numChar)
  } yield Party.ByEori(s"${country.code}${number.mkString}")

  private val partyByAddress: Gen[Party] = for {
    name <- stringsWithMaxLength(40)
    addr <- arbitrary[Address]
  } yield Party.ByAddress(name, addr)

  "The goods consignor format" - {
    implicit val fmt: Format[Party] = goodsConsignorFormat

    "identified by EORI" - {
      "should serialise symmetrically" in {
        forAll(partyByEori) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByEori) { p =>
          val actual = p.toXml
          val expected = <TINCO259>{p.asInstanceOf[Party.ByEori].eori}</TINCO259>
          actual must contain theSameElementsInOrderAs(expected)
        }
      }
    }

    "identified by name and address" - {
      "should serialise symmetrically" in {
        forAll(partyByAddress) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByAddress) { p =>
          val actual = p.toXml
          val expected = {
            val byAddr = p.asInstanceOf[Party.ByAddress]

            <NamCO27>{byAddr.name}</NamCO27>
            <StrAndNumCO222>{byAddr.address.streetAndNumber}</StrAndNumCO222>
            <PosCodCO223>{byAddr.address.postCode}</PosCodCO223>
            <CitCO224>{byAddr.address.city}</CitCO224>
            <CouCO225>{byAddr.address.country.toXmlString}</CouCO225>
          }

          actual must contain theSameElementsInOrderAs(expected)
        }
      }
    }
  }

  "The goods consignee format" - {
    implicit val fmt = goodsConsigneeFormat

    "identified by EORI" - {
      "should serialise symmetrically" in {
        forAll(partyByEori) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByEori) { p =>
          val actual = p.toXml
          val expected = <TINCE259>{p.asInstanceOf[Party.ByEori].eori}</TINCE259>
          actual must contain theSameElementsAs(expected)
        }
      }
    }

    "identified by name and address" - {
      "should serialise symmetrically" in {
        forAll(partyByAddress) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByAddress) { p =>
          val actual = p.toXml
          val expected = {
            val byAddr = p.asInstanceOf[Party.ByAddress]

            <NamCE27>{byAddr.name}</NamCE27>
            <StrAndNumCE222>{byAddr.address.streetAndNumber}</StrAndNumCE222>
            <PosCodCE223>{byAddr.address.postCode}</PosCodCE223>
            <CitCE224>{byAddr.address.city}</CitCE224>
            <CouCE225>{byAddr.address.country.toXmlString}</CouCE225>
          }

          actual must contain theSameElementsInOrderAs(expected)
        }
      }
    }
  }

  "The goods notified party format" - {
    implicit val fmt = goodsNotifiedPartyFormat

    "identified by EORI" - {
      "should serialise symmetrically" in {
        forAll(partyByEori) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByEori) { p =>
          val actual = p.toXml
          val expected = <TINPRTNOT641>{p.asInstanceOf[Party.ByEori].eori}</TINPRTNOT641>
          actual must contain theSameElementsInOrderAs(expected)
        }
      }
    }

    "identified by name and address" - {
      "should serialise symmetrically" in {
        forAll(partyByAddress) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByAddress) { p =>
          val actual = p.toXml
          val expected = {
            val byAddr = p.asInstanceOf[Party.ByAddress]

            <NamPRTNOT642>{byAddr.name}</NamPRTNOT642>
            <StrNumPRTNOT646>{byAddr.address.streetAndNumber}</StrNumPRTNOT646>
            <PstCodPRTNOT644>{byAddr.address.postCode}</PstCodPRTNOT644>
            <CtyPRTNOT643>{byAddr.address.city}</CtyPRTNOT643>
            <CouCodGINOT647>{byAddr.address.country.toXmlString}</CouCodGINOT647>
          }

          actual must contain theSameElementsInOrderAs(expected)
        }
      }
    }
  }

  "The lodging person format" - {
    implicit val fmt = lodgingPersonFormat

    "identified by EORI" - {
      "should serialise symmetrically" in {
        forAll(partyByEori) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByEori) { p =>
          val actual = p.toXml
          val expected = <TINPLD1>{p.asInstanceOf[Party.ByEori].eori}</TINPLD1>
          actual must contain theSameElementsInOrderAs(expected)
        }
      }
    }

    "identified by name and address" - {
      "should serialise symmetrically" in {
        forAll(partyByAddress) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByAddress) { p =>
          val actual = p.toXml
          val expected = {
            val byAddr = p.asInstanceOf[Party.ByAddress]

            <NamPLD1>{byAddr.name}</NamPLD1>
            <StrAndNumPLD1>{byAddr.address.streetAndNumber}</StrAndNumPLD1>
            <PosCodPLD1>{byAddr.address.postCode}</PosCodPLD1>
            <CitPLD1>{byAddr.address.city}</CitPLD1>
            <CouCodPLD1>{byAddr.address.country.toXmlString}</CouCodPLD1>
          }

          actual must contain theSameElementsInOrderAs(expected)
        }
      }
    }
  }

  "The carrier format" - {
    implicit val fmt = carrierFormat

    "identified by EORI" - {
      "should serialise symmetrically" in {
        forAll(partyByEori) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByEori) { p =>
          val actual = p.toXml
          val expected = <TINTRACARENT602>{p.asInstanceOf[Party.ByEori].eori}</TINTRACARENT602>
          actual must contain theSameElementsAs(expected)
        }
      }
    }

    "identified by name and address" - {
      "should serialise symmetrically" in {
        forAll(partyByAddress) { p => p.toXml.parseXml[Party] must be(p) }
      }

      "should serialise correctly to XML" in {
        forAll(partyByAddress) { p =>
          val actual = p.toXml
          val expected = {
            val byAddr = p.asInstanceOf[Party.ByAddress]

            <NamTRACARENT604>{byAddr.name}</NamTRACARENT604>
            <StrNumTRACARENT607>{byAddr.address.streetAndNumber}</StrNumTRACARENT607>
            <PstCodTRACARENT606>{byAddr.address.postCode}</PstCodTRACARENT606>
            <CtyTRACARENT603>{byAddr.address.city}</CtyTRACARENT603>
            <CouCodTRACARENT605>{byAddr.address.country.toXmlString}</CouCodTRACARENT605>
          }

          actual must contain theSameElementsInOrderAs(expected)
        }
      }
    }
  }
}
