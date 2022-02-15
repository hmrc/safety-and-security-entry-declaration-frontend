#!/bin/bash

echo ""
echo "Applying migration ConsignorsIdentity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/consignorsIdentity                        controllers.ConsignorsIdentityController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/consignorsIdentity                        controllers.ConsignorsIdentityController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeConsignorsIdentity                  controllers.ConsignorsIdentityController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeConsignorsIdentity                  controllers.ConsignorsIdentityController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "consignorsIdentity.title = How do you want to identify the consignor?" >> ../conf/messages.en
echo "consignorsIdentity.heading = How do you want to identify the consignor?" >> ../conf/messages.en
echo "consignorsIdentity.gb-eori = I'll provide the consignor's GB EORI number" >> ../conf/messages.en
echo "consignorsIdentity.name-address = I'll provide the consignor's name and address" >> ../conf/messages.en
echo "consignorsIdentity.checkYourAnswersLabel = How do you want to identify the consignor?" >> ../conf/messages.en
echo "consignorsIdentity.error.required = Select consignorsIdentity" >> ../conf/messages.en
echo "consignorsIdentity.change.hidden = ConsignorsIdentity" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsignorsIdentityUserAnswersEntry: Arbitrary[(ConsignorsIdentityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ConsignorsIdentityPage.type]";\
    print "        value <- arbitrary[ConsignorsIdentity].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsignorsIdentityPage: Arbitrary[ConsignorsIdentityPage.type] =";\
    print "    Arbitrary(ConsignorsIdentityPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsignorsIdentity: Arbitrary[ConsignorsIdentity] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(ConsignorsIdentity.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ConsignorsIdentityPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ConsignorsIdentity completed"
