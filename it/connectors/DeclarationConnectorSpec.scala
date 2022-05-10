package connectors

import com.github.tomakehurst.wiremock.client.WireMock._
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.test.WireMockSupport

import base.SpecBase
import models.MovementReferenceNumber
import models.completion.downstream.{CorrelationId, Outcome}
import serialisation.xml.XmlPayloadFixtures

class DeclarationConnectorSpec
  extends SpecBase
  with WireMockSupport
  with XmlPayloadFixtures {

  private implicit val hc: HeaderCarrier = HeaderCarrier()

  private def app = applicationBuilder().configure(
    "microservice.services.iced-store.host" -> wireMockHost,
    "microservice.services.iced-store.port" -> wireMockPort,
    "microservice.services.iced-outcome.host" -> wireMockHost,
    "microservice.services.iced-outcome.port" -> wireMockPort
  ).build()

  "The declaration connector" - {
    "when submitting a new declaration" - {
      "should extract correlation ID from a successful response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val dec = submissionGen.sample.value

          stubFor(
            post(urlEqualTo("/"))
              .willReturn(aResponse().withStatus(200).withBody(submissionResponse.toString))
          )

          client.submitDeclaration(dec).futureValue must be(submissionCorrId)

          verify(postRequestedFor(urlEqualTo("/")))
        }
      }

      "should report a specific bad request exception for a 400 response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val dec = submissionGen.sample.value

          stubFor(
            post(urlEqualTo("/"))
              .willReturn(aResponse().withStatus(400))
          )

          client.submitDeclaration(dec).failed.futureValue must be(
            a[DeclarationConnecting.BadRequestException]
          )

          verify(postRequestedFor(urlEqualTo("/")))
        }
      }

      "should report a general request failed exception for some other unexpected response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val dec = submissionGen.sample.value

          stubFor(
            post(urlEqualTo("/"))
              .willReturn(aResponse().withStatus(500))
          )

          client.submitDeclaration(dec).failed.futureValue must be(
            a[DeclarationConnecting.RequestFailedException]
          )

          verify(postRequestedFor(urlEqualTo("/")))
        }
      }
    }

    "when listing outcomes" - {
      "should extract a list of correlation IDs from the response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]

          stubFor(
            get(urlEqualTo("/"))
              .willReturn(aResponse().withStatus(200).withBody(listOutcomesResponse.toString))
          )

          client.listOutcomes().futureValue must be(outcomeCorrIds)

          verify(getRequestedFor(urlEqualTo("/")))
        }
      }

      "should extract an empty list of correlation IDs from a 204 response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]

          stubFor(
            get(urlEqualTo("/"))
              .willReturn(aResponse().withStatus(204))
          )

          client.listOutcomes().futureValue must be(Nil)

          verify(getRequestedFor(urlEqualTo("/")))
        }
      }

      "should report a request failed exception for an unexpected response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]

          stubFor(
            get(urlEqualTo("/"))
              .willReturn(aResponse().withStatus(500))
          )

          client.listOutcomes().failed.futureValue must be(
            a[DeclarationConnecting.RequestFailedException]
          )

          verify(getRequestedFor(urlEqualTo("/")))
        }
      }
    }

    "when retrieving one outcome" - {
      "should extract the outcome from the response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val corrId = CorrelationId("123_456")
          val outcome = Outcome.Accepted(
            CorrelationId("0JRF7UncK0t004"),
            MovementReferenceNumber("10GB08I01234567891")
          )

          stubFor(
            get(urlEqualTo(s"/${corrId.id}"))
              .willReturn(aResponse().withStatus(200).withBody(acceptedSubmissionOutcome.toString))
          )

          client.fetchOutcome(corrId).futureValue must be(outcome)

          verify(getRequestedFor(urlEqualTo(s"/${corrId.id}")))
        }
      }

      "should report a request failed exception for some unexpected response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val corrId = CorrelationId("123_456")

          stubFor(
            get(urlEqualTo(s"/${corrId.id}"))
              .willReturn(aResponse().withStatus(500))
          )

          client.fetchOutcome(corrId).failed.futureValue must be(
            a[DeclarationConnecting.RequestFailedException]
          )

          verify(getRequestedFor(urlEqualTo(s"/${corrId.id}")))
        }
      }
    }

    "when acknowledging one outcome" - {
      "should successfully make a DELETE request" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val corrId = CorrelationId("123_456")

          stubFor(
            delete(urlEqualTo(s"/${corrId.id}"))
              .willReturn(aResponse().withStatus(200))
          )

          client.ackOutcome(corrId).futureValue must be(())

          verify(deleteRequestedFor(urlEqualTo(s"/${corrId.id}")))
        }
      }

      "should report a request failed exception for some unexpected response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val corrId = CorrelationId("123_456")

          stubFor(
            delete(urlEqualTo(s"/${corrId.id}"))
              .willReturn(aResponse().withStatus(500))
          )

          client.ackOutcome(corrId).failed.futureValue must be(
            a[DeclarationConnecting.RequestFailedException]
          )

          verify(deleteRequestedFor(urlEqualTo(s"/${corrId.id}")))
        }
      }
    }

    "when submitting an amended declaration" - {
      "should extract a correlation ID from a successful response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val dec = amendmentGen.sample.value
          val mrn = dec.header.ref.asInstanceOf[MovementReferenceNumber].value

          stubFor(
            put(urlEqualTo(s"/$mrn"))
              .willReturn(aResponse().withStatus(200).withBody(submissionResponse.toString))
          )

          client.amendDeclaration(dec).futureValue must be(submissionCorrId)

          verify(putRequestedFor(urlEqualTo(s"/$mrn")))
        }
      }

      "should report an error when movement reference number is not provided" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val dec = submissionGen.sample.value

          client.amendDeclaration(dec).failed.futureValue must be(
            a[DeclarationConnecting.InvalidAmendmentException]
          )

          verify(0, anyRequestedFor(anyUrl()))
        }
      }

      "should report a request failed exception for some unexpected response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val dec = amendmentGen.sample.value
          val mrn = dec.header.ref.asInstanceOf[MovementReferenceNumber].value

          stubFor(
            put(urlEqualTo(s"/$mrn"))
              .willReturn(aResponse().withStatus(500))
          )

          client.amendDeclaration(dec).failed.futureValue must be(
            a[DeclarationConnecting.RequestFailedException]
          )

          verify(putRequestedFor(urlEqualTo(s"/$mrn")))
        }
      }
    }
  }
}
