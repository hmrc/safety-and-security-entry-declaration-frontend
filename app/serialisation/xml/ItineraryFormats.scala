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

import models.Country
import models.completion.Itinerary
import serialisation.xml.XmlImplicits._

trait ItineraryFormats extends CommonFormats {
  implicit val itineraryFmt = new Format[Itinerary] {
    override def encode(itin: Itinerary): NodeSeq = {
      itin.countries map {
        country => <ITI><CouOfRouCodITI1>{country.toXmlString}</CouOfRouCodITI1></ITI>
      }
    }

    override def decode(data: NodeSeq): Itinerary = {
      val countries: Seq[Country] = data map { node =>
        val countryCode = (node \ "CouOfRouCodITI1").text
        countryCode.parseXmlString[Country]
      }

      Itinerary(countries.toList)
    }
  }
}

object ItineraryFormats extends ItineraryFormats
