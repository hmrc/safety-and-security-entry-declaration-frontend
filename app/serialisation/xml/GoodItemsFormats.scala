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

import models.{Container, Document, DocumentType}
import serialisation.xml.XmlImplicits._

import scala.xml.NodeSeq

trait GoodItemsFormats extends CommonFormats {

  implicit val containerFmt = new Format[Container] {
    override def encode(container: Container): NodeSeq = {
      <ConNumNR21>{container.itemContainerNumber}</ConNumNR21>
    }
    override def decode(data: NodeSeq): Container = Container(data.text)
  }

  implicit val documentTypeFormat = new StringFormat[DocumentType] {
    override def encode(docType: DocumentType): String = docType.code
    override def decode(s: String): DocumentType = {
      DocumentType.allDocumentTypes.find { _.code == s}.getOrElse{
        throw new XmlDecodingException(s"Bad Document code: $s")
      }
    }
  }

  implicit val documentFormat = new Format[Document] {
    override def encode(doc: Document): NodeSeq = {
      <DocTypDC21>{doc.documentType.toXmlString}</DocTypDC21>
      <DocRefDC23>{doc.reference}</DocRefDC23>
    }
    override def decode(data: NodeSeq): Document = {
      Document(
        documentType = (data \\ "DocTypDC21").text.parseXmlString[DocumentType],
        reference = (data \\ "DocRefDC23").text
      )
    }
  }
}
