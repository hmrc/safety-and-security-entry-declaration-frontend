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

package extractors

import cats.implicits._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import base.SpecBase
import extractors.ValidationError.MissingField
import models.{Country, Document, Index}
import models.TransportIdentity._
import models.TransportMode._
import models.completion.answers._
import models.completion.answers.Transport
import pages.transport._
import queries._
import queries.transport.{AllOverallDocumentsQuery, AllSealsQuery}

class TransportExtractorSpec extends SpecBase {
  private val nationality = arbitrary[Country].sample.value

  private val airDetails = arbitrary[AirIdentity].sample.value
  private val maritimeDetails = arbitrary[MaritimeIdentity].sample.value
  private val railDetails = arbitrary[RailIdentity].sample.value
  private val roadDetails = arbitrary[RoadIdentity].sample.value
  private val roroAccompaniedDetails = arbitrary[RoroAccompaniedIdentity].sample.value
  private val roroUnaccompaniedDetails = arbitrary[RoroUnaccompaniedIdentity].sample.value

  private val documents = {
    Gen.choose(1, 10)
      .flatMap { len => Gen.listOfN(len, arbitrary[Document]) }
      .sample.value
  }

  private val seals = {
    Gen.choose(1, 10)
      .flatMap { len => Gen.listOfN(len, arbitrary[String]) }
      .sample.value
  }

  private val expectedResult = Transport(
    identity = roadDetails,
    mode = Road,
    nationality = Some(nationality),
    documents = documents,
    seals = seals
  )

  private val validAnswers = {
    emptyUserAnswers
      .set(TransportModePage, Road).success.value
      .set(NationalityOfTransportPage, nationality).success.value
      .set(RoadIdentityPage, roadDetails).success.value
      .set(AnyOverallDocumentsPage, true).success.value
      .set(AllOverallDocumentsQuery, documents).success.value
      .set(AddAnySealsPage, true).success.value
      .set(AllSealsQuery, seals).success.value
  }

    "the transport extractor" - {
      "should fail if no answer to transport mode page" in {
        val answers = validAnswers.remove(TransportModePage).success.value

        val expected = List(MissingField(TransportModePage))
        val actual = new TransportExtractor()(answers).extract().invalidValue.toList

        actual must contain theSameElementsAs(expected)
      }

      "for mode Air" - {
        "should extract correctly" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Air).success.value
              .remove(NationalityOfTransportPage).success.value
              .set(AirIdentityPage, airDetails).success.value
          }

