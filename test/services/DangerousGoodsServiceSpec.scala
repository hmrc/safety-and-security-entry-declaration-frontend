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

package services

import base.SpecBase
import org.scalatestplus.mockito.MockitoSugar

class DangerousGoodsServiceSpec extends SpecBase with MockitoSugar {

  val application = applicationBuilder(None).build()

  def service: DangerousGoodsService = application.injector.instanceOf[DangerousGoodsService]

  "Dangerous Good Service Spec" - {
    "will expose all dangerous goods" in {
      val result = service.allDangerousGoods

      result.length mustEqual 4
      result.head.code mustEqual "4"
      result.head.name mustEqual "AMMONIUM PICRATE dry or wetted with less than 10% water, by mass"
    }
  }
}
