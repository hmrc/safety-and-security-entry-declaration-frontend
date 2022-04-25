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
import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.routes
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mockito.{Mockito, MockitoSugar}
import org.scalatest.BeforeAndAfterEach
import play.api.mvc.{Action, AnyContent, BodyParsers, DefaultActionBuilder, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.AffinityGroup.Organisation
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Retrieval, ~}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class AuthActionSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {

  private type RetrievalsType = Enrolments
  private val ssEnrolment = Enrolments(Set(Enrolment("HMRC-SS-ORG", Seq(EnrolmentIdentifier("EORINumber", "123456789")), "Activated")))
  private val inactiveSSEnrolment = Enrolments(Set(Enrolment("HMRC-SS-ORG", Seq(EnrolmentIdentifier("EORINumber", "123456789")), "Inactive")))
  private val ssEnrolmentNoEORI = Enrolments(Set(Enrolment("HMRC-SS-ORG", Seq(EnrolmentIdentifier("ARN", "123456789")), "Activated")))
  private val incorectEnrolment = Enrolments(Set(Enrolment("HMRC-AA-ORG", Seq(EnrolmentIdentifier("EORINumber", "123456789")), "Activated")))


  class Harness(authAction: IdentifierAction, defaultAction: DefaultActionBuilder) {
    def onPageLoad(): Action[AnyContent] = (defaultAction andThen authAction) { _ => Results.Ok }
  }

  val mockAuthConnector: AuthConnector = mock[AuthConnector]

  override def beforeEach(): Unit = {
    Mockito.reset(mockAuthConnector)
  }


  "Auth Action" - {
    "when the user is logged in with strong credentials" - {
      "must succeed" in {
        val application = applicationBuilder(None).build()

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig = application.injector.instanceOf[FrontendAppConfig]
          val actionBuilder = application.injector.instanceOf[DefaultActionBuilder]


          when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
            .thenReturn(Future.successful(ssEnrolment))

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector,
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction, actionBuilder)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustEqual OK
        }
      }
    }

    "When looking at the enrolments" - {
      "if the user has enrolments but not the required HMRC-SS-ORG" - {
        "must redirect to Enrolment Required Page" in {
          val application = applicationBuilder(None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
            val appConfig = application.injector.instanceOf[FrontendAppConfig]
            val actionBuilder = application.injector.instanceOf[DefaultActionBuilder]


            when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
              .thenReturn(Future.successful(incorectEnrolment))

            val authAction = new AuthenticatedIdentifierAction(mockAuthConnector,
              appConfig,
              bodyParsers
            )
            val controller = new Harness(authAction, actionBuilder)
            val result = controller.onPageLoad()(FakeRequest())

            status(result) mustEqual SEE_OTHER
            redirectLocation(result).value mustBe routes.EnrolmentRequiredController.onPageLoad().url
          }
        }
      }


      "if the user does have any enrolments" - {
        "must redirect to Enrolment Required Page" in {
          val application = applicationBuilder(None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
            val appConfig = application.injector.instanceOf[FrontendAppConfig]
            val actionBuilder = application.injector.instanceOf[DefaultActionBuilder]


            when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
              .thenReturn(Future.successful(Enrolments(Set.empty)))

            val authAction = new AuthenticatedIdentifierAction(mockAuthConnector,
              appConfig,
              bodyParsers
            )
            val controller = new Harness(authAction, actionBuilder)
            val result = controller.onPageLoad()(FakeRequest())

            status(result) mustEqual SEE_OTHER
            redirectLocation(result).value mustBe routes.EnrolmentRequiredController.onPageLoad().url
          }
        }
      }

      "if the user has HMRC-SS-ORG enrolment but there is no EORI" - {
        "must redirect to EORI Required Page" in {
          val application = applicationBuilder(None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
            val appConfig = application.injector.instanceOf[FrontendAppConfig]
            val actionBuilder = application.injector.instanceOf[DefaultActionBuilder]


            when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
              .thenReturn(Future.successful(ssEnrolmentNoEORI))

            val authAction = new AuthenticatedIdentifierAction(mockAuthConnector,
              appConfig,
              bodyParsers
            )
            val controller = new Harness(authAction, actionBuilder)
            val result = controller.onPageLoad()(FakeRequest())

            status(result) mustEqual SEE_OTHER
            redirectLocation(result).value mustBe routes.EORIRequiredController.onPageLoad().url
          }
        }
      }

      "if the user has HMRC-SS-ORG but not active" - {
        "must redirect to Enrolment Required Page" in {
          val application = applicationBuilder(None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
            val appConfig = application.injector.instanceOf[FrontendAppConfig]
            val actionBuilder = application.injector.instanceOf[DefaultActionBuilder]


            when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
              .thenReturn(Future.successful(inactiveSSEnrolment))

            val authAction = new AuthenticatedIdentifierAction(mockAuthConnector,
              appConfig,
              bodyParsers
            )
            val controller = new Harness(authAction, actionBuilder)
            val result = controller.onPageLoad()(FakeRequest())

            status(result) mustEqual SEE_OTHER
            redirectLocation(result).value mustBe routes.EnrolmentRequiredController.onPageLoad().url
          }
        }
      }
    }


/*
    "when the user hasn't logged in" - {

      "must redirect the user to log in " in {

        val application = applicationBuilder(userAnswers = None).build()

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new MissingBearerToken),
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value must startWith(appConfig.loginUrl)
        }
      }
    }

    "the user's session has expired" - {

      "must redirect the user to log in " in {

        val application = applicationBuilder(userAnswers = None).build()

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new BearerTokenExpired),
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value must startWith(appConfig.loginUrl)
        }
      }
    }

    "the user doesn't have sufficient enrolments" - {

      "must redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new InsufficientEnrolments),
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.UnauthorisedController.onPageLoad.url
        }
      }
    }

    "the user doesn't have sufficient confidence level" - {

      "must redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new InsufficientConfidenceLevel),
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.UnauthorisedController.onPageLoad.url
        }
      }
    }

    "the user used an unaccepted auth provider" - {

      "must redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new UnsupportedAuthProvider),
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.UnauthorisedController.onPageLoad.url
        }
      }
    }

    "the user has an unsupported affinity group" - {

      "must redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new UnsupportedAffinityGroup),
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
        }
      }
    }

    "the user has an unsupported credential role" - {

      "must redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new UnsupportedCredentialRole),
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
        }
      }
    }*/
  }
}

class FakeFailingAuthConnector @Inject() (exceptionToReturn: Throwable) extends AuthConnector {
  val serviceUrl: String = ""

  override def authorise[A](
    predicate: Predicate,
    retrieval: Retrieval[A]
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[A] =
    Future.failed(exceptionToReturn)
}
