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

trait AddToListSection

case object ConsigneeSection extends AddToListSection
case object NotifiedPartySection extends AddToListSection
case object ConsignorSection extends AddToListSection
case object CountryEnRouteSection extends AddToListSection
case object PlaceOfLoadingSection extends AddToListSection
case object PlaceOfUnloadingSection extends AddToListSection
case object DocumentSection extends AddToListSection
case object PackageSection extends AddToListSection
case object ContainerSection extends AddToListSection
case object GoodsItemSection extends AddToListSection
case object SealSection extends AddToListSection
case object OverallDocumentSection extends AddToListSection
