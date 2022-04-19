package repositories

import config.FrontendAppConfig
import generators.Generators
import java.time.{Clock, Instant, ZoneId}

import models.{LocalReferenceNumber, UserAnswers}
import org.mockito.Mockito.when
import org.mongodb.scala.model.Filters
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport

class SessionRepositorySpec
  extends AnyFreeSpec
  with Matchers
  with DefaultPlayMongoRepositorySupport[UserAnswers]
  with ScalaFutures
  with IntegrationPatience
  with OptionValues
  with MockitoSugar
  with Generators {

  private val instant = Instant.now
  private val stubClock: Clock = Clock.fixed(instant, ZoneId.systemDefault)

  private val userId1 = "id1"
  private val userId2 = "id2"
  private val lrn1 = LocalReferenceNumber("ABC123")
  private val lrn2 = LocalReferenceNumber("DEF456")
  private val userAnswers1 =
    UserAnswers(userId1, lrn1, Json.obj("foo" -> "bar"), Instant.ofEpochSecond(1))
  private val userAnswers2 =
    UserAnswers(userId1, lrn2, Json.obj("bar" -> "baz"), Instant.ofEpochSecond(2))
  private val userAnswers3 =
    UserAnswers(userId2, lrn1, Json.obj("bar" -> "baz"), Instant.ofEpochSecond(3))

  private val mockAppConfig = mock[FrontendAppConfig]
  when(mockAppConfig.cacheTtl) thenReturn 1

  private def getFilter(userId: String, lrn: LocalReferenceNumber) =
    Filters.and(
      Filters.equal("userId", userId),
      Filters.equal("lrn", lrn.value)
    )

  protected override val repository = new SessionRepository(
    mongoComponent = mongoComponent,
    appConfig = mockAppConfig,
    clock = stubClock
  )

  ".set" - {

    "must insert a record when it does not already exist in Mongo, setting the last updated time to `now`" in {

      val expectedResult = userAnswers1 copy (lastUpdated = instant)

      val setResult = repository.set(userAnswers1).futureValue
      val updatedRecord = find(getFilter(userId1, lrn1)).futureValue.headOption.value

      setResult mustEqual true
      updatedRecord mustEqual expectedResult
    }

    "must update existing records with the same user id and lrn, setting the last updated time to `now`" in {

      insert(userAnswers1).futureValue
      insert(userAnswers2).futureValue
      insert(userAnswers3).futureValue

      val setResult = repository.set(userAnswers2).futureValue

      setResult mustEqual true

      find(getFilter(userId1, lrn1)).futureValue.headOption.value mustEqual userAnswers1
      find(getFilter(userId1, lrn2)).futureValue.headOption.value mustEqual userAnswers2.copy(lastUpdated = instant)
      find(getFilter(userId2, lrn1)).futureValue.headOption.value mustEqual userAnswers3
    }
  }

  ".getLrns" - {

    "When there are draft declarations will return their lrns" - {

      val refs: List[LocalReferenceNumber] = {
        Gen.choose(1, 5) map { len => Gen.listOfN(len, arbitrary[LocalReferenceNumber]) }
      }.sample.value.sample.value

      refs.foreach { lrn => insert(userAnswers1.copy(lrn = lrn)).futureValue }

      val result = repository.getLrns(userId1).futureValue
      result must contain theSameElementsAs (refs)
    }

    "When there are no draft declarations will return an empty list" - {
      repository.getLrns(userId = "id that does not exist").futureValue should have size 0
    }
  }

  ".get" - {

    "when there is a record for this id and lrn" - {

      "must update the lastUpdated time and get the record" in {

        insert(userAnswers1).futureValue

        val result = repository.get(userId1, lrn1).futureValue
        val expectedResult = userAnswers1 copy (lastUpdated = instant)

        result.value mustEqual expectedResult
      }
    }

    "when there is no record for this user" - {

      "must return None" in {

        repository.get("id that does not exist", lrn1).futureValue must not be defined
      }
    }

    "when there is a record for this user but not for this LRN" - {

      "must return None" in {

        insert(userAnswers1).futureValue
        repository
          .get("id", LocalReferenceNumber("LRN that does not exist"))
          .futureValue must not be defined
      }
    }
  }

  ".clear" - {

    "must remove a record" in {

      insert(userAnswers1).futureValue

      val result = repository.clear(userAnswers1.id).futureValue

      result mustEqual true
      repository.get(userId1, lrn1).futureValue must not be defined
    }

    "must return true when there is no record to remove" in {
      val result = repository.clear("id that does not exist").futureValue

      result mustEqual true
    }
  }

  ".keepAlive" - {

    "when there is a record for this id" - {

      "must update its lastUpdated to `now` and return true" in {

        insert(userAnswers1).futureValue

        val result = repository.keepAlive(userAnswers1.id).futureValue

        val expectedUpdatedAnswers = userAnswers1 copy (lastUpdated = instant)

        result mustEqual true
        val updatedAnswers = find(getFilter(userId1, lrn1)).futureValue.headOption.value
        updatedAnswers mustEqual expectedUpdatedAnswers
      }
    }

    "when there is no record for this user" - {

      "must return true" in {

        repository.keepAlive("id that does not exist").futureValue mustEqual true
      }
    }
  }
}
