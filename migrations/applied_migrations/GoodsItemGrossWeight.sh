#!/bin/bash

echo ""
echo "Applying migration GoodsItemGrossWeight"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/goodsItemGrossWeight                  controllers.GoodsItemGrossWeightController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/goodsItemGrossWeight                  controllers.GoodsItemGrossWeightController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeGoodsItemGrossWeight                        controllers.GoodsItemGrossWeightController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeGoodsItemGrossWeight                        controllers.GoodsItemGrossWeightController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "goodsItemGrossWeight.title = GoodsItemGrossWeight" >> ../conf/messages.en
echo "goodsItemGrossWeight.heading = GoodsItemGrossWeight" >> ../conf/messages.en
echo "goodsItemGrossWeight.checkYourAnswersLabel = GoodsItemGrossWeight" >> ../conf/messages.en
echo "goodsItemGrossWeight.error.nonNumeric = Enter your goodsItemGrossWeight using numbers" >> ../conf/messages.en
echo "goodsItemGrossWeight.error.required = Enter your goodsItemGrossWeight" >> ../conf/messages.en
echo "goodsItemGrossWeight.error.wholeNumber = Enter your goodsItemGrossWeight using whole numbers" >> ../conf/messages.en
echo "goodsItemGrossWeight.error.outOfRange = GoodsItemGrossWeight must be between {0} and {1}" >> ../conf/messages.en
echo "goodsItemGrossWeight.change.hidden = GoodsItemGrossWeight" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsItemGrossWeightUserAnswersEntry: Arbitrary[(GoodsItemGrossWeightPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GoodsItemGrossWeightPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsItemGrossWeightPage: Arbitrary[GoodsItemGrossWeightPage.type] =";\
    print "    Arbitrary(GoodsItemGrossWeightPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GoodsItemGrossWeightPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration GoodsItemGrossWeight completed"
