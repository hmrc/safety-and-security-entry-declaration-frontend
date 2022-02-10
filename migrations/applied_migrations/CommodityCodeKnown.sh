#!/bin/bash

echo ""
echo "Applying migration CommodityCodeKnown"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/commodityCodeKnown                        controllers.CommodityCodeKnownController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/commodityCodeKnown                        controllers.CommodityCodeKnownController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeCommodityCodeKnown                  controllers.CommodityCodeKnownController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeCommodityCodeKnown                  controllers.CommodityCodeKnownController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "commodityCodeKnown.title = commodityCodeKnown" >> ../conf/messages.en
echo "commodityCodeKnown.heading = commodityCodeKnown" >> ../conf/messages.en
echo "commodityCodeKnown.checkYourAnswersLabel = commodityCodeKnown" >> ../conf/messages.en
echo "commodityCodeKnown.error.required = Select yes if commodityCodeKnown" >> ../conf/messages.en
echo "commodityCodeKnown.change.hidden = CommodityCodeKnown" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCommodityCodeKnownUserAnswersEntry: Arbitrary[(CommodityCodeKnownPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CommodityCodeKnownPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCommodityCodeKnownPage: Arbitrary[CommodityCodeKnownPage.type] =";\
    print "    Arbitrary(CommodityCodeKnownPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CommodityCodeKnownPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration CommodityCodeKnown completed"
