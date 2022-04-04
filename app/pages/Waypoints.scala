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

trait Waypoints {

  val mode: Mode
  def push(waypoint: Waypoint): Waypoints
  def pop: Waypoints
}

case class NonEmptyWaypoints(list: NonEmptyList[Waypoint]) extends Waypoints {

  val current: Waypoint = list.head
  override val mode: Mode = current.mode

  override def push(waypoint: Waypoint): Waypoints =
    NonEmptyWaypoints(NonEmptyList(waypoint, list.toList))

  def pop: Waypoints = list.tail match {
    case Nil => EmptyWaypoints
    case t => NonEmptyWaypoints(NonEmptyList(t.head, t.tail))
  }

  override def toString: String = list.toList.map(_.urlFragment).mkString(",")
}

case object EmptyWaypoints extends Waypoints {

  override val mode: Mode = NormalMode

  override def push(waypoint: Waypoint): Waypoints =
    NonEmptyWaypoints(NonEmptyList(waypoint, Nil))

  override def pop: Waypoints = this
}

object Waypoints {

  def apply(waypoints: List[Waypoint]): Waypoints =
    waypoints match {
      case Nil => EmptyWaypoints
      case h :: t => NonEmptyWaypoints(NonEmptyList(h, t))
    }

  def fromString(s: String): Option[Waypoints] =
    s.split(',').toList
      .map(Waypoint.fromString)
      .sequence
      .map(apply)

  implicit def queryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[Waypoints] =
    new QueryStringBindable[Waypoints] {

      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Waypoints]] = {
        params.get(key).map {
          data =>
            Try(Waypoints.fromString(data.head)) match {
              case Success(maybeStack) =>
                maybeStack
                  .map(Right(_))
                  .getOrElse(Left("Unable to bind parameter as waypoints"))

              case Failure(_) =>
                Left("Unable to bind parameter as waypoints")
            }
        }
      }

      override def unbind(key: String, value: Waypoints): String =
        stringBinder.unbind(key, value.toString)
    }
}
