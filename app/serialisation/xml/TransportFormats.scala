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

import models.{Country, PaymentMethod, TransportMode}
import models.PaymentMethod._
import models.TransportMode._
import models.completion.downstream.TransportDetails
import serialisation.xml.XmlImplicits._

/**
 * XML formats related to transport properties
 */
trait TransportFormats extends CommonFormats {
  implicit val transportModeFmt = new StringFormat[TransportMode] {
    override def encode(m: TransportMode): String = m match {
      case Maritime => "1"
      case Rail => "2"
      case Road => "3"
      case Air => "4"
      case RoroAccompanied => "10"
      case RoroUnaccompanied => "11"
    }

    override def decode(s: String): TransportMode = s match {
      case "1" => Maritime
      case "2" => Rail
      case "3" => Road
      case "4" => Air
      case "10" => RoroAccompanied
      case "11" => RoroUnaccompanied
      case v => throw new XmlDecodingException(s"Unexpected value $v for transport mode")
    }
  }

  implicit val paymentMethodFmt = new StringFormat[PaymentMethod] {
    override def encode(pm: PaymentMethod): String = pm match {
      case Cash => "A"
      case CreditCard => "B"
      case Cheque => "C"
      case Other => "D"
      case CreditTransfer => "H"
      case AccountHolderWithCarrier => "Y"
      case NotPrePaid => "Z"
    }

    override def decode(s: String): PaymentMethod = s match {
      case "A" => Cash
      case "B" => CreditCard
      case "C" => Cheque
      case "D" => Other
      case "H" => CreditTransfer
      case "Y" => AccountHolderWithCarrier
      case "Z" => NotPrePaid
      case v => throw new XmlDecodingException(s"Unexpected value $v for payment method")
    }
  }

  implicit val transportDetailsFmt = new Format[TransportDetails] {
    override def encode(details: TransportDetails): NodeSeq = {
      val id: NodeSeq = details.identity.map { trId =>
        <IdeOfMeaOfTraCroHEA85>{trId}</IdeOfMeaOfTraCroHEA85>,
      }.toSeq

      val nationality: NodeSeq = details.nationality.map { n =>
        <NatOfMeaOfTraCroHEA87>{n.toXmlString}</NatOfMeaOfTraCroHEA87>
      }.toSeq

      val conveyanceRef: NodeSeq = details.conveyanceReferenceNumber.map { num =>
        <ConRefNumHEA>{num}</ConRefNumHEA>
      }.toSeq

      Seq(
        <TraModAtBorHEA76>{details.mode.toXmlString}</TraModAtBorHEA76>,
        id,
        nationality,
        conveyanceRef
      ).flatten
    }

    override def decode(data: NodeSeq): TransportDetails = {
      val mode = (data \\ "TraModAtBorHEA76").text.parseXmlString[TransportMode]
      val identifier = (data \\ "IdeOfMeaOfTraCroHEA85").headOption.map { _.text }
      val nationality = (data \\ "NatOfMeaOfTraCroHEA87").headOption map {
        _.text.parseXmlString[Country]
      }
      val conveyanceRef = (data \\ "ConRefNumHEA").headOption map { _.text }

      TransportDetails(mode, identifier, nationality, conveyanceRef)
    }
  }

}

object TransportFormats extends TransportFormats
