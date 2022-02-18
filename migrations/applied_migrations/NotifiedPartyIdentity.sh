#!/bin/bash

echo ""
echo "Applying migration NotifiedPartyIdentity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/notifiedPartyIdentity                        controllers.NotifiedPartyIdentityController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/notifiedPartyIdentity                        controllers.NotifiedPartyIdentityController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeNotifiedPartyIdentity                  controllers.NotifiedPartyIdentityController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeNotifiedPartyIdentity                  controllers.NotifiedPartyIdentityController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "notifiedPartyIdentity.title = How do you want to identify the notified party?" >> ../conf/messages.en
echo "notifiedPartyIdentity.heading = How do you want to identify the notified party?" >> ../conf/messages.en
echo "notifiedPartyIdentity.gBEORI = I'll provide the notified party's GB EORI number" >> ../conf/messages.en
echo "notifiedPartyIdentity.nameAddress = I'll provide the notified party's name and address" >> ../conf/messages.en
echo "notifiedPartyIdentity.checkYourAnswersLabel = How do you want to identify the notified party?" >> ../conf/messages.en
echo "notifiedPartyIdentity.error.required = Select notifiedPartyIdentity" >> ../conf/messages.en
echo "notifiedPartyIdentity.change.hidden = NotifiedPartyIdentity" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyIdentityUserAnswersEntry: Arbitrary[(NotifiedPartyIdentityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NotifiedPartyIdentityPage.type]";\
    print "        value <- arbitrary[NotifiedPartyIdentity].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyIdentityPage: Arbitrary[NotifiedPartyIdentityPage.type] =";\
    print "    Arbitrary(NotifiedPartyIdentityPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyIdentity: Arbitrary[NotifiedPartyIdentity] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(NotifiedPartyIdentity.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NotifiedPartyIdentityPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration NotifiedPartyIdentity completed"
