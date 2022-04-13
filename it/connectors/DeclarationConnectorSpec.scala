package connectors

import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalacheck.Arbitrary.arbitrary
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.test.WireMockSupport

import base.SpecBase
import models.completion.downstream.Submission
import serialisation.xml.XmlPayloadFixtures

class DeclarationConnectorSpec
  extends SpecBase
  with WireMockSupport
  with XmlPayloadFixtures {

  private implicit val hc: HeaderCarrier = HeaderCarrier()

  private def app = applicationBuilder().configure(
    "microservice.services.iced-store.host" -> wireMockHost,
    "microservice.services.iced-store.port" -> wireMockPort
  ).build()

  "The declaration connector" - {
    "when submitting a new declaration" - {
      "should extract correlation ID from a successful response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val dec = arbitrary[Submission].sample.value

          stubFor(
            post(urlEqualTo("/"))
              .willReturn(aResponse().withStatus(200).withBody(submissionResponse.toString))
          )

          client.submitDeclaration(dec).futureValue must be(submissionCorrId)
        }
      }

      "should report a specific bad request exception for a 400 response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val dec = arbitrary[Submission].sample.value

          stubFor(
            post(urlEqualTo("/"))
              .willReturn(aResponse().withStatus(400))
          )

          client.submitDeclaration(dec).failed.futureValue must be(
            a[DeclarationConnecting.BadRequestException]
          )
        }
      }

      "should report a general request failed exception for some other unexpected response" in {
        running(app) {
          val client = app.injector.instanceOf[DeclarationConnector]
          val dec = arbitrary[Submission].sample.value

          stubFor(
            post(urlEqualTo("/"))
              .willReturn(aResponse().withStatus(500))
          )

          client.submitDeclaration(dec).failed.futureValue must be(
            a[DeclarationConnecting.RequestFailedException]
          )
        }
      }
    }
  }
}
