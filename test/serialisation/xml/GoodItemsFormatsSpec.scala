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
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class GoodItemsFormatsSpec extends SpecBase
  with GoodItemsFormats
  with XmlImplicits
  with ScalaCheckPropertyChecks {

  override implicit lazy val arbitraryDocumentType: Arbitrary[DocumentType] = {
    Arbitrary {
      Gen.oneOf(DocumentType.allDocumentTypes)
    }
  }

  private val document: Gen[Document] = {
    for {
      docType <- arbitrary[DocumentType]
      ref <- Gen.alphaNumStr
    } yield Document(docType, ref)
  }

  "The container format" - {
    "should parse one container" in {
      val container = Container("0103")
      container.toXml.parseXml[Container] must be(container)
    }
  }

  "The DocumentType Format" - {
    "should parse one documentType" in {
      forAll(arbitrary[DocumentType]) { c =>
        c.toXmlString.parseXmlString[DocumentType] must be(c)
      }
    }
  }

  "The Document Format" - {
    "should parse one doucment" in {
      forAll(arbitrary[Document]) { c =>
        c.toXml.parseXml[Document] must be(c)
      }
    }
  }
}
