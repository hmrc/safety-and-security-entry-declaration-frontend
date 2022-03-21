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

import models.Container
import scala.xml.NodeSeq

trait GoodItemsFormats extends CommonFormats {

  implicit val containerFmt = new Format[Container] {
    override def encode(container: Container): NodeSeq = {
      <ConNumNR21>{container.itemContainerNumber}</ConNumNR21>
    }
    override def decode(data: NodeSeq): Container = Container(data.text)
  }
}
