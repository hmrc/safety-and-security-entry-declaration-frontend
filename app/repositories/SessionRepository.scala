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
import views.html.declarations.DraftDeclarationsView

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
        Indexes.ascending("eori", "lrn"),
        IndexOptions()
          .name("eoriAndLrnIdx")
          .unique(true)
      )
    )
  ) {

  implicit val instantFormat: Format[Instant] = MongoJavatimeFormats.instantFormat

  private def byEORI(eori: String): Bson = Filters.equal("eori", eori)

  private def byEORIandLrn(eori: String, lrn: LocalReferenceNumber): Bson =
    Filters.and(
      Filters.equal("eori", eori),
      Filters.equal("lrn", lrn.value)
    )

  def keepAlive(eori: String): Future[Boolean] =
    collection
      .updateOne(
        filter = byEORI(eori),
        update = Updates.set("lastUpdated", Instant.now(clock))
      )
      .toFuture
      .map(_ => true)

  def get(eori: String, lrn: LocalReferenceNumber): Future[Option[UserAnswers]] =
    keepAlive(eori).flatMap { _ =>
      collection
        .find(byEORIandLrn(eori, lrn))
        .headOption
    }

  def getSummaryList(eori: String): Future[Seq[LocalReferenceNumber]] =
    keepAlive(eori).flatMap { _ =>
      collection
        .find(byEORI(eori))
        .map { l => l.lrn }
        .toFuture()
    }

  def set(answers: UserAnswers): Future[Boolean] = {

    val updatedAnswers = answers copy (lastUpdated = Instant.now(clock))

    collection
      .replaceOne(
        filter = byEORIandLrn(updatedAnswers.id, updatedAnswers.lrn),
        replacement = updatedAnswers,
        options = ReplaceOptions().upsert(true)
      )
      .toFuture
      .map(_ => true)
  }

  def clear(eori: String): Future[Boolean] =
    collection
      .deleteMany(byEORI(eori))
      .toFuture
      .map(_ => true)
}
