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

import java.io.StringReader
import javax.xml.XMLConstants
import javax.xml.transform.{Source => XMLSource}
import javax.xml.transform.sax.SAXSource
import javax.xml.validation.{Validator, SchemaFactory}
import org.xml.sax.InputSource
import scala.io.Source

/**
 * Validate XML against a list of XML schemata provided as resource filenames relative to /schema
 */
class XmlValidator(schemata: Seq[String]) {
  private def src(data: String): XMLSource = new SAXSource(new InputSource(new StringReader(data)))

  private lazy val validator: Validator = {
    val sources: Array[XMLSource] = schemata.map { s =>
      src(Source.fromResource(s"schema/$s").mkString)
    }.toArray

    SchemaFactory
      .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      .newSchema(sources)
      .newValidator()
  }

  def validate(doc: String): Unit = validator.validate(src(doc))
}

object XmlValidator {
  /**
   * Validate documents against CC315A schemata
   */
  object SubmissionValidator extends XmlValidator(
    Seq(
      "doc-v11-2.xsd",
      "tcl-v11-2.xsd",
      "simple_types-v11-2.xsd",
      "complex_types_ics-v11-2.xsd",
      "CC315A-v11-2.xsd"
    )
  )

  object AmendmentValidator extends XmlValidator(
    Seq(
      "doc-v11-2.xsd",
      "tcl-v11-2.xsd",
      "simple_types-v11-2.xsd",
      "complex_types_ics-v11-2.xsd",
      "CC313A-v11-2.xsd"
    )
  )
}
