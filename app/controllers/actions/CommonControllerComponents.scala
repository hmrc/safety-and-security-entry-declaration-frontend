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

import models.LocalReferenceNumber
import models.requests.{DataRequest, OptionalDataRequest}
import play.api.http.FileMimeTypes
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc._
import repositories.SessionRepository

import javax.inject.Inject
import scala.concurrent.ExecutionContext

trait CommonControllerComponents extends MessagesControllerComponents {

  def sessionRepository: SessionRepository
  def identify: IdentifierAction
  def getData: DataRetrievalActionProvider
  def requireData: DataRequiredAction
  def limitIndex: LimitIndexActionProvider

  def authAndGetData(lrn: LocalReferenceNumber): ActionBuilder[DataRequest, AnyContent] =
    identify andThen getData(lrn) andThen requireData

  def authAndGetOptionalData(lrn: LocalReferenceNumber): ActionBuilder[OptionalDataRequest, AnyContent] =
    identify andThen getData(lrn)
}

case class DefaultCommonControllerComponents @Inject()(
  messagesActionBuilder: MessagesActionBuilder,
  actionBuilder: DefaultActionBuilder,
  parsers: PlayBodyParsers,
  messagesApi: MessagesApi,
  langs: Langs,
  fileMimeTypes: FileMimeTypes,
  executionContext: ExecutionContext,
  identify: IdentifierAction,
  getData: DataRetrievalActionProvider,
  requireData: DataRequiredAction,
  limitIndex: LimitIndexActionProvider,
  sessionRepository: SessionRepository
) extends CommonControllerComponents
