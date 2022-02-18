#!/bin/bash

echo ""
echo "Applying migration ConsigneeIdentity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/consigneeIdentity                        controllers.ConsigneeIdentityController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/consigneeIdentity                        controllers.ConsigneeIdentityController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeConsigneeIdentity                  controllers.ConsigneeIdentityController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeConsigneeIdentity                  controllers.ConsigneeIdentityController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "consigneeIdentity.title = How do you want to identify the cosignee?" >> ../conf/messages.en
echo "consigneeIdentity.heading = How do you want to identify the cosignee?" >> ../conf/messages.en
echo "consigneeIdentity.gBEORI = I'll provide the consignee's GB EORI number" >> ../conf/messages.en
echo "consigneeIdentity.nameAddress = I'll provide the consignee's name and address" >> ../conf/messages.en
echo "consigneeIdentity.checkYourAnswersLabel = How do you want to identify the cosignee?" >> ../conf/messages.en
echo "consigneeIdentity.error.required = Select consigneeIdentity" >> ../conf/messages.en
echo "consigneeIdentity.change.hidden = ConsigneeIdentity" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeIdentityUserAnswersEntry: Arbitrary[(ConsigneeIdentityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ConsigneeIdentityPage.type]";\
    print "        value <- arbitrary[ConsigneeIdentity].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeIdentityPage: Arbitrary[ConsigneeIdentityPage.type] =";\
    print "    Arbitrary(ConsigneeIdentityPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeIdentity: Arbitrary[ConsigneeIdentity] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(ConsigneeIdentity.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ConsigneeIdentityPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ConsigneeIdentity completed"
