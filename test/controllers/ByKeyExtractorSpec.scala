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

package controllers

import base.SpecBase
import models.requests.DataRequest
import models.{Index, UserAnswers, WithKey}
import pages.QuestionPage
import play.api.libs.json.{JsPath, Json, OFormat}
import play.api.mvc.Results.Ok
import play.api.mvc.{AnyContent, Result}
import play.api.test.FakeRequest
import queries.Gettable

import scala.concurrent.Future

class ByKeyExtractorSpec extends SpecBase {

  private case class Item(key: Int) extends WithKey

  private object Item {
    implicit val format: OFormat[Item] = Json.format[Item]
  }

  private case class ItemPage(index: Index) extends QuestionPage[Item] {
    override def path: JsPath = JsPath \ "items" \ index.position
  }

  private case object AllItems extends Gettable[List[Item]] {
    override def path: JsPath = JsPath \ "items"
  }

  private def getRequest(answers: UserAnswers): DataRequest[AnyContent] =
    DataRequest(FakeRequest(), answers.id, answers)

  private class TestController extends ByKeyExtractor {

    def get(index: Index)(implicit request: DataRequest[AnyContent]): Int =
      getItemKey(index, AllItems) {
        id =>
          id
      }
  }

  "getItemKey" - {

    "must return 1 when there are no items in the user's answers" in {

      implicit val request = getRequest(emptyUserAnswers)

      val controller = new TestController()

      controller.get(Index(0)) mustEqual 1
    }

    "must return the existing Id of the item if it already exists in the user's answers" in {

      val answers =
        emptyUserAnswers
          .set(ItemPage(Index(0)), Item(1)).success.value
          .set(ItemPage(Index(1)), Item(2)).success.value

      implicit val request = getRequest(answers)

      val controller = new TestController()

      controller.get(Index(0)) mustEqual 1
      controller.get(Index(1)) mustEqual 2
    }

    "must return a value 1 greater than the maximum existing key when this item isn't already in the user's answers" in {

      val answers =
        emptyUserAnswers
          .set(ItemPage(Index(0)), Item(2)).success.value
          .set(ItemPage(Index(1)), Item(1)).success.value

      implicit val request = getRequest(answers)

      val controller = new TestController()

      controller.get(Index(2)) mustEqual 3
    }
  }
}
