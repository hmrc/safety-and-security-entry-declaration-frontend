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

package queries.goods

import models.{Container, Index}
import play.api.libs.json.JsPath
import queries.{Gettable, Settable}

final case class AllContainersQuery(index: Index) extends Gettable[List[Container]] with Settable[List[Container]] {

  override def path: JsPath = JsPath \ "goodsItems" \ index.position \ "containers"
}