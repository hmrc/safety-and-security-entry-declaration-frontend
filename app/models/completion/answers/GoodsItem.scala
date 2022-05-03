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

package models.completion.answers

import models.completion.Party.ByEori
import models.{Container, Document, PaymentMethod}
import models.completion.{LoadingPlace, Party}
import models.completion.downstream.{DangerousGoodsCode, GoodsItemIdentity, Package}

/**
 * Represents a single goods item in the Extractor
 */

case class GoodsItem(
  id: GoodsItemIdentity,
  consignor: Party,
  consignee: Option[Party],
  notifiedParty: Option[Party],
  placeOfLoading: LoadingPlace,
  placeOfUnloading: LoadingPlace,
  containers: List[Container],
  packages: List[Package],
  grossMass: Option[BigDecimal],
  documents: List[Document],
  dangerousGoodsCode: Option[DangerousGoodsCode],
  paymentMethod: PaymentMethod
)
