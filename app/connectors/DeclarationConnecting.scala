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

package connectors

import models.MovementReferenceNumber

import java.net.URL
import scala.concurrent.{ExecutionContext, Future}
import scala.xml.XML
import uk.gov.hmrc.http._
import models.completion.downstream.{CorrelationId, Declaration, Outcome}
import serialisation.xml.{DeclarationFormats, ResponseFormats}
import serialisation.xml.XmlImplicits._

trait DeclarationConnecting extends DeclarationFormats {
  import DeclarationConnecting._

  protected val storeUrl: URL
  protected val outcomesUrl: URL
  protected val httpClient: HttpClient

  protected implicit val ec: ExecutionContext

  def submitDeclaration(declaration: Declaration)(
    implicit hc: HeaderCarrier
  ): Future[CorrelationId] = {
    httpClient.POSTString[CorrelationId](storeUrl, declaration.toXml.toString)
  }

  def listOutcomes()(implicit hc: HeaderCarrier): Future[List[CorrelationId]] = {
    httpClient.GET[List[CorrelationId]](outcomesUrl)
  }

  def fetchOutcome(
    correlationId: CorrelationId
  )(implicit hc: HeaderCarrier): Future[Outcome] = {
    httpClient.GET[Outcome](url"$outcomesUrl/${correlationId.id}")
  }

  def ackOutcome(correlationId: CorrelationId)(implicit hc: HeaderCarrier): Future[Unit] = {
    httpClient.DELETE[Unit](url"$outcomesUrl/${correlationId.id}")
  }

  def amendDeclaration(mrn:MovementReferenceNumber, declaration: Declaration)(implicit hc: HeaderCarrier): Future[CorrelationId] = {
    httpClient.PUTString[CorrelationId](url"$storeUrl/${mrn.value}", declaration.toXml.toString)
  }
}

object DeclarationConnecting extends ResponseFormats {
  class RequestFailedException(status: Int, body: String) extends RuntimeException(
    s"API responded with unexpected status: $status; full response body: $body"
  )
  class BadRequestException(body: String) extends RequestFailedException(400, body)

  private def handleFailures[T](response: HttpResponse)(onSuccess: => T): T = {
    response.status match {
      case 400 =>
        throw new BadRequestException(response.body)
      case status if status < 200 || status >= 300 =>
        throw new RequestFailedException(status, response.body)
      case _ =>
        onSuccess
    }
  }

  implicit val declarationResponseReads: HttpReads[CorrelationId] = new HttpReads[CorrelationId] {
    override def read(method: String, url: String, response: HttpResponse): CorrelationId = {
      handleFailures(response) {
        XML.loadString(response.body).parseXml[CorrelationId]
      }
    }
  }

  implicit val listOutcomesResponseReads: HttpReads[List[CorrelationId]] = {
    new HttpReads[List[CorrelationId]] {
      override def read(method: String, url: String, response: HttpResponse): List[CorrelationId] = {
        handleFailures(response) {
          if (response.status == 204) {
            Nil
          } else {
            XML.loadString(response.body).parseXml[List[CorrelationId]]
          }
        }
      }
    }
  }

  implicit val fetchOutcomeResponseReads: HttpReads[Outcome] = new HttpReads[Outcome] {
    override def read(method: String, url: String, response: HttpResponse): Outcome = {
      handleFailures(response) {
        XML.loadString(response.body).parseXml[Outcome]
      }
    }
  }

  implicit val noContentResponseReads: HttpReads[Unit] = new HttpReads[Unit] {
    override def read(method: String, url: String, response: HttpResponse): Unit = {
      handleFailures(response)(())
    }
  }
}
