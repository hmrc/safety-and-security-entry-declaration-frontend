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
 * A 5-digit "special mention" code needed to indicate specific scenarios in the declaration
 *
 * e.g. when a notified party is provided for a goods item, we use code 10600
 *
 * TODO: When we have a code list this model may change to be more in line with similar enums
 * like Country or PaymentMethod.
 */
case class SpecialMention(code: String)
