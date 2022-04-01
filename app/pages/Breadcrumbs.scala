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

package pages

import cats.data.NonEmptyList
import cats.implicits.toTraverseOps
import models.{Mode, NormalMode}
import play.api.mvc.QueryStringBindable

import scala.util.{Failure, Success, Try}

trait Breadcrumbs {

  val mode: Mode
  def push(breadcrumb: Breadcrumb): Breadcrumbs
  def pop: Breadcrumbs
}

case class NonEmptyBreadcrumbs(list: NonEmptyList[Breadcrumb]) extends Breadcrumbs {

  val current: Breadcrumb = list.head
  override val mode: Mode = current.mode

  override def push(breadcrumb: Breadcrumb): Breadcrumbs =
    NonEmptyBreadcrumbs(NonEmptyList(breadcrumb, list.toList))

  def pop: Breadcrumbs = list.tail match {
    case Nil => EmptyBreadcrumbs
    case t => NonEmptyBreadcrumbs(NonEmptyList(t.head, t.tail))
  }

  override def toString: String = list.toList.map(_.urlFragment).mkString(",")
}

case object EmptyBreadcrumbs extends Breadcrumbs {

  override val mode: Mode = NormalMode

  override def push(breadcrumb: Breadcrumb): Breadcrumbs =
    NonEmptyBreadcrumbs(NonEmptyList(breadcrumb, Nil))

  override def pop: Breadcrumbs = this
}

object Breadcrumbs {

  def apply(breadcrumbs: List[Breadcrumb]): Breadcrumbs =
    breadcrumbs match {
      case Nil => EmptyBreadcrumbs
      case h :: t => NonEmptyBreadcrumbs(NonEmptyList(h, t))
    }

  def fromString(s: String): Option[Breadcrumbs] =
    s.split(',').toList
      .map(Breadcrumb.fromString)
      .sequence
      .map(apply)

  implicit def queryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[Breadcrumbs] =
    new QueryStringBindable[Breadcrumbs] {

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Breadcrumbs]] = {
        params.get(key).map {
          data =>
            Try(Breadcrumbs.fromString(data.head)) match {
              case Success(maybeStack) =>
                maybeStack
                  .map(Right(_))
                  .getOrElse(Left("Unable to bind parameter as breadcrumbs"))

              case Failure(_) =>
                Left("Unable to bind parameter as breadcrumbs")
            }
        }
      }

      override def unbind(key: String, value: Breadcrumbs): String =
        stringBinder.unbind(key, value.toString)
    }
}
