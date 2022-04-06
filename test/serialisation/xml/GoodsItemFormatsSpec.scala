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

import base.SpecBase
import models.{Container, Document, DocumentType}
import models.completion.{LoadingPlace, Party}
import models.completion.downstream._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class GoodsItemFormatsSpec extends SpecBase
  with GoodsItemFormats
  with XmlImplicits
  with ScalaCheckPropertyChecks {

  private val documents: Gen[Document] = {
    for {
      docType <- arbitrary[DocumentType]
      ref <- Gen.alphaNumStr
    } yield Document(docType, ref)
  }

  "The container format" - {
    "should work symmetrically" in {
      val container = Container("0103")
      container.toXml.parseXml[Container] must be(container)
    }
  }

  "The DocumentType Format" - {
    "should work symmetrically" in {
      forAll(arbitrary[DocumentType]) { c =>
        c.toXmlString.parseXmlString[DocumentType] must be(c)
      }
    }
  }

  "The Document Format" - {
    "should work symmetrically" in {
      forAll(documents) { c =>
        c.toXml.parseXml[Document] must be(c)
      }
    }
  }

  "The goods item identity format" - {
    "by commodity code" - {
      "should work symmetrically" in {
        forAll(arbitrary[GoodsItemIdentity.ByCommodityCode]) { id =>
          id.toXml.parseXml[GoodsItemIdentity.ByCommodityCode] must be(id)
        }
      }
    }

    "with description" - {
      "should work symmetrically" in {
        forAll(arbitrary[GoodsItemIdentity.WithDescription]) { id =>
          id.toXml.parseXml[GoodsItemIdentity.WithDescription] must be(id)
        }
      }
    }

    "unified type" - {
      "should work symmetrically" in {
        forAll(arbitrary[GoodsItemIdentity]) { id =>
          id.toXml.parseXml[GoodsItemIdentity] must be(id)
        }
      }
    }
  }

  "The dangerous goods code format" - {
    "should work symmetrically" in {
      forAll(arbitrary[DangerousGoodsCode]) { good =>
        good.toXmlString.parseXmlString[DangerousGoodsCode] must be(good)
      }
    }
  }

  "The loading place format" - {
    "should work symmetrically" in {
      forAll(arbitrary[LoadingPlace]) { lp =>
        lp.toXmlString.parseXmlString[LoadingPlace] must be(lp)
      }
    }

    "should serialise to country code plus description, space-delimited" in {
      forAll(arbitrary[LoadingPlace]) { lp =>
        lp.toXmlString must be(s"${lp.country.code} ${lp.desc}")
      }
    }
  }

  "The goods item format" - {
    "should work symmetrically" - {
      "for any good item" in {
        forAll(arbitrary[GoodsItem]) { item =>
          item.toXml.parseXml[GoodsItem] must be(item)
        }
      }

      "when gross mass" - {
        "is specified" in {
          val gen = for {
            item <- arbitrary[GoodsItem]
            mass <- Gen.choose(BigDecimal("0.001"), BigDecimal("99999999.999"))
          } yield item.copy(grossMass = Some(mass))

          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }

        "is missing" in {
          val gen = arbitrary[GoodsItem] map { _.copy(grossMass = None) }
          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }
      }

      "when dangerous goods code" - {
        "is specified" in {
          val gen = for {
            item <- arbitrary[GoodsItem]
            code <- arbitrary[DangerousGoodsCode]
          } yield item.copy(dangerousGoodsCode = Some(code))

          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }

        "is missing" in {
          val gen = arbitrary[GoodsItem] map { _.copy(dangerousGoodsCode = None) }
          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }
      }

      "when documents" - {
        "are present" in {
          val gen = for {
            item <- arbitrary[GoodsItem]
            len <- Gen.choose(1, 99)
            docs <- Gen.listOfN(len, arbitrary[Document])
          } yield item.copy(documents = docs)

          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }

        "are absent" in {
          val gen = arbitrary[GoodsItem] map { _.copy(documents = Nil) }
          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }
      }

      "when consignee" - {
        "is specified" in {
          val gen = for {
            item <- arbitrary[GoodsItem]
            con <- arbitrary[Party]
          } yield item.copy(consignee = Some(con))

          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }

        "is missing" in {
          val gen = arbitrary[GoodsItem] map { _.copy(consignee = None) }
          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }
      }

      "when notified party" - {
        "is specified" in {
          val gen = for {
            item <- arbitrary[GoodsItem]
            party <- arbitrary[Party]
          } yield item.copy(notifiedParty = Some(party))

          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }

        "is missing" in {
          val gen = arbitrary[GoodsItem] map { _.copy(notifiedParty = None) }
          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }
      }

      "when containers" - {
        "are present" in {
          val gen = for {
            item <- arbitrary[GoodsItem]
            len <- Gen.choose(1, 99)
            containers <- Gen.listOfN(len, arbitrary[Container])
          } yield item.copy(containers = containers)

          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }

        "are absent" in {
          val gen = arbitrary[GoodsItem] map { _.copy(containers = Nil) }
          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }
      }

      "when packages" - {
        "are present" in {
          val gen = for {
            item <- arbitrary[GoodsItem]
            len <- Gen.choose(1, 99)
            packages <- Gen.listOfN(len, arbitrary[Package])
          } yield item.copy(packages = packages)

          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }

        "are absent" in {
          val gen = arbitrary[GoodsItem] map { _.copy(packages = Nil) }
          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }
      }

      "when special mentions" - {
        "are present" in {
          val gen = for {
            item <- arbitrary[GoodsItem]
            len <- Gen.choose(1, 10)
            mentions <- Gen.listOfN(len, arbitrary[SpecialMention])
          } yield item.copy(specialMentions = mentions)

          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }

        "are absent" in {
          val gen = arbitrary[GoodsItem] map { _.copy(specialMentions = Nil) }
          forAll(gen) { item => item.toXml.parseXml[GoodsItem] must be(item) }
        }
      }
    }
  }
}
