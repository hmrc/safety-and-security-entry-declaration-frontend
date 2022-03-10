#!/bin/bash

echo ""
echo "Applying migration Consignor"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/consignor                        controllers.goods.ConsignorController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/consignor                        controllers.goods.ConsignorController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeConsignor                  controllers.goods.ConsignorController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeConsignor                  controllers.goods.ConsignorController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "consignor.title = Your consignors" >> ../conf/messages.en
echo "consignor.heading = Your consignors" >> ../conf/messages.en
echo "consignor.option1 = Option 1" >> ../conf/messages.en
echo "consignor.option2 = Option 2" >> ../conf/messages.en
echo "consignor.checkYourAnswersLabel = Your consignors" >> ../conf/messages.en
echo "consignor.error.required = Select consignor" >> ../conf/messages.en
echo "consignor.change.hidden = Consignor" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsignorUserAnswersEntry: Arbitrary[(ConsignorPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ConsignorPage.type]";\
    print "        value <- arbitrary[Consignor].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsignorPage: Arbitrary[ConsignorPage.type] =";\
    print "    Arbitrary(ConsignorPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsignor: Arbitrary[Consignor] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(Consignor.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ConsignorPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration Consignor completed"
