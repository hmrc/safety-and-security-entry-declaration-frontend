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

import java.net.URL
import scala.concurrent.{ExecutionContext, Future}
import scala.xml.XML

import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpReads, HttpResponse}

import models.completion.downstream.{CorrelationId, Submission}
import serialisation.xml.{ResponseFormats, SubmissionFormats}
import serialisation.xml.XmlImplicits._

trait DeclarationConnecting extends SubmissionFormats {
  import DeclarationConnecting._

  protected val storeUrl: URL
  protected val httpClient: HttpClient

  protected implicit val ec: ExecutionContext

  def submitDeclaration(declaration: Submission)(
    implicit hc: HeaderCarrier
  ): Future[CorrelationId] = {
    httpClient.POSTString[CorrelationId](storeUrl, declaration.toXml.toString)
  }
}

object DeclarationConnecting extends ResponseFormats {
  class RequestFailedException(status: Int, body: String) extends RuntimeException(
    s"API responded with unexpected status: $status; full response body: $body"
  )
  class BadRequestException(body: String) extends RequestFailedException(400, body)

  implicit val declarationResponseReads: HttpReads[CorrelationId] = new HttpReads[CorrelationId] {
    override def read(method: String, url: String, response: HttpResponse): CorrelationId = {
      response.status match {
        case 400 =>
          throw new BadRequestException(response.body)
        case status if status < 200 || status >= 300 =>
          throw new RequestFailedException(status, response.body)
        case _ =>
          XML.loadString(response.body).parseXml[CorrelationId]
      }
    }
  }
}
