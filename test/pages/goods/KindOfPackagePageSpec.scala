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

package pages.goods

import base.SpecBase
import controllers.goods.{routes => goodsRoutes}
import models.KindOfPackage
import org.scalacheck.Gen
import pages.behaviours.PageBehaviours
import pages.{EmptyWaypoints, Waypoints}

class KindOfPackagePageSpec extends SpecBase with PageBehaviours {

  "KindOfPackagePage" - {

    val bulkPackage = Gen.oneOf(KindOfPackage.bulkKindsOfPackage).sample.value
    val standardPackage = Gen.oneOf(KindOfPackage.standardKindsOfPackages).sample.value
    val unpackedPackage = Gen.oneOf(KindOfPackage.unpackedKindsOfPackage).sample.value

    "must navigate when there are no waypoints" - {

      val waypoints = EmptyWaypoints

      "to Add Mark or Number when the answer is a bulk kind of package" in {

        val answers = emptyUserAnswers.set(KindOfPackagePage(index, index), bulkPackage).success.value

        KindOfPackagePage(index, index)
          .navigate(waypoints, answers)
          .mustEqual(
            goodsRoutes.AddMarkOrNumberController.onPageLoad(waypoints, answers.lrn, index, index)
          )
      }

      "to Number of Packages when the answer is a standard kind of package" in {

        val answers = emptyUserAnswers.set(KindOfPackagePage(index, index), standardPackage).success.value

        KindOfPackagePage(index, index)
          .navigate(waypoints, answers)
          .mustEqual(
            goodsRoutes.NumberOfPackagesController.onPageLoad(waypoints, answers.lrn, index, index)
          )
      }

      "to Number of Pieces when the answer is an unpacked kind of package" in {

        val answers = emptyUserAnswers.set(KindOfPackagePage(index, index), unpackedPackage).success.value

        KindOfPackagePage(index, index)
          .navigate(waypoints, answers)
          .mustEqual(
            goodsRoutes.NumberOfPiecesController.onPageLoad(waypoints, answers.lrn, index, index)
          )
      }
    }

    "must navigate when the current waypoint is Check Package" - {

      val waypoints = Waypoints(List(CheckPackageItemPage(index, index).waypoint))

      "when the answer is a bulk kind of package" - {

        "to Check Package with the current waypoint removed when Add Mark or Number is already answered" in{

          val answers =
            emptyUserAnswers
              .set(KindOfPackagePage(index, index), bulkPackage).success.value
              .set(AddMarkOrNumberPage(index, index), true).success.value

          KindOfPackagePage(index, index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckPackageItemController.onPageLoad(EmptyWaypoints, answers.lrn, index, index))
        }

        "to Add Mark or Number when it is not already answered" in {

          val answers =
            emptyUserAnswers
              .set(KindOfPackagePage(index, index), bulkPackage).success.value

          KindOfPackagePage(index, index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.AddMarkOrNumberController.onPageLoad(waypoints, answers.lrn, index, index))
        }
      }

      "when the answer is a standard kind of package" - {

        "to Check Package with the current waypoint removed when Number of Packages is already answered" in{

          val answers =
            emptyUserAnswers
              .set(KindOfPackagePage(index, index), standardPackage).success.value
              .set(NumberOfPackagesPage(index, index), 1).success.value

          KindOfPackagePage(index, index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckPackageItemController.onPageLoad(EmptyWaypoints, answers.lrn, index, index))
        }

        "to Number of Packages when it is not already answered" in {

          val answers =
            emptyUserAnswers
              .set(KindOfPackagePage(index, index), standardPackage).success.value

          KindOfPackagePage(index, index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.NumberOfPackagesController.onPageLoad(waypoints, answers.lrn, index, index))
        }
      }

      "when the answer is an unpacked kind of package" - {

        "to Check Package with the current waypoint removed when Number of Pieces is already answered" in{

          val answers =
            emptyUserAnswers
              .set(KindOfPackagePage(index, index), unpackedPackage).success.value
              .set(NumberOfPiecesPage(index, index), 1).success.value

          KindOfPackagePage(index, index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.CheckPackageItemController.onPageLoad(EmptyWaypoints, answers.lrn, index, index))
        }

        "to Number of Pieces when it is not already answered" in {

          val answers =
            emptyUserAnswers
              .set(KindOfPackagePage(index, index), unpackedPackage).success.value

          KindOfPackagePage(index, index).navigate(waypoints, answers)
            .mustEqual(goodsRoutes.NumberOfPiecesController.onPageLoad(waypoints, answers.lrn, index, index))
        }
      }
    }

    "must remove Number of Pieces and Number of Packages when the answer is a bulk kind of package" in {

      val answers =
        emptyUserAnswers
          .set(NumberOfPackagesPage(index, index), 1).success.value
          .set(NumberOfPiecesPage(index, index), 2).success.value
          .set(AddMarkOrNumberPage(index, index), true).success.value
          .set(MarkOrNumberPage(index, index), "mark").success.value

      val result = answers.set(KindOfPackagePage(index, index), bulkPackage).success.value

      result.get(NumberOfPackagesPage(index, index)) must not be defined
      result.get(NumberOfPiecesPage(index, index)) must not be defined
      result.get(AddMarkOrNumberPage(index, index)).value mustEqual true
      result.get(MarkOrNumberPage(index, index)).value mustEqual "mark"
    }

    "must remove Number of Pieces and Add Mark or Number when the answer is a standard kind of package" in {

      val answers =
        emptyUserAnswers
          .set(NumberOfPackagesPage(index, index), 1).success.value
          .set(NumberOfPiecesPage(index, index), 2).success.value
          .set(AddMarkOrNumberPage(index, index), true).success.value
          .set(MarkOrNumberPage(index, index), "mark").success.value

      val result = answers.set(KindOfPackagePage(index, index), standardPackage).success.value

      result.get(NumberOfPackagesPage(index, index)).value mustEqual 1
      result.get(NumberOfPiecesPage(index, index)) must not be defined
      result.get(AddMarkOrNumberPage(index, index)) must not be defined
      result.get(MarkOrNumberPage(index, index)).value mustEqual "mark"
    }

    "must remove Number of Packages when the answer is an unpacked kind of package" in {

      val answers =
        emptyUserAnswers
          .set(NumberOfPackagesPage(index, index), 1).success.value
          .set(NumberOfPiecesPage(index, index), 2).success.value
          .set(AddMarkOrNumberPage(index, index), true).success.value
          .set(MarkOrNumberPage(index, index), "mark").success.value

      val result = answers.set(KindOfPackagePage(index, index), unpackedPackage).success.value

      result.get(NumberOfPackagesPage(index, index)) must not be defined
      result.get(NumberOfPiecesPage(index, index)).value mustEqual 2
      result.get(AddMarkOrNumberPage(index, index)).value mustEqual true
      result.get(MarkOrNumberPage(index, index)).value mustEqual "mark"
    }
  }
}
