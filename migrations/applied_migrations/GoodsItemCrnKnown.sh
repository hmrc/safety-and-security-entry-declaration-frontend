#!/bin/bash

echo ""
echo "Applying migration GoodsItemCrnKnown"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/goodsItemCrnKnown                        controllers.GoodsItemCrnKnownController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/goodsItemCrnKnown                        controllers.GoodsItemCrnKnownController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeGoodsItemCrnKnown                  controllers.GoodsItemCrnKnownController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeGoodsItemCrnKnown                  controllers.GoodsItemCrnKnownController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "goodsItemCrnKnown.title = goodsItemCrnKnown" >> ../conf/messages.en
echo "goodsItemCrnKnown.heading = goodsItemCrnKnown" >> ../conf/messages.en
echo "goodsItemCrnKnown.checkYourAnswersLabel = goodsItemCrnKnown" >> ../conf/messages.en
echo "goodsItemCrnKnown.error.required = Select yes if goodsItemCrnKnown" >> ../conf/messages.en
echo "goodsItemCrnKnown.change.hidden = GoodsItemCrnKnown" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsItemCrnKnownUserAnswersEntry: Arbitrary[(GoodsItemCrnKnownPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GoodsItemCrnKnownPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsItemCrnKnownPage: Arbitrary[GoodsItemCrnKnownPage.type] =";\
    print "    Arbitrary(GoodsItemCrnKnownPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GoodsItemCrnKnownPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration GoodsItemCrnKnown completed"
