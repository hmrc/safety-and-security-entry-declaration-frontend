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

import cats.implicits.toTraverseOps
import play.api.mvc.QueryStringBindable

import scala.util.{Failure, Success, Try}

case class Breadcrumbs(list: List[Breadcrumb[_]]) {

  def current[_]: Option[Breadcrumb[_]] = list.headOption

  def push(breadcrumb: Breadcrumb[_]): Breadcrumbs = Breadcrumbs(breadcrumb +: list)
  def pop: Breadcrumbs = Breadcrumbs(list.tail)

  override def toString: String = list.map(_.urlFragment).mkString(",")
}

object Breadcrumbs {

  def empty: Breadcrumbs = Breadcrumbs(Nil)

  def fromString(s: String): Option[Breadcrumbs] =
    s.split(',').toList
      .map(Breadcrumb.fromString)
      .sequence
      .map(Breadcrumbs(_))

  implicit def queryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[Breadcrumbs] =
    new QueryStringBindable[Breadcrumbs] {

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Breadcrumbs]] = {
        params.get(key).map {
          data =>
            Try(Breadcrumbs.fromString(data.head)) match {
              case Success(maybeStack) =>
                maybeStack
                  .map(Right(_))
                  .getOrElse(Left("Unable to bind parameter as a mode stack"))

              case Failure(_) =>
                Left("Unable to bind parameter as a mode stack")
            }
        }
      }

      override def unbind(key: String, value: Breadcrumbs): String =
        stringBinder.unbind(key, value.toString)
    }
}
