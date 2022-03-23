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

/**
 * Identify the user "sending" the message, represented by EORI number and "branch"
 *
 * @param eori The EORI number of the user submitting the declaration
 * @param branch This is either "GB" + VAT reg number + "000", for VAT-registered businesses, or
 *               "GB" + a unique number issued by HMRC for non-VAT-registered businesses.
 */
case class MessageSender(eori: String, branch: String)
