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

package controllers.actions

import models.requests.{IdentifierRequest, OptionalDataRequest}
import models.{LocalReferenceNumber, UserAnswers}
import org.scalatestplus.mockito.MockitoSugar.mock
import repositories.SessionRepository

import scala.concurrent.{ExecutionContext, Future}

class FakeDataRetrievalAction(dataToReturn: Option[UserAnswers])
  extends DataRetrievalAction(
    LocalReferenceNumber("ABC123"),
    mock[SessionRepository]
  )(ExecutionContext.Implicits.global) {

  override protected def transform[A](
    request: IdentifierRequest[A]
  ): Future[OptionalDataRequest[A]] =
    Future(OptionalDataRequest(request.request, request.eori, dataToReturn))
}

class FakeDataRetrievalActionProvider(dataToReturn: Option[UserAnswers])
  extends DataRetrievalActionProvider(mock[SessionRepository])(ExecutionContext.Implicits.global) {

  override def apply(len: LocalReferenceNumber): DataRetrievalAction =
    new FakeDataRetrievalAction(dataToReturn)
}
