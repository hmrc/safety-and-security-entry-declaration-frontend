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

import com.google.inject.{Inject, Singleton}
import models.DangerousGood
import play.api.{Configuration, Environment}
import scala.io.Source
import play.api.libs.json.Json

@Singleton
class DangerousGoodsService @Inject() (env: Environment, configuration: Configuration) {

  private val dangerousGoodsFile: String = configuration.get[String]("dangerous-goods-file")

  val allDangerousGoods: Seq[DangerousGood] = {
    val json = env
      .resourceAsStream(dangerousGoodsFile)
      .fold(throw new Exception("no dangerous good file found"))(Source.fromInputStream)
      .mkString

    Json.parse(json).as[Seq[DangerousGood]]
  }
}
