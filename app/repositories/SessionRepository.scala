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

package repositories

import config.FrontendAppConfig
import models.{LocalReferenceNumber, UserAnswers}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model._
import play.api.libs.json.Format
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import uk.gov.hmrc.mongo.play.json.formats.MongoJavatimeFormats

import java.time.{Clock, Instant}
import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SessionRepository @Inject() (
  mongoComponent: MongoComponent,
  appConfig: FrontendAppConfig,
  clock: Clock
)(implicit ec: ExecutionContext)
  extends PlayMongoRepository[UserAnswers](
    collectionName = "user-answers",
    mongoComponent = mongoComponent,
    domainFormat = UserAnswers.format,
    indexes = Seq(
      IndexModel(
        Indexes.ascending("lastUpdated"),
        IndexOptions()
          .name("lastUpdatedIdx")
          .expireAfter(appConfig.cacheTtl, TimeUnit.SECONDS)
      ),
      IndexModel(
        Indexes.ascending("userId", "lrn"),
        IndexOptions()
          .name("userIdAndLrnIdx")
          .unique(true)
      )
    )
  ) {

  implicit val instantFormat: Format[Instant] = MongoJavatimeFormats.instantFormat

  private def byUserId(userId: String): Bson = Filters.equal("userId", userId)

  private def byUserIdAndLrn(userId: String, lrn: LocalReferenceNumber): Bson =
    Filters.and(
      Filters.equal("userId", userId),
      Filters.equal("lrn", lrn.value)
    )

  def keepAlive(userId: String): Future[Boolean] =
    collection
      .updateOne(
        filter = byUserId(userId),
        update = Updates.set("lastUpdated", Instant.now(clock))
      )
      .toFuture
      .map(_ => true)

  def get(userId: String, lrn: LocalReferenceNumber): Future[Option[UserAnswers]] =
    keepAlive(userId).flatMap { _ =>
      collection
        .find(byUserIdAndLrn(userId, lrn))
        .headOption
    }

  def set(answers: UserAnswers): Future[Boolean] = {

    val updatedAnswers = answers copy (lastUpdated = Instant.now(clock))

    collection
      .replaceOne(
        filter = byUserIdAndLrn(updatedAnswers.id, updatedAnswers.lrn),
        replacement = updatedAnswers,
        options = ReplaceOptions().upsert(true)
      )
      .toFuture
      .map(_ => true)
  }

  def clear(userId: String): Future[Boolean] =
    collection
      .deleteMany(byUserId(userId))
      .toFuture
      .map(_ => true)
}
