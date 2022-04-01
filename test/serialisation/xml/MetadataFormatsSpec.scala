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

import java.time.Instant

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import base.SpecBase
import models.GbEori
import models.completion.downstream.{MessageSender, MessageType, Metadata}

class MetadataFormatsSpec
  extends SpecBase
  with ScalaCheckPropertyChecks
  with MetadataFormats
  with XmlImplicits {

  private val senders: Gen[MessageSender] = for {
    eori <- arbitrary[GbEori]
    branch <- Gen.listOfN(10, Gen.numChar)
  } yield MessageSender("GB" + eori.value, branch.mkString)

  "The message type format" - {
    "should serialise symmetrically" in {
      val messageTypes: Gen[MessageType] = Gen.oneOf(MessageType.Submission, MessageType.Amendment)

      forAll(messageTypes) { mt =>
        mt.toXmlString.parseXmlString[MessageType] must be(mt)
      }
    }
  }

  "The message sender format" - {
    "should serialise symmetrically" in {
      forAll(senders) { sender =>
        sender.toXmlString.parseXmlString[MessageSender] must be(sender)
      }
    }

    "should separate fields with a forward slash" in {
      forAll(senders) { sender =>
        sender.toXmlString must be(s"${sender.eori}/${sender.branch}")
      }
    }
  }

  "The metadata date + time format" - {
    "should serialise symmetrically" in {
      forAll(minutePrecisionInstants) { dt =>
        dt.toXml.parseXml[Instant] must be(dt)
      }
    }
  }

  "The metadata format" - {
    "should serialise symmetrically" in {
      val metadataGen = for {
        messageId <- stringsWithExactLength(20)
        messageSender <- arbitrary[MessageSender]
        messageType <- arbitrary[MessageType]
        dt <- minutePrecisionInstants
      } yield Metadata(messageId, messageSender, messageType, dt)

      forAll(metadataGen) { m =>
        m.toXml.parseXml[Metadata] must be(m)
      }
    }
  }
}
