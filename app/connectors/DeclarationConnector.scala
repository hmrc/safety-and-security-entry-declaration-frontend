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

import scala.concurrent.ExecutionContext

import com.google.inject.Inject
import uk.gov.hmrc.http.HttpClient

import config.FrontendAppConfig

class DeclarationConnector @Inject() (
  cfg: FrontendAppConfig,
  override protected val httpClient: HttpClient
)(override protected implicit val ec: ExecutionContext) extends DeclarationConnecting {
  override protected val storeUrl = cfg.declarationStoreUrl
  override protected val outcomesUrl = cfg.declarationOutcomesUrl
}
