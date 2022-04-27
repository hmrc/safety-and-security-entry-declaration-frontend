package repositories

import config.FrontendAppConfig
import generators.Generators
import java.time.{Clock, Instant, ZoneId}
import java.time.temporal.ChronoUnit
import models.{LocalReferenceNumber, MovementReferenceNumber, UserAnswers}
import models.completion.DeclarationEvent
import models.completion.downstream.{CorrelationId, MessageType, Outcome}
import org.mockito.Mockito.when
import org.mongodb.scala.model.Filters
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Json
import scala.concurrent.Future
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

  private val instant = Instant.now.truncatedTo(ChronoUnit.MILLIS)
  private val stubClock: Clock = Clock.fixed(instant, ZoneId.systemDefault)

  private val userId1 = "id1"
  private val userId2 = "id2"
  private val lrn1 = LocalReferenceNumber("ABC123")
  private val lrn2 = LocalReferenceNumber("DEF456")
  private val lrn3 = LocalReferenceNumber("HIG789")

  private val user1Answers1 =
    UserAnswers(userId1, lrn1, Map.empty, Json.obj("foo" -> "bar"), Instant.ofEpochSecond(1))
  private val user1Answers2 =
    UserAnswers(userId1, lrn2, Map.empty, Json.obj("bar" -> "baz"), Instant.ofEpochSecond(2))
  private val user1Answers3 =
    UserAnswers(userId1, lrn3, Map.empty, Json.obj("bar" -> "baz"), Instant.ofEpochSecond(3))
  private val user2Answers1 =
    UserAnswers(userId2, lrn1, Map.empty, Json.obj("bar" -> "baz"), Instant.ofEpochSecond(3))

  private val corrId1 = CorrelationId("abcdef")
  private val event1 = DeclarationEvent(MessageType.Submission, outcome = None)
  private val outcome1 = {
    Outcome.Accepted(corrId1, event1.messageType, instant, MovementReferenceNumber("123"))
  }

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

      val expectedResult = user1Answers1.copy(lastUpdated = instant)

      val setResult = repository.set(user1Answers1).futureValue
      val updatedRecord = find(getFilter(userId1, lrn1)).futureValue.headOption.value

      setResult mustEqual true
      updatedRecord mustEqual expectedResult
    }

    "must update existing records with the same user id and lrn, setting the last updated time to `now`" in {
      val answers = Seq(user1Answers1, user1Answers2, user2Answers1)
      Future.traverse(answers)(insert).futureValue

      val setResult = repository.set(user1Answers2).futureValue

      setResult mustEqual true

      find(getFilter(userId1, lrn1)).futureValue.headOption.value mustEqual user1Answers1
      find(getFilter(userId1, lrn2)).futureValue.headOption.value mustEqual user1Answers2.copy(lastUpdated = instant)
      find(getFilter(userId2, lrn1)).futureValue.headOption.value mustEqual user2Answers1
    }
  }

  ".getSummaryList" - {

    "when there are draft declarations will return their lrns" - {
      // LRN 1 is inserted for user 2, so we should not retrieve it
      val answers = Seq(user2Answers1, user1Answers2, user1Answers3)
      val expected = Seq(lrn2, lrn3)

      Future.traverse(answers)(insert).futureValue

      val actual = repository.getSummaryList(userId1).futureValue
      actual must contain theSameElementsAs(expected)
    }

    "When there are no draft declarations will return an empty list" - {
      repository.getSummaryList(userId = "id that does not exist").futureValue should have size 0
    }
  }

  ".get" - {

    "when there is a record for this id and lrn" - {

      "must update the lastUpdated time and get the record" in {

        insert(user1Answers1).futureValue

        val result = repository.get(userId1, lrn1).futureValue
        val expectedResult = user1Answers1.copy(lastUpdated = instant)

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

        insert(user1Answers1).futureValue
        repository
          .get("id", LocalReferenceNumber("LRN that does not exist"))
          .futureValue must not be defined
      }
    }
  }

  ".clear" - {

    "must remove a record" in {

      insert(user1Answers1).futureValue

      val result = repository.clear(user1Answers1.id).futureValue

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

        insert(user1Answers1).futureValue

        val result = repository.keepAlive(user1Answers1.id).futureValue

        val expectedUpdatedAnswers = user1Answers1.copy(lastUpdated = instant)

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

  ".storeDeclarationEvent" - {
    "should add a declaration event for a valid document" in {
      insert(user1Answers1).futureValue
      repository.storeDeclarationEvent(userId1, lrn1, corrId1, event1).futureValue

      val expected = user1Answers1.withDeclarationEvent(corrId1, event1).copy(lastUpdated = instant)
      val actual = repository.get(userId1, lrn1).futureValue.value

      actual must be(expected)
    }

    "should fail with an illegal state exception if the document does not exist" in {
      repository.storeDeclarationEvent(userId1, lrn1, corrId1, event1).failed.futureValue must be(
        an[IllegalStateException]
      )
    }
  }
}
