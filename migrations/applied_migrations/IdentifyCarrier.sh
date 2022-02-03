#!/bin/bash

echo ""
echo "Applying migration IdentifyCarrier"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/identifyCarrier                        controllers.IdentifyCarrierController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/identifyCarrier                        controllers.IdentifyCarrierController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeIdentifyCarrier                  controllers.IdentifyCarrierController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeIdentifyCarrier                  controllers.IdentifyCarrierController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "identifyCarrier.title = How do you want to identify the carrier?" >> ../conf/messages.en
echo "identifyCarrier.heading = How do you want to identify the carrier?" >> ../conf/messages.en
echo "identifyCarrier.gb.eori = GB EORI" >> ../conf/messages.en
echo "identifyCarrier.nameAddress = Name/address" >> ../conf/messages.en
echo "identifyCarrier.checkYourAnswersLabel = How do you want to identify the carrier?" >> ../conf/messages.en
echo "identifyCarrier.error.required = Select identifyCarrier" >> ../conf/messages.en
echo "identifyCarrier.change.hidden = IdentifyCarrier" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIdentifyCarrierUserAnswersEntry: Arbitrary[(IdentifyCarrierPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[IdentifyCarrierPage.type]";\
    print "        value <- arbitrary[IdentifyCarrier].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIdentifyCarrierPage: Arbitrary[IdentifyCarrierPage.type] =";\
    print "    Arbitrary(IdentifyCarrierPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIdentifyCarrier: Arbitrary[IdentifyCarrier] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(IdentifyCarrier.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(IdentifyCarrierPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Migration IdentifyCarrier completed"
