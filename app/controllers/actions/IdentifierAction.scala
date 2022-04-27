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

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.routes
import models.requests.IdentifierRequest
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core.AffinityGroup.Organisation
import uk.gov.hmrc.auth.core.{CredentialStrength, _}
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.~
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.http.HeaderCarrierConverter
import scala.concurrent.{ExecutionContext, Future}

trait IdentifierAction
  extends ActionBuilder[IdentifierRequest, AnyContent]
  with ActionFunction[Request, IdentifierRequest]

class AuthenticatedIdentifierAction @Inject() (
  override val authConnector: AuthConnector,
  config: FrontendAppConfig,
  val parser: BodyParsers.Default
)(implicit val executionContext: ExecutionContext)
  extends IdentifierAction
  with AuthorisedFunctions {

  override def invokeBlock[A](
    request: Request[A],
    block: IdentifierRequest[A] => Future[Result]
  ): Future[Result] = {

    implicit val hc: HeaderCarrier =
      HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    authorised(
      AuthProviders(AuthProvider.GovernmentGateway) and
        CredentialStrength(CredentialStrength.strong)
    ).retrieve(
      Retrievals.allEnrolments and
        Retrievals.affinityGroup
    ) {
      case userEnrolments ~ Some(Organisation) =>
        val eoris: Set[String] = userEnrolments.enrolments
          .flatMap(enrolment => enrolment.getIdentifier(config.eoriNumber).map(EORI => EORI.value))

        if (eoris.isEmpty) throw InsufficientEnrolments("EORI_missing")

        val SNSenrolments =
          userEnrolments.enrolments.filter(enrolment => enrolment.isActivated && enrolment.key == config.enrolment)

        if (SNSenrolments.isEmpty) throw InsufficientEnrolments("HMRC-SS-ORG_missing")

        SNSenrolments
          .flatMap(enrolment => enrolment.getIdentifier(config.eoriNumber).map(EORI => EORI.value))
          .headOption
          .fold(throw InsufficientEnrolments("EORI_missing"))(EORI => block(IdentifierRequest(request, EORI)))
      case _ => throw UnsupportedAffinityGroup()
    } recover {
      case failedAuthentication: InsufficientEnrolments =>
        if (failedAuthentication.msg == "EORI_missing") {
          Redirect(routes.EORIRequiredController.onPageLoad)
        } else {
          Redirect(routes.EnrolmentRequiredController.onPageLoad)
        }
      case _: NoActiveSession =>
        Redirect(config.loginUrl, Map("continue" -> Seq(config.loginContinueUrl)))

      case _: IncorrectCredentialStrength =>
        Redirect(
          config.mfaUpliftUrl,
          Map(
            "origin" -> Seq(config.origin),
            "continueUrl" -> Seq(config.loginContinueUrl)
          )
        )
      case _: UnsupportedAffinityGroup => Redirect(routes.OrganisationAccountRequiredController.onPageLoad)
      case _: InsufficientConfidenceLevel => Redirect(config.loginUrl, Map("continue" -> Seq(config.loginContinueUrl)))
      case _: AuthorisationException =>
        Redirect(routes.UnauthorisedController.onPageLoad)
    }
  }
}

class SessionIdentifierAction @Inject() (
  val parser: BodyParsers.Default
)(implicit val executionContext: ExecutionContext)
  extends IdentifierAction {

  override def invokeBlock[A](
    request: Request[A],
    block: IdentifierRequest[A] => Future[Result]
  ): Future[Result] = {

    implicit val hc: HeaderCarrier =
      HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    hc.sessionId match {
      case Some(session) =>
        block(IdentifierRequest(request, session.value))
      case None =>
        Future.successful(Redirect(routes.JourneyRecoveryController.onPageLoad()))
    }
  }
}
