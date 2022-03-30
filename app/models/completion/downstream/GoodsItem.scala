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

package models.completion.downstream

import models.{Container, Document, PaymentMethod}
import models.completion.LoadingPlace

/**
 * Represents a single goods item in the XML payload (GOOITEGDS)
 */
case class GoodsItem(
  itemNumber: Int,
  itemIdentity: GoodsItemIdentity,
  grossMass: Option[BigDecimal],
  paymentMethod: PaymentMethod,
  dangerousGoodsCode: Option[DangerousGoodsCode],
  placeOfLoading: LoadingPlace,
  placeOfUnloading: LoadingPlace,
  documents: List[Document],
  consignor: Party,
  consignee: Option[Party],
  notifiedParty: Option[Party],
  containers: List[Container],
  packages: List[Package],
  specialMentions: List[SpecialMention]
)
