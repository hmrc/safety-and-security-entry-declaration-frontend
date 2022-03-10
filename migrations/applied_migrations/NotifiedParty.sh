#!/bin/bash

echo ""
echo "Applying migration NotifiedParty"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/notifiedParty                        controllers.goods.NotifiedPartyController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/notifiedParty                        controllers.goods.NotifiedPartyController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeNotifiedParty                  controllers.goods.NotifiedPartyController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeNotifiedParty                  controllers.goods.NotifiedPartyController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "notifiedParty.title = Your notified parties" >> ../conf/messages.en
echo "notifiedParty.heading = Your notified parties" >> ../conf/messages.en
echo "notifiedParty.option1 = Option 1" >> ../conf/messages.en
echo "notifiedParty.option2 = Option 2" >> ../conf/messages.en
echo "notifiedParty.checkYourAnswersLabel = Your notified parties" >> ../conf/messages.en
echo "notifiedParty.error.required = Select notifiedParty" >> ../conf/messages.en
echo "notifiedParty.change.hidden = NotifiedParty" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyUserAnswersEntry: Arbitrary[(NotifiedPartyPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NotifiedPartyPage.type]";\
    print "        value <- arbitrary[NotifiedParty].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyPage: Arbitrary[NotifiedPartyPage.type] =";\
    print "    Arbitrary(NotifiedPartyPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedParty: Arbitrary[NotifiedParty] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(NotifiedParty.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NotifiedPartyPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration NotifiedParty completed"
