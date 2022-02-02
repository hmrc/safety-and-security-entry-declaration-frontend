#!/bin/bash

echo ""
echo "Applying migration GrossWeight"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /grossWeight                        controllers.GrossWeightController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /grossWeight                        controllers.GrossWeightController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeGrossWeight                  controllers.GrossWeightController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeGrossWeight                  controllers.GrossWeightController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "grossWeight.title = How do you want to give the gross weight of the goods?" >> ../conf/messages.en
echo "grossWeight.heading = How do you want to give the gross weight of the goods?" >> ../conf/messages.en
echo "grossWeight.overall = Overall" >> ../conf/messages.en
echo "grossWeight.perItem = Per item" >> ../conf/messages.en
echo "grossWeight.checkYourAnswersLabel = How do you want to give the gross weight of the goods?" >> ../conf/messages.en
echo "grossWeight.error.required = Select grossWeight" >> ../conf/messages.en
echo "grossWeight.change.hidden = GrossWeight" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGrossWeightUserAnswersEntry: Arbitrary[(GrossWeightPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GrossWeightPage.type]";\
    print "        value <- arbitrary[GrossWeight].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGrossWeightPage: Arbitrary[GrossWeightPage.type] =";\
    print "    Arbitrary(GrossWeightPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGrossWeight: Arbitrary[GrossWeight] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(GrossWeight.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GrossWeightPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Migration GrossWeight completed"
