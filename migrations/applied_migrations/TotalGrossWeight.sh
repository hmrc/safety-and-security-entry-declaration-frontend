#!/bin/bash

echo ""
echo "Applying migration TotalGrossWeight"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /totalGrossWeight                  controllers.TotalGrossWeightController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /totalGrossWeight                  controllers.TotalGrossWeightController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTotalGrossWeight                        controllers.TotalGrossWeightController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTotalGrossWeight                        controllers.TotalGrossWeightController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "totalGrossWeight.title = TotalGrossWeight" >> ../conf/messages.en
echo "totalGrossWeight.heading = TotalGrossWeight" >> ../conf/messages.en
echo "totalGrossWeight.checkYourAnswersLabel = TotalGrossWeight" >> ../conf/messages.en
echo "totalGrossWeight.error.nonNumeric = Enter your totalGrossWeight using numbers" >> ../conf/messages.en
echo "totalGrossWeight.error.required = Enter your totalGrossWeight" >> ../conf/messages.en
echo "totalGrossWeight.error.wholeNumber = Enter your totalGrossWeight using whole numbers" >> ../conf/messages.en
echo "totalGrossWeight.error.outOfRange = TotalGrossWeight must be between {0} and {1}" >> ../conf/messages.en
echo "totalGrossWeight.change.hidden = TotalGrossWeight" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTotalGrossWeightUserAnswersEntry: Arbitrary[(TotalGrossWeightPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TotalGrossWeightPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTotalGrossWeightPage: Arbitrary[TotalGrossWeightPage.type] =";\
    print "    Arbitrary(TotalGrossWeightPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TotalGrossWeightPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Migration TotalGrossWeight completed"