          val expected = expectedResult.copy(identity = airDetails, mode = Air, nationality = None)
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should never extract nationality of transport" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Air).success.value
              .set(NationalityOfTransportPage, nationality).success.value
              .set(AirIdentityPage, airDetails).success.value
          }

          val expected = expectedResult.copy(identity = airDetails, mode = Air, nationality = None)
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should fail if transport identity was not answered" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Air).success.value
              .remove(AirIdentityPage).success.value
          }

          val expected = List(MissingField(AirIdentityPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs (expected)
        }
      }

      "for mode Maritime" - {
        "should extract correctly" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Maritime).success.value
              .remove(NationalityOfTransportPage).success.value
              .set(MaritimeIdentityPage, maritimeDetails).success.value
          }

          val expected = expectedResult.copy(identity = maritimeDetails, mode = Maritime, nationality = None)
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should never extract nationality of transport" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Maritime).success.value
              .set(NationalityOfTransportPage, nationality).success.value
              .set(MaritimeIdentityPage, maritimeDetails).success.value
          }

          val expected = expectedResult.copy(identity = maritimeDetails, mode = Maritime, nationality = None)
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should fail if transport identity was not answered" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Maritime).success.value
              .remove(MaritimeIdentityPage).success.value
          }

          val expected = List(MissingField(MaritimeIdentityPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs (expected)
        }
      }

      "for mode Rail" - {
        "should extract correctly" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Rail).success.value
              .remove(NationalityOfTransportPage).success.value
              .set(RailIdentityPage, railDetails).success.value
          }

          val expected = expectedResult.copy(identity = railDetails, mode = Rail, nationality = None)
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should never extract nationality of transport" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Rail).success.value
              .set(NationalityOfTransportPage, nationality).success.value
              .set(RailIdentityPage, railDetails).success.value
          }

          val expected = expectedResult.copy(identity = railDetails, mode = Rail, nationality = None)
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should fail if transport identity was not answered" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Rail).success.value
              .remove(RailIdentityPage).success.value
          }

          val expected = List(MissingField(RailIdentityPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs (expected)
        }
      }


      "for mode Road" - {
        "should extract correctly" in {
          val answers = {
            validAnswers
              .set(TransportModePage, RoroAccompanied).success.value
              .set(NationalityOfTransportPage, nationality).success.value
              .set(RoroAccompaniedIdentityPage, roroAccompaniedDetails).success.value
          }

          val expected = expectedResult.copy(
            identity = roroAccompaniedDetails,
            mode = RoroAccompanied,
            nationality = Some(nationality),
          )
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should fail when nationality is not provided" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Road).success.value
              .remove(NationalityOfTransportPage).success.value
              .set(RoadIdentityPage, roadDetails).success.value
          }

          val expected = List(MissingField(NationalityOfTransportPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }

        "should fail if transport identity is not answered" in {
          val answers = {
            validAnswers
              .set(TransportModePage, Road).success.value
              .remove(RoadIdentityPage).success.value
          }

          val expected = List(MissingField(RoadIdentityPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }
      }

      "for mode RoroAccompanied" - {
        "should extract correctly" in {
          val answers = {
            validAnswers
              .set(TransportModePage, RoroAccompanied).success.value
              .set(NationalityOfTransportPage, nationality).success.value
              .set(RoroAccompaniedIdentityPage, roroAccompaniedDetails).success.value
          }

          val expected = expectedResult.copy(
            identity = roroAccompaniedDetails,
            mode = RoroAccompanied,
            nationality = Some(nationality),
          )
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should fail when nationality is not provided" in {
          val answers = {
            validAnswers
              .set(TransportModePage, RoroAccompanied).success.value
              .remove(NationalityOfTransportPage).success.value
              .set(RoroAccompaniedIdentityPage, roroAccompaniedDetails).success.value
          }

          val expected = List(MissingField(NationalityOfTransportPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }

        "should fail if transport identity is not answered" in {
          val answers = {
            validAnswers
              .set(TransportModePage, RoroAccompanied).success.value
              .remove(RoadIdentityPage).success.value
              .remove(RoroAccompaniedIdentityPage).success.value
          }

          val expected = List(MissingField(RoroAccompaniedIdentityPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }
      }

      "for mode RoroUnaccompanied" - {
        "should extract correctly" in {
          val answers = {
            validAnswers
              .set(TransportModePage, RoroUnaccompanied).success.value
              .set(NationalityOfTransportPage, nationality).success.value
              .set(RoroUnaccompaniedIdentityPage, roroUnaccompaniedDetails).success.value
          }

          val expected = expectedResult.copy(
            identity = roroUnaccompaniedDetails,
            mode = RoroUnaccompanied,
            nationality = Some(nationality),
          )
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should fail when nationality is not provided" in {
          val answers = {
            validAnswers
              .set(TransportModePage, RoroUnaccompanied).success.value
              .remove(NationalityOfTransportPage).success.value
              .set(RoroUnaccompaniedIdentityPage, roroUnaccompaniedDetails).success.value
          }

          val expected = List(MissingField(NationalityOfTransportPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }

        "should fail if transport identity is not answered" in {
          val answers = {
            validAnswers
              .set(TransportModePage, RoroUnaccompanied).success.value
              .remove(RoadIdentityPage).success.value
              .remove(RoroUnaccompaniedIdentityPage).success.value
          }

          val expected = List(MissingField(RoroUnaccompaniedIdentityPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }
      }

      "documents" - {
        "should correctly extract if no documents are added" in {
          val answers = {
            validAnswers
              .set(AnyOverallDocumentsPage, false).success.value
              .set(AllOverallDocumentsQuery, Nil).success.value
          }

          val expected = expectedResult.copy(documents = Nil)
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should fail if no answer to add overall documents page" in {
          val answers = validAnswers.remove(AnyOverallDocumentsPage).success.value

          val expected = List(MissingField(AnyOverallDocumentsPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }

        "should fail if documents are to be added but no documents are provided" in {
          val answers = validAnswers.set(AllOverallDocumentsQuery, Nil).success.value

          val expected = List(MissingField(OverallDocumentPage(Index(0))))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }
      }

      "seals" - {
        "should correctly extract if no seals are added" in {
          val answers = {
            validAnswers
              .set(AddAnySealsPage, false).success.value
              .set(AllSealsQuery, Nil).success.value
          }

          val expected = expectedResult.copy(seals = Nil)
          val actual = new TransportExtractor()(answers).extract().value

          actual must be(expected)
        }

        "should fail if no answer to add seals page" in {
          val answers = validAnswers.remove(AddAnySealsPage).success.value

          val expected = List(MissingField(AddAnySealsPage))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }

        "should fail if seals are to be added but no seals are provided" in {
          val answers = validAnswers.set(AllSealsQuery, Nil).success.value

          val expected = List(MissingField(SealPage(Index(0))))
          val actual = new TransportExtractor()(answers).extract().invalidValue.toList

          actual must contain theSameElementsAs(expected)
        }
      }
    }
}
