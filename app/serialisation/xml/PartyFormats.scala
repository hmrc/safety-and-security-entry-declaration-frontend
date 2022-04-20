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

import scala.xml.{Elem, NodeSeq, Null, Text, TopScope}
import models.{Address, Country, GbEori}
import models.completion.Party
import serialisation.xml.XmlImplicits._

/**
 * Formats related to identifying parties of interest in the declaration
 */
trait PartyFormats extends CommonFormats {
  import PartyFormats.Fields

  private def partyFmt(fields: PartyFormats.Fields): Format[Party] = new Format[Party] {
    /**
     * Convenience method to more easily create an XML element with a non-literal label
     */
    private def elem(label: String, content: String): Elem = Elem(
      prefix = null,
      label = label,
      attributes = Null,
      scope = TopScope,
      minimizeEmpty = false,
      Text(content)
    )

    override def encode(p: Party): NodeSeq = p match {
      case Party.ByEori(eori) =>
        elem(fields.eori, eori.toXmlString)
      case Party.ByAddress(name, addr) =>
        Seq(
          elem(fields.name, name),
          elem(fields.streetAndNumber, addr.streetAndNumber),
          elem(fields.postCode, addr.postCode),
          elem(fields.city, addr.city),
          elem(fields.country, addr.country.toXmlString)
        )
    }

    override def decode(data: NodeSeq): Party = {
      (data \\ fields.eori).headOption map {
        eori => Party.ByEori(eori.text.parseXmlString[GbEori])
      } getOrElse {
        Party.ByAddress(
          (data \\ fields.name).text,
          Address(
            (data \\ fields.streetAndNumber).text,
            (data \\ fields.city).text,
            (data \\ fields.postCode).text,
            (data \\ fields.country).text.parseXmlString[Country]
          )
        )
      }
    }
  }

  val goodsConsignorFormat = {
    partyFmt(Fields("TINCO259", "NamCO27", "StrAndNumCO222", "CitCO224", "PosCodCO223", "CouCO225"))
  }

  val goodsConsigneeFormat = {
    partyFmt(Fields("TINCE259", "NamCE27", "StrAndNumCE222", "CitCE224", "PosCodCE223", "CouCE225"))
  }

  val goodsNotifiedPartyFormat = {
    partyFmt(
      Fields(
        "TINPRTNOT641",
        "NamPRTNOT642",
        "StrNumPRTNOT646",
        "CtyPRTNOT643",
        "PstCodPRTNOT644",
        "CouCodGINOT647"
      )
    )
  }

  val lodgingPersonFormat = {
    partyFmt(Fields("TINPLD1", "NamPLD1", "StrAndNumPLD1", "CitPLD1", "PosCodPLD1", "CouCodPLD1"))
  }

  val carrierFormat = {
    partyFmt(
      Fields(
        "TINTRACARENT602",
        "NamTRACARENT604",
        "StrNumTRACARENT607",
        "CtyTRACARENT603",
        "PstCodTRACARENT606",
        "CouCodTRACARENT605"
      )
    )
  }
}

object PartyFormats extends PartyFormats {
  /**
   * Identifies the field names required to (de)serialise this party
   *
   * These fields vary depending on which party is being identified
   */
  private[xml] case class Fields(
    eori: String,
    name: String,
    streetAndNumber: String,
    city: String,
    postCode: String,
    country: String
  )
}
