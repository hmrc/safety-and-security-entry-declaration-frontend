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

import models.completion.downstream.Package
import models.KindOfPackage
import serialisation.xml.XmlImplicits._

trait PackagesFormats extends CommonFormats {

  implicit val packageKindFormat = new StringFormat[KindOfPackage] {
    override def encode(packageKind: KindOfPackage): String = packageKind.code
    override def decode(s: String): KindOfPackage = {
      KindOfPackage.allKindsOfPackage.find { _.code == s }.getOrElse {
        throw new XmlDecodingException(s"Bad Package code: $s")
      }
    }
  }

  implicit val packageFmt = new Format[Package] {
    override def encode(p: Package): NodeSeq = {
      val numPackages: NodeSeq = {
        p.numPackages.map { n => <NumOfPacGS24>{n}</NumOfPacGS24> }.toSeq
      }
      val numPieces: NodeSeq = {
        p.numPieces.map { n => <NumOfPieGS25>{n}</NumOfPieGS25> }.toSeq
      }
      val mark: NodeSeq = {
        p.mark.map { m => <MarNumOfPacGSL21>{m}</MarNumOfPacGSL21> }.toSeq
      }

      Seq(
        <KinOfPacGS23>{p.kindPackage.toXmlString}</KinOfPacGS23>,
        numPackages,
        numPieces,
        mark
      ).flatten
    }

    override def decode(data: NodeSeq): Package = Package(
      kindPackage = (data \\ "KinOfPacGS23").text.parseXmlString[KindOfPackage],
      numPackages = (data \\ "NumOfPacGS24").headOption map { _.text.toInt },
      numPieces = (data \\ "NumOfPieGS25").headOption map { _.text.toInt },
      mark = (data \\ "MarNumOfPacGSL21").headOption map { _.text }
    )
  }
}

object PackagesFormats extends PackagesFormats
