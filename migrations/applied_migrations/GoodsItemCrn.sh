#!/bin/bash

echo ""
echo "Applying migration GoodsItemCrn"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/goodsItemCrn                        controllers.GoodsItemCrnController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/goodsItemCrn                        controllers.GoodsItemCrnController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeGoodsItemCrn                  controllers.GoodsItemCrnController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeGoodsItemCrn                  controllers.GoodsItemCrnController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "goodsItemCrn.title = goodsItemCrn" >> ../conf/messages.en
echo "goodsItemCrn.heading = goodsItemCrn" >> ../conf/messages.en
echo "goodsItemCrn.checkYourAnswersLabel = goodsItemCrn" >> ../conf/messages.en
echo "goodsItemCrn.error.required = Enter goodsItemCrn" >> ../conf/messages.en
echo "goodsItemCrn.error.length = GoodsItemCrn must be 70 characters or less" >> ../conf/messages.en
echo "goodsItemCrn.change.hidden = GoodsItemCrn" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsItemCrnUserAnswersEntry: Arbitrary[(GoodsItemCrnPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GoodsItemCrnPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsItemCrnPage: Arbitrary[GoodsItemCrnPage.type] =";\
    print "    Arbitrary(GoodsItemCrnPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GoodsItemCrnPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration GoodsItemCrn completed"
