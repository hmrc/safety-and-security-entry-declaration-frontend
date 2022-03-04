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

trait XmlImplicits {
  implicit class XmlEncodable[T : Encoding](t: T) {
    def toXml: NodeSeq = implicitly[Encoding[T]].encode(t)
  }

  implicit class XmlDecodable(data: NodeSeq) {
    def parseXml[T : Decoding] = implicitly[Decoding[T]].decode(data)
  }

  implicit class XmlStringEncodable[T : StringEncoding](t: T) {
    def toXmlString: String = implicitly[StringEncoding[T]].encode(t)
  }

  implicit class XmlStringDecodable(s: String) {
    def parseXmlString[T : StringDecoding] = implicitly[StringDecoding[T]].decode(s)
  }
}

object XmlImplicits extends XmlImplicits
