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

package base

import java.time.{Clock, Instant, LocalDate, ZoneId}
import java.time.temporal.ChronoUnit

import cats.scalatest.ValidatedValues
import controllers.actions._
import generators.Generators
import models.{GbEori, Index, LocalReferenceNumber, UserAnswers}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{OptionValues, TryValues}
import play.api.Application
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest

trait SpecBase
  extends AnyFreeSpec
  with Matchers
  with TryValues
  with OptionValues
  with ValidatedValues
  with ScalaFutures
  with IntegrationPatience
  with Generators {

  protected val index: Index = Index(0)
  protected val userAnswersId: String = "id"
  protected val lrn: LocalReferenceNumber = LocalReferenceNumber("ABC123")

  protected val arbitraryDate: LocalDate =
    datesBetween(LocalDate.of(2022, 7, 1), LocalDate.of(2050, 12, 31)).sample.value
  protected val arbitraryInstant: Instant =
    arbitraryDate.atStartOfDay(ZoneId.systemDefault).toInstant
  protected val stubClockAtArbitraryDate: Clock =
    Clock.fixed(arbitraryInstant, ZoneId.systemDefault)

  // For fields which write minute-precision datetimes
  protected val minutePrecisionInstants = arbitrary[Instant](arbitraryRecentInstant) map {
    _.truncatedTo(ChronoUnit.MINUTES)
  }

  protected def emptyUserAnswers: UserAnswers =
    UserAnswers(userAnswersId, lrn, lastUpdated = arbitraryInstant)

  protected val eori = new GbEori("123456789000")

  def messages(app: Application): Messages =
    app.injector.instanceOf[MessagesApi].preferred(FakeRequest())

  protected def applicationBuilder(
    userAnswers: Option[UserAnswers] = None,
    clock: Clock = stubClockAtArbitraryDate
  ): GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .configure(
        "dangerous-goods-file" -> "test-dangerousGoods.json"
      )
      .overrides(
        bind[DataRequiredAction].to[DataRequiredActionImpl],
        bind[IdentifierAction].to[FakeIdentifierAction],
        bind[DataRetrievalActionProvider]
          .toInstance(new FakeDataRetrievalActionProvider(userAnswers)),
        bind[Clock].toInstance(clock)
      )
}
