#!/bin/bash

echo ""
echo "Applying migration NotifiedPartyAddress"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/notifiedPartyAddress                        controllers.NotifiedPartyAddressController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/notifiedPartyAddress                        controllers.NotifiedPartyAddressController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeNotifiedPartyAddress                  controllers.NotifiedPartyAddressController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeNotifiedPartyAddress                  controllers.NotifiedPartyAddressController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "notifiedPartyAddress.title = notifiedPartyAddress" >> ../conf/messages.en
echo "notifiedPartyAddress.heading = notifiedPartyAddress" >> ../conf/messages.en
echo "notifiedPartyAddress.checkYourAnswersLabel = notifiedPartyAddress" >> ../conf/messages.en
echo "notifiedPartyAddress.error.required = Enter notifiedPartyAddress" >> ../conf/messages.en
echo "notifiedPartyAddress.error.length = NotifiedPartyAddress must be 35 characters or less" >> ../conf/messages.en
echo "notifiedPartyAddress.change.hidden = NotifiedPartyAddress" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyAddressUserAnswersEntry: Arbitrary[(NotifiedPartyAddressPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NotifiedPartyAddressPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyAddressPage: Arbitrary[NotifiedPartyAddressPage.type] =";\
    print "    Arbitrary(NotifiedPartyAddressPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NotifiedPartyAddressPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration NotifiedPartyAddress completed"
