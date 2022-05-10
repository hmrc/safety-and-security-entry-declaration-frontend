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

import base.SpecBase
import controllers.routes
import models.Index
import models.requests.DataRequest
import play.api.mvc.Result
import play.api.mvc.Results.Redirect
import play.api.test.FakeRequest

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LimitIndexActionSpec extends SpecBase {

  class Harness(index: Index, maxAllowed: Int) extends LimitIndexAction(index, maxAllowed) {

    def callFilter[A](request: DataRequest[A]): Future[Option[Result]] =
      filter(request)
  }

  private val request = DataRequest(FakeRequest(), "GB123456789000", emptyUserAnswers)

  "Limit Index Action" - {

    "when the current index's position is below the max allowed" - {

      "must not filter the request" in {

        val index = Index(1)
        val max = 2
        val controller = new Harness(index, max)

        val result = controller.callFilter(request).futureValue

        result must not be defined
      }
    }

    "when the current index's position is equal to the max allowed" - {

      "must filter the request" in {

        val index = Index(2)
        val max = 2
        val controller = new Harness(index, max)

        val result = controller.callFilter(request).futureValue

        result.value mustEqual Redirect(routes.JourneyRecoveryController.onPageLoad())
      }
    }

    "when the current index's position is above the max allowed" - {

      "must filter the request" in {

        val index = Index(3)
        val max = 2
        val controller = new Harness(index, max)

        val result = controller.callFilter(request).futureValue

        result.value mustEqual Redirect(routes.JourneyRecoveryController.onPageLoad())
      }
    }
  }
}
