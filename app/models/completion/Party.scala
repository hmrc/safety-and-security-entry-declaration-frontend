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

package models.completion

import models.Address

/**
 * This identifies any party reported within the XML payload, by EORI or name + address
 *
 * This applies to several different parties, e.g. consignors, consignees, declaring person,
 * notified party...
 */
sealed trait Party

object Party {
  case class ByEori(eori: String) extends Party
  case class ByAddress(name: String, address: Address) extends Party
}
