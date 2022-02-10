#!/bin/bash

echo ""
echo "Applying migration GoodsDescription"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/goodsDescription                        controllers.GoodsDescriptionController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/goodsDescription                        controllers.GoodsDescriptionController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeGoodsDescription                  controllers.GoodsDescriptionController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeGoodsDescription                  controllers.GoodsDescriptionController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "goodsDescription.title = goodsDescription" >> ../conf/messages.en
echo "goodsDescription.heading = goodsDescription" >> ../conf/messages.en
echo "goodsDescription.checkYourAnswersLabel = goodsDescription" >> ../conf/messages.en
echo "goodsDescription.error.required = Enter goodsDescription" >> ../conf/messages.en
echo "goodsDescription.error.length = GoodsDescription must be 280 characters or less" >> ../conf/messages.en
echo "goodsDescription.change.hidden = GoodsDescription" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsDescriptionUserAnswersEntry: Arbitrary[(GoodsDescriptionPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GoodsDescriptionPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGoodsDescriptionPage: Arbitrary[GoodsDescriptionPage.type] =";\
    print "    Arbitrary(GoodsDescriptionPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GoodsDescriptionPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration GoodsDescription completed"
