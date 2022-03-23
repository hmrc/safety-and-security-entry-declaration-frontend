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

import scala.xml.NodeSeq

import models.completion.downstream.Packages
import models.KindOfPackage
import serialisation.xml.XmlImplicits._

trait PackagesFormats extends CommonFormats {

  implicit val packageKindFormat = new StringFormat[KindOfPackage] {
    override def encode(packageKind: KindOfPackage): String = packageKind.code
    override def decode(s: String): KindOfPackage = {
      KindOfPackage.standardKindsOfPackages.find { _.code == s }.getOrElse {
        throw new XmlDecodingException(s"Bad Package code: $s")
      }
    }
  }

  implicit val packagesFmt = new Format[Packages] {
    override def encode(packages: Packages): NodeSeq = {
      val numPackages: NodeSeq = {
        packages.numPackages.map { n => <NumOfPacGS24>{n}</NumOfPacGS24> }.toSeq
      }
      val numPieces: NodeSeq = {
        packages.numPieces.map { n => <NumOfPieGS25>{n}</NumOfPieGS25> }.toSeq
      }
      Seq(
        <KinOfPacGS23>{packages.kindPackage.toXmlString}</KinOfPacGS23>,
        numPackages,
        numPieces,
        <MarNumOfPacGSL21>{packages.itemMark}</MarNumOfPacGSL21>
      ).flatten
    }

    override def decode(data: NodeSeq): Packages = Packages(
      kindPackage = (data \\ "KinOfPacGS23").text.parseXmlString[KindOfPackage],
      numPackages = (data \\ "NumOfPacGS24").headOption map { _.text.toInt },
      numPieces = (data \\ "NumOfPieGS25").headOption map { _.text.toInt },
      itemMark = (data \\ "MarNumOfPacGSL21").text
    )
  }
}

object PackagesFormats extends PackagesFormats
