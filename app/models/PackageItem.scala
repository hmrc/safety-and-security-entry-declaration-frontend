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

package models

import play.api.libs.json._

trait PackageItem {
  val kind: KindOfPackage
}

object PackageItem {

  implicit val reads: Reads[PackageItem] =
    BulkPackageItem.reads.widen[PackageItem] orElse
      UnpackedPackageItem.reads.widen[PackageItem] orElse
      StandardPackageItem.reads.widen[PackageItem]
}

final case class BulkPackageItem(
                                  kind: KindOfPackage,
                                  markOrNumber: Option[String]
                                ) extends PackageItem

object BulkPackageItem {

  implicit lazy val reads: Reads[BulkPackageItem] = {

    import play.api.libs.functional.syntax._

    (__ \ "kindOfPackage").read[KindOfPackage].flatMap[KindOfPackage] {
      k =>
        KindOfPackage.bulkKindsOfPackage
          .find(_ == k)
          .map(k => Reads(_ => JsSuccess(k)))
          .getOrElse(Reads(_ => JsError("Kind of package was not found in the list of bulk kinds of package")))
    }.andKeep(
      (
        (__ \ "kindOfPackage").read[KindOfPackage] and
        (__ \ "markOrNumber").readNullable[String]
      )(BulkPackageItem(_, _))
    )
  }
}

final case class UnpackedPackageItem(
                                      kind: KindOfPackage,
                                      numberOfPieces: Int,
                                      markOrNumber: Option[String]
                                    ) extends PackageItem

object UnpackedPackageItem {

  implicit lazy val reads: Reads[UnpackedPackageItem] = {

    import play.api.libs.functional.syntax._

    (__ \ "kindOfPackage").read[KindOfPackage].flatMap[KindOfPackage] {
      k =>
        KindOfPackage.unpackedKindsOfPackage
          .find(_ == k)
          .map(k => Reads(_ => JsSuccess(k)))
          .getOrElse(Reads(_ => JsError("Kind of package was not found in the list of unpacked kinds of package")))
    }.andKeep(
      (
        (__ \ "kindOfPackage").read[KindOfPackage] and
          (__ \ "numberOfPieces").read[Int] and
          (__ \ "markOrNumber").readNullable[String]
        )(UnpackedPackageItem(_, _, _))
    )
  }
}

final case class StandardPackageItem(
                                      kind: KindOfPackage,
                                      numberOfPackages: Int,
                                      markOrNumber: String
                                    ) extends PackageItem

object StandardPackageItem {

  implicit lazy val reads: Reads[StandardPackageItem] = {

    import play.api.libs.functional.syntax._

    (__ \ "kindOfPackage").read[KindOfPackage].flatMap[KindOfPackage] {
      k =>
        KindOfPackage.standardKindsOfPackages
          .find(_ == k)
          .map(k => Reads(_ => JsSuccess(k)))
          .getOrElse(Reads(_ => JsError("Kind of package was not found in the list of standard kinds of package")))
    }.andKeep(
        (
          (__ \ "kindOfPackage").read[KindOfPackage] and
          (__ \ "numberOfPackages").read[Int] and
          (__ \ "markOrNumber").read[String]
        )(StandardPackageItem(_, _, _))
      )
    }
}
