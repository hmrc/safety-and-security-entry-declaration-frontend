#!/bin/bash

echo ""
echo "Applying migration Consignee"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/consignee                        controllers.goods.ConsigneeController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/consignee                        controllers.goods.ConsigneeController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeConsignee                  controllers.goods.ConsigneeController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeConsignee                  controllers.goods.ConsigneeController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "consignee.title = Your consignees" >> ../conf/messages.en
echo "consignee.heading = Your consignees" >> ../conf/messages.en
echo "consignee.option1 = Option 1" >> ../conf/messages.en
echo "consignee.option2 = Option 2" >> ../conf/messages.en
echo "consignee.checkYourAnswersLabel = Your consignees" >> ../conf/messages.en
echo "consignee.error.required = Select consignee" >> ../conf/messages.en
echo "consignee.change.hidden = Consignee" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeUserAnswersEntry: Arbitrary[(ConsigneePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ConsigneePage.type]";\
    print "        value <- arbitrary[Consignee].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneePage: Arbitrary[ConsigneePage.type] =";\
    print "    Arbitrary(ConsigneePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsignee: Arbitrary[Consignee] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(Consignee.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ConsigneePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration Consignee completed"
