#!/bin/bash

echo ""
echo "Applying migration GoodsPassThroughOtherCountries"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/goodsPassThroughOtherCountries                        controllers.GoodsPassThroughOtherCountriesController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/goodsPassThroughOtherCountries                        controllers.GoodsPassThroughOtherCountriesController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeGoodsPassThroughOtherCountries                  controllers.GoodsPassThroughOtherCountriesController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeGoodsPassThroughOtherCountries                  controllers.GoodsPassThroughOtherCountriesController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "goodsPassThroughOtherCountries.title = goodsPassThroughOtherCountries" >> ../conf/messages.en
echo "goodsPassThroughOtherCountries.heading = goodsPassThroughOtherCountries" >> ../conf/messages.en
echo "goodsPassThroughOtherCountries.checkYourAnswersLabel = goodsPassThroughOtherCountries" >> ../conf/messages.en
echo "goodsPassThroughOtherCountries.error.required = Select yes if goodsPassThroughOtherCountries" >> ../conf/messages.en
echo "goodsPassThroughOtherCountries.change.hidden = GoodsPassThroughOtherCountries" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsPassThroughOtherCountriesUserAnswersEntry: Arbitrary[(GoodsPassThroughOtherCountriesPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GoodsPassThroughOtherCountriesPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsPassThroughOtherCountriesPage: Arbitrary[GoodsPassThroughOtherCountriesPage.type] =";\
    print "    Arbitrary(GoodsPassThroughOtherCountriesPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GoodsPassThroughOtherCountriesPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration GoodsPassThroughOtherCountries completed"
