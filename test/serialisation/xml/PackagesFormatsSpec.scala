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
import models.completion.downstream.Packages
import models.KindOfPackage
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class PackagesFormatsSpec
  extends SpecBase
  with PackagesFormats
  with ScalaCheckPropertyChecks
  with XmlImplicits {

  "The packages format" - {
    "should serialise symmetrically" in {
      val packagesGen = for {
        kindPackage <- arbitrary[KindOfPackage]
        numPackages <- Gen.option(Gen.choose(0, 99999))
        numPieces <- Gen.option(Gen.choose(0, 99999))
        itemMark <- Gen.alphaStr.map { _.take(2) }
      } yield Packages(kindPackage, numPackages, numPieces, itemMark)

      forAll(packagesGen) { p =>
        p.toXml.parseXml[Packages] must be(p)
      }
    }
  }

  "The kind of package Format" - {
    "should serialise symmetrically" in {
      val packages = Gen.oneOf(KindOfPackage.standardKindsOfPackages)
      forAll(packages) { p =>
        p.toXmlString.parseXmlString[KindOfPackage] must be(p)
      }
    }
  }
}
