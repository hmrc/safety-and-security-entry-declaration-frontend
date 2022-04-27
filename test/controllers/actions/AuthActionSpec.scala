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
import org.mockito.{Mockito, MockitoSugar}
import org.scalatest.BeforeAndAfterEach
import play.api.mvc.{Action, AnyContent, BodyParsers, DefaultActionBuilder, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.AffinityGroup.{Agent, Individual, Organisation}
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.{Retrieval, ~}
import uk.gov.hmrc.http.{HeaderCarrier, HeaderNames}
import auth.Retrievals._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class AuthActionSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {

  private type RetrievalsType = Enrolments ~ Option[AffinityGroup]
  private val SNSenrolmentWithEORI =
    Enrolment("HMRC-SS-ORG", Seq(EnrolmentIdentifier("EORINumber", "123456789")), "Activated")
  private val inactiveSNSEnrolment =
    Enrolment("HMRC-SS-ORG", Seq(EnrolmentIdentifier("EORINumber", "123456789")), "Inactive")
  private val SNSenrolmentNoEORI = Enrolment("HMRC-SS-ORG", Seq(EnrolmentIdentifier("ARN", "123456789")), "Activated")
  private val nonSNSEnrolmentWithEORI =
    Enrolment("HMRC-AA-ORG", Seq(EnrolmentIdentifier("EORINumber", "123456789")), "Activated")
  private val nonSNSEnrolmentNoEORI =
    Enrolment("HMRC-BB-ORG", Seq(EnrolmentIdentifier("Test", "123456789")), "Activated")
  private val anotherNonSNSEnrolmentNoEORI =
    Enrolment("HMRC-CC-ORG", Seq(EnrolmentIdentifier("Test", "123456789")), "Activated")
  private val application = applicationBuilder(None).build()
  private val mockAuthConnector: AuthConnector = mock[AuthConnector]
  private val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
  private val appConfig = application.injector.instanceOf[FrontendAppConfig]
  private val actionBuilder = application.injector.instanceOf[DefaultActionBuilder]

  class Harness(authAction: IdentifierAction, defaultAction: DefaultActionBuilder) {
    def onPageLoad(): Action[AnyContent] = (defaultAction andThen authAction) { _ => Results.Ok }
  }

  override def beforeEach(): Unit = {
    Mockito.reset(mockAuthConnector)
  }

  "A user being authenticated" - {
    "Will successfully login" - {
      "When the user is logged in as Organisation with strong credentials, enrolment HMRC-SS-ORG and EORI" in {
        running(application) {
          when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
            .thenReturn(Future.successful(Enrolments(Set(SNSenrolmentWithEORI)) ~ Some(Organisation)))

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, appConfig, bodyParsers)
          val controller = new Harness(authAction, actionBuilder)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustEqual OK
        }
      }
    }
    "Will fail authentication" - {
      "When the user is an Individual" - {
        "And will redirect to Organisation Required Page" in {
          running(application) {
            when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
              .thenReturn(Future.successful(Enrolments(Set(SNSenrolmentWithEORI)) ~ Some(Individual)))

            val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, appConfig, bodyParsers)
            val controller = new Harness(authAction, actionBuilder)
            val result = controller.onPageLoad()(FakeRequest())

            status(result) mustEqual SEE_OTHER
            redirectLocation(result).value mustBe routes.OrganisationAccountRequiredController.onPageLoad().url
          }
        }
      }
      "When the user is an Agent" - {
        "And will redirect to Organisation Required Page" in {
          running(application) {
            when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
              .thenReturn(Future.successful(Enrolments(Set(SNSenrolmentWithEORI)) ~ Some(Agent)))

            val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, appConfig, bodyParsers)
            val controller = new Harness(authAction, actionBuilder)
            val result = controller.onPageLoad()(FakeRequest())

            status(result) mustEqual SEE_OTHER
            redirectLocation(result).value mustBe routes.OrganisationAccountRequiredController.onPageLoad().url
          }
        }
      }
      "When the enrolments" - {
        "Do not contain HMRC-SS-ORG and do not contain EORI" - {
          "And will redirect to EORI Required Page" in {
            running(application) {
              when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
                .thenReturn(
                  Future.successful(
                    Enrolments(Set(nonSNSEnrolmentNoEORI, anotherNonSNSEnrolmentNoEORI)) ~ Some(Organisation)
                  )
                )

              val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, appConfig, bodyParsers)
              val controller = new Harness(authAction, actionBuilder)
              val result = controller.onPageLoad()(FakeRequest())

              status(result) mustEqual SEE_OTHER
              redirectLocation(result).value mustBe routes.EORIRequiredController.onPageLoad().url
            }
          }
        }
        "Do not contain HMRC-SS-ORG but contain EORI" - {
          "And will redirect to Enrolment Required Page" in {
            running(application) {
              when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
                .thenReturn(Future.successful(Enrolments(Set(nonSNSEnrolmentWithEORI)) ~ Some(Organisation)))

              val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, appConfig, bodyParsers)
              val controller = new Harness(authAction, actionBuilder)
              val result = controller.onPageLoad()(FakeRequest())

              status(result) mustEqual SEE_OTHER
              redirectLocation(result).value mustBe routes.EnrolmentRequiredController.onPageLoad().url
            }
          }
        }
        "When there are no enrolments at all" - {
          "And will redirect to EORI Required Page" in {
            running(application) {
              when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
                .thenReturn(Future.successful(Enrolments(Set.empty) ~ Some(Organisation)))

              val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, appConfig, bodyParsers)
              val controller = new Harness(authAction, actionBuilder)
              val result = controller.onPageLoad()(FakeRequest())

              status(result) mustEqual SEE_OTHER
              redirectLocation(result).value mustBe routes.EORIRequiredController.onPageLoad().url
            }
          }
        }
        "Contain HMRC-SS-ORG but the EORI is missing" - {
          "And will redirect to EORI Required Page" in {
            running(application) {
              when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
                .thenReturn(Future.successful(Enrolments(Set(SNSenrolmentNoEORI)) ~ Some(Organisation)))

              val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, appConfig, bodyParsers)
              val controller = new Harness(authAction, actionBuilder)
              val result = controller.onPageLoad()(FakeRequest())

              status(result) mustEqual SEE_OTHER
              redirectLocation(result).value mustBe routes.EORIRequiredController.onPageLoad().url
            }
          }
        }
        "Contain HMRC-SS-ORG but it is inactive" - {
          "And will redirect to Enrolment Required Page" in {
            running(application) {
              when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any()))
                .thenReturn(Future.successful(Enrolments(Set(inactiveSNSEnrolment)) ~ Some(Organisation)))

              val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, appConfig, bodyParsers)
              val controller = new Harness(authAction, actionBuilder)
              val result = controller.onPageLoad()(FakeRequest())

              status(result) mustEqual SEE_OTHER
              redirectLocation(result).value mustBe routes.EnrolmentRequiredController.onPageLoad().url
            }
          }
        }
      }
      "When the user is not logged in" in {
        running(application) {
          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new MissingBearerToken),
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction, actionBuilder)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value must startWith(appConfig.loginUrl)
        }
      }
      "When the user's session has expired" in {
        running(application) {
          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new BearerTokenExpired),
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction, actionBuilder)
          val result = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value must startWith(appConfig.loginUrl)
        }
      }
    }
    "When the user has weak credentials" - {
      "Will be redirected to enable Multi Factor Authentication" in {
        running(application) {
          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new IncorrectCredentialStrength),
            appConfig,
            bodyParsers
          )

          val controller = new Harness(authAction, actionBuilder)
          val result = controller.onPageLoad()(FakeRequest().withHeaders(HeaderNames.xSessionId -> "123"))

          status(result) mustEqual SEE_OTHER
          redirectLocation(
            result
          ).value mustBe "http://localhost:9553/bas-gateway/uplift-mfa?origin=SNS&continueUrl=http%3A%2F%2Flocalhost%3A11200%2Fsafety-and-security-entry-declaration%2F%3Fk%3D123"
        }
      }
    }
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
