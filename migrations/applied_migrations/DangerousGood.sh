#!/bin/bash

echo ""
echo "Applying migration DangerousGood"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/dangerousGood                        controllers.DangerousGoodController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/dangerousGood                        controllers.DangerousGoodController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeDangerousGood                  controllers.DangerousGoodController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeDangerousGood                  controllers.DangerousGoodController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "dangerousGood.title = dangerousGood" >> ../conf/messages.en
echo "dangerousGood.heading = dangerousGood" >> ../conf/messages.en
echo "dangerousGood.checkYourAnswersLabel = dangerousGood" >> ../conf/messages.en
echo "dangerousGood.error.required = Select yes if dangerousGood" >> ../conf/messages.en
echo "dangerousGood.change.hidden = DangerousGood" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDangerousGoodUserAnswersEntry: Arbitrary[(DangerousGoodPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DangerousGoodPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDangerousGoodPage: Arbitrary[DangerousGoodPage.type] =";\
    print "    Arbitrary(DangerousGoodPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DangerousGoodPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration DangerousGood completed"
